package com.pointwest.bootcamp.eventhubri.modules.billing.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.OrganizationRepository;
import com.pointwest.bootcamp.eventhubri.modules.billing.dto.InvoiceResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.dto.PlatformRateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.InvoiceItem;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.OrganizationInvoice;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.PlatformRate;
import com.pointwest.bootcamp.eventhubri.modules.billing.repository.EventRegistrationRepository;
import com.pointwest.bootcamp.eventhubri.modules.billing.repository.OrganizationInvoiceRepository;
import com.pointwest.bootcamp.eventhubri.modules.billing.repository.PlatformRateRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final PlatformRateRepository platformRateRepository;
    private final OrganizationInvoiceRepository organizationInvoiceRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public PlatformRate setBaseRate(PlatformRateRequestDto request) {
        platformRateRepository.findByIsActiveTrue().ifPresent(current -> {
            current.setActive(false);
            if (current.getEffectiveEndDate() == null) {
                current.setEffectiveEndDate(request.effectiveStartDate().minusDays(1));
            }
            platformRateRepository.save(current);
        });

        PlatformRate newRate = PlatformRate.builder()
                .ratePerAttendee(request.ratePerAttendee())
                .effectiveStartDate(request.effectiveStartDate())
                .effectiveEndDate(request.effectiveEndDate())
                .isActive(true)
                .build();

        return platformRateRepository.save(newRate);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getAllInvoices() {
        return organizationInvoiceRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesByPeriod(LocalDate periodStart, LocalDate periodEnd) {
        return organizationInvoiceRepository
                .findByBillingPeriodStartAndBillingPeriodEnd(periodStart, periodEnd)
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesByOrganization(Long organizationId) {
        return organizationInvoiceRepository.findByOrganization_Id(organizationId)
                .stream().map(this::toDto).toList();
    }

    @Transactional
    public InvoiceResponseDto generateInvoice(Long organizationId, LocalDate periodStart, LocalDate periodEnd) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + organizationId));

        PlatformRate applicableRate = platformRateRepository.findApplicableRateForPeriod(periodStart, periodEnd)
                .orElseGet(() -> platformRateRepository.findByIsActiveTrue()
                        .orElseThrow(() -> new IllegalStateException(
                                "No platform rate configured for the requested billing period")));

        List<EventRegistrationRepository.EventRegistrationSummary> registrationSummaries = eventRegistrationRepository
                .findConfirmedRegistrationsByEventForOrganizationAndPeriod(
                        organizationId,
                        periodStart.atStartOfDay(),
                        periodEnd.atTime(LocalTime.MAX));

        List<InvoiceItem> invoiceItems = new ArrayList<>();
        int totalAttendeeCount = 0;
        BigDecimal invoiceAmount = BigDecimal.ZERO;

        for (EventRegistrationRepository.EventRegistrationSummary summary : registrationSummaries) {
            Event event = summary.getEvent();
            int eventAttendeeCount = summary.getRegistrationCount() == null ? 0
                    : Math.toIntExact(summary.getRegistrationCount());
            BigDecimal lineTotal = applicableRate.getRatePerAttendee().multiply(BigDecimal.valueOf(eventAttendeeCount));

            totalAttendeeCount += eventAttendeeCount;
            invoiceAmount = invoiceAmount.add(lineTotal);

            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .event(event)
                    .eventAttendeeCount(eventAttendeeCount)
                    .rateApplied(applicableRate.getRatePerAttendee())
                    .lineTotal(lineTotal)
                    .build();

            invoiceItems.add(invoiceItem);
        }

        OrganizationInvoice invoice = OrganizationInvoice.builder()
                .organization(organization)
                .invoiceNumber(generateInvoiceNumber(organizationId))
                .billingPeriodStart(periodStart)
                .billingPeriodEnd(periodEnd)
                .totalAttendeeCount(totalAttendeeCount)
                .appliedRatePerAttendee(applicableRate.getRatePerAttendee())
                .invoiceAmount(invoiceAmount)
                .paymentStatus(OrganizationInvoice.PaymentStatus.UNPAID)
                .issuedAt(LocalDateTime.now())
                .dueDate(LocalDate.now().plusDays(30))
                .items(new ArrayList<>())
                .build();

        for (InvoiceItem invoiceItem : invoiceItems) {
            invoiceItem.setInvoice(invoice);
            invoice.getItems().add(invoiceItem);
        }

        OrganizationInvoice savedInvoice = organizationInvoiceRepository.save(invoice);
        return toDto(savedInvoice);
    }

    @Transactional(readOnly = true)
    public byte[] exportInvoiceAsPdf(Long invoiceId) {
        OrganizationInvoice invoice = organizationInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + invoiceId));
        return buildInvoicePdf(invoice);
    }

    private byte[] buildInvoicePdf(OrganizationInvoice invoice) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDFont fontHelvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDFont fontHelveticaBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPageContentStream content = new PDPageContentStream(document, page);
            try {
                float margin = 50f;
                float y = 780f;

                drawText(content, margin, y, "INVOICE", fontHelveticaBold, 20);
                y -= 26;
                drawText(content, margin, y, invoice.getInvoiceNumber(), fontHelveticaBold, 12);
                y -= 24;

                drawText(content, margin, y, "Billed To:", fontHelveticaBold, 10);
                y -= 14;
                drawText(content, margin, y, invoice.getOrganization().getCompanyName(), fontHelvetica, 11);
                y -= 14;
                drawText(content, margin, y, invoice.getOrganization().getPrimaryContactEmail(), fontHelvetica, 10);

                float rightX = 360f;
                drawText(content, rightX, 780f, "Issue Date:", fontHelveticaBold, 10);
                drawText(content, rightX + 90f, 780f, invoice.getIssuedAt().toLocalDate().toString(), fontHelvetica,
                        10);
                drawText(content, rightX, 766f, "Due Date:", fontHelveticaBold, 10);
                drawText(content, rightX + 90f, 766f, invoice.getDueDate().toString(), fontHelvetica, 10);
                drawText(content, rightX, 752f, "Billing Period:", fontHelveticaBold, 10);
                drawText(content, rightX + 90f, 752f,
                        invoice.getBillingPeriodStart() + " to " + invoice.getBillingPeriodEnd(), fontHelvetica, 10);

                y -= 65;
                drawLine(content, margin, y, 545f, y, fontHelvetica, 1f);
                y -= 16;

                drawText(content, margin, y, "Event", fontHelveticaBold, 10);
                drawText(content, margin + 260f, y, "Attendees", fontHelveticaBold, 10);
                drawText(content, margin + 360f, y, "Rate", fontHelveticaBold, 10);
                drawText(content, margin + 450f, y, "Amount", fontHelveticaBold, 10);
                y -= 14;
                drawLine(content, margin, y, 545f, y, fontHelvetica, 0.8f);
                y -= 18;

                if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
                    drawText(content, margin, y, "No event charges for this billing period.", fontHelvetica, 10);
                    y -= 20;
                } else {
                    for (var item : invoice.getItems()) {
                        String eventTitle = item.getEvent() != null && item.getEvent().getTitle() != null
                                ? item.getEvent().getTitle()
                                : "Unknown Event";
                        String attendees = String.valueOf(item.getEventAttendeeCount());
                        String rate = formatMoney(item.getRateApplied());
                        String amount = formatMoney(item.getLineTotal());

                        if (y < 140f) {
                            content.close();
                            PDPage nextPage = new PDPage(PDRectangle.A4);
                            document.addPage(nextPage);
                            content = new PDPageContentStream(document, nextPage);
                            y = 780f;
                        }

                        drawText(content, margin, y, truncate(eventTitle, 28), fontHelvetica, 9);
                        drawText(content, margin + 270f, y, attendees, fontHelvetica, 9);
                        drawText(content, margin + 360f, y, rate, fontHelvetica, 9);
                        drawText(content, margin + 450f, y, amount, fontHelvetica, 9);
                        y -= 18;
                    }
                }

                y -= 22;
                drawLine(content, margin, y, 545f, y, fontHelvetica, 0.8f);
                y -= 20;
                drawText(content, margin + 290f, y, "Total Attendees:", fontHelveticaBold, 10);
                drawText(content, margin + 450f, y, String.valueOf(invoice.getTotalAttendeeCount()), fontHelvetica, 10);
                y -= 18;
                drawText(content, margin + 290f, y, "Rate / attendee:", fontHelveticaBold, 10);
                drawText(content, margin + 450f, y, formatMoney(invoice.getAppliedRatePerAttendee()), fontHelvetica,
                        10);
                y -= 18;
                drawText(content, margin + 290f, y, "Total Amount:", fontHelveticaBold, 11);
                drawText(content, margin + 450f, y, formatMoney(invoice.getInvoiceAmount()), fontHelveticaBold, 11);
                y -= 18;
                drawText(content, margin + 290f, y, "Status:", fontHelveticaBold, 10);
                drawText(content, margin + 450f, y, invoice.getPaymentStatus().name(), fontHelvetica, 10);
                y -= 18;
                drawText(content, margin + 290f, y, "Due Date:", fontHelveticaBold, 10);
                drawText(content, margin + 450f, y, invoice.getDueDate().toString(), fontHelvetica, 10);
            } finally {
                content.close();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to generate invoice PDF", e);
        }
    }

    private void drawText(PDPageContentStream content, float x, float y, String text,
            PDFont font, int size) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    private void drawLine(PDPageContentStream content, float startX, float startY, float endX, float endY,
            PDFont font, float width) throws IOException {
        content.setLineWidth(width);
        content.moveTo(startX, startY);
        content.lineTo(endX, endY);
        content.stroke();
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            return "PHP 0.00";
        }
        return String.format("PHP %.2f", value);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private String generateInvoiceNumber(Long organizationId) {
        return "INV-" + organizationId + "-" + System.currentTimeMillis();
    }

    @Transactional
    public List<InvoiceResponseDto> generateBatchInvoices(LocalDate periodStart, LocalDate periodEnd) {
        List<Organization> activeOrganizations = organizationRepository.findByStatus(Organization.Status.ACTIVE);
        return activeOrganizations.stream()
                .map(org -> generateInvoice(org.getId(), periodStart, periodEnd))
                .toList();
    }

    private InvoiceResponseDto toDto(OrganizationInvoice invoice) {
        List<InvoiceResponseDto.InvoiceLineItem> items = invoice.getItems().stream()
                .map(item -> new InvoiceResponseDto.InvoiceLineItem(
                        item.getEvent().getId(),
                        item.getEvent().getTitle(),
                        item.getEventAttendeeCount(),
                        item.getRateApplied(),
                        item.getLineTotal()))
                .toList();

        return new InvoiceResponseDto(
                invoice.getId(),
                invoice.getOrganization().getId(),
                invoice.getOrganization().getCompanyName(),
                invoice.getOrganization().getPrimaryContactEmail(),
                invoice.getInvoiceNumber(),
                invoice.getBillingPeriodStart(),
                invoice.getBillingPeriodEnd(),
                invoice.getTotalAttendeeCount(),
                invoice.getAppliedRatePerAttendee(),
                invoice.getInvoiceAmount(),
                invoice.getPaymentStatus(),
                invoice.getIssuedAt(),
                invoice.getDueDate(),
                items);
    }
}
package com.pointwest.bootcamp.eventhubri.modules.billing.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont; // <-- Add this import
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.OrganizationRepository;
import com.pointwest.bootcamp.eventhubri.modules.billing.dto.InvoiceResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.dto.PlatformRateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.OrganizationInvoice;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.PlatformRate;
import com.pointwest.bootcamp.eventhubri.modules.billing.repository.EventRegistrationRepository;
import com.pointwest.bootcamp.eventhubri.modules.billing.repository.OrganizationInvoiceRepository;
import com.pointwest.bootcamp.eventhubri.modules.billing.repository.PlatformRateRepository;

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

        PlatformRate activeRate = platformRateRepository.findByIsActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No active platform rate configured"));

        long confirmedCount = eventRegistrationRepository.countConfirmedRegistrationsForOrganization(
                organizationId,
                periodStart.atStartOfDay(),
                periodEnd.atTime(LocalTime.MAX));

        BigDecimal invoiceAmount = activeRate.getRatePerAttendee()
                .multiply(BigDecimal.valueOf(confirmedCount));

        OrganizationInvoice invoice = OrganizationInvoice.builder()
                .organization(organization)
                .invoiceNumber(generateInvoiceNumber(organizationId))
                .billingPeriodStart(periodStart)
                .billingPeriodEnd(periodEnd)
                .totalAttendeeCount((int) confirmedCount)
                .appliedRatePerAttendee(activeRate.getRatePerAttendee())
                .invoiceAmount(invoiceAmount)
                .paymentStatus(OrganizationInvoice.PaymentStatus.UNPAID)
                .issuedAt(LocalDateTime.now())
                .dueDate(LocalDate.now().plusDays(30))
                .build();

        return toDto(organizationInvoiceRepository.save(invoice));
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

            // Instantiate PDFBox 3.x Standard 14 Fonts
            PDFont fontHelvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDFont fontHelveticaBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float y = 750;
                y = writeLine(content, "Invoice " + invoice.getInvoiceNumber(), y, fontHelveticaBold, 16);
                y = writeLine(content, "Organization: " + invoice.getOrganization().getCompanyName(), y, fontHelvetica, 11);
                y = writeLine(content, "Billing Period: " + invoice.getBillingPeriodStart() + " to "
                        + invoice.getBillingPeriodEnd(), y, fontHelvetica, 11);
                y = writeLine(content, "Total Attendees: " + invoice.getTotalAttendeeCount(), y, fontHelvetica, 11);
                y = writeLine(content, "Rate per Attendee: " + invoice.getAppliedRatePerAttendee(), y, fontHelvetica, 11);
                y = writeLine(content, "Invoice Amount: " + invoice.getInvoiceAmount(), y, fontHelveticaBold, 12);
                y = writeLine(content, "Status: " + invoice.getPaymentStatus(), y, fontHelvetica, 11);
                writeLine(content, "Due Date: " + invoice.getDueDate(), y, fontHelvetica, 11);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to generate invoice PDF", e);
        }
    }


    private float writeLine(PDPageContentStream content, String text, float y,
            PDFont font, int size) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(50, y);
        content.showText(text);
        content.endText();
        return y - 20;
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
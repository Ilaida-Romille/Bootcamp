import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarItem } from '../../../layout/sidebar/sidebar.component';
import { ROUTE_PATHS } from '../../../app.routes';
import { PLATFORM_ROUTE_PATHS } from '../platform.routes';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { InvoiceModalComponent } from './components/invoice-modal/invoice-modal.component';
import { BatchSummaryModalComponent } from './components/batch-summary-modal/batch-summary-modal.component';
import {
  Invoice,
  BatchSummary,
  BillingOrganizer,
  BillingMonth,
  BillingFeatureData
} from './models/billing-model/billing-model.component';
import { BillingDataService } from './services/billing-data.service';

declare var bootstrap: any;

@Component({
  selector: 'app-billing',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    PaginationComponent, 
    InvoiceModalComponent, 
    BatchSummaryModalComponent
  ],
  templateUrl: './billing.component.html',
  styleUrl: './billing.component.css'
})
export class BillingComponent implements OnInit {
  private readonly base = `/${ROUTE_PATHS.platformOwner}`;
  private generatedInvoiceSequence = 1;

  adminNavItems: SidebarItem[] = [
    { label: 'Dashboard', route: `${this.base}/${PLATFORM_ROUTE_PATHS.dashboard}` },
    { label: 'Organizers', route: `${this.base}/${PLATFORM_ROUTE_PATHS.organizers}` },
    { label: 'Billing & Invoices', route: `${this.base}/${PLATFORM_ROUTE_PATHS.billing}` },
    { label: 'Tickets & Requests', route: `${this.base}/${PLATFORM_ROUTE_PATHS.tickets}` }
  ];

  // Search & Filter State
  searchTerm: string = '';
  fromMonth: string = '';
  toMonth: string = '';

  // Single Organizer Invoice Generation Inputs
  selectedGenInvoiceId: string = '';

  // Dropdown Options
  organizerOptions: BillingOrganizer[] = [];
  monthOptions: BillingMonth[] = [];

  currency: string = 'PHP';
  currencySymbol: string = 'P';
  dataLoadingError: string = '';

  // Pagination State
  currentPage: number = 1;
  itemsPerPage: number = 5;

  // Selected Items for Modals
  activeInvoicePreview: Invoice | null = null;
  activeBatchSummary: BatchSummary | null = null;

  // Master Data
  invoices: Invoice[] = [];

  filteredInvoices: Invoice[] = [];

  constructor(
    private readonly billingDataService: BillingDataService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadBillingData();
  }

  get generationOrganizerOptions(): Invoice[] {
    const uniqueByOrganizer = new Map<string, Invoice>();

    const sortedFiltered = [...this.filteredInvoices].sort((a, b) =>
      b.monthCode.localeCompare(a.monthCode) || b.invoiceNumber.localeCompare(a.invoiceNumber)
    );

    for (const invoice of sortedFiltered) {
      if (!uniqueByOrganizer.has(invoice.organizerId)) {
        uniqueByOrganizer.set(invoice.organizerId, invoice);
      }
    }

    return Array.from(uniqueByOrganizer.values());
  }

  get selectedGenerationInvoice(): Invoice | null {
    return this.generationOrganizerOptions.find(inv => inv.id === this.selectedGenInvoiceId) || null;
  }

  get selectedGenOrganizerLabel(): string {
    return this.selectedGenerationInvoice?.organizerName || 'Select organizer...';
  }

  get selectedGenMonthLabel(): string {
    return this.selectedGenerationInvoice?.period || '-------------';
  }

  private loadBillingData(): void {
    this.dataLoadingError = '';

    this.billingDataService.getBillingData().subscribe({
      next: (payload: BillingFeatureData) => {
        this.currency = payload.currency;
        this.currencySymbol = payload.currencySymbol;
        this.organizerOptions = payload.organizers;
        this.monthOptions = payload.months;
        this.invoices = payload.invoices;
        this.applyFilter();
        this.cdr.detectChanges();
      },
      error: () => {
        this.invoices = [];
        this.filteredInvoices = [];
        this.dataLoadingError = 'Unable to load billing data from /data/invoices.json.';
        this.cdr.detectChanges();
      }
    });
  }

  private getMonthEndDate(monthCode: string): string {
    const [yearText, monthText] = monthCode.split('-');
    const year = Number(yearText);
    const month = Number(monthText);
    const endDate = new Date(year, month, 0);
    return endDate.toISOString().slice(0, 10);
  }

  private addDays(dateIso: string, days: number): string {
    const date = new Date(dateIso);
    date.setDate(date.getDate() + days);
    return date.toISOString().slice(0, 10);
  }

  applyFilter(): void {
    this.filteredInvoices = this.invoices.filter(inv => {
      const matchesSearch = inv.organizerName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                            inv.invoiceNumber.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      let matchesFrom = true;
      let matchesTo = true;

      if (this.fromMonth) {
        matchesFrom = inv.monthCode >= this.fromMonth;
      }
      if (this.toMonth) {
        matchesTo = inv.monthCode <= this.toMonth;
      }

      return matchesSearch && matchesFrom && matchesTo;
    });

    this.syncSelectedGenerationInvoice();
    this.currentPage = 1;
  }

  private syncSelectedGenerationInvoice(): void {
    const optionStillExists = this.generationOrganizerOptions.some(inv => inv.id === this.selectedGenInvoiceId);
    if (!optionStillExists) {
      this.selectedGenInvoiceId = '';
    }
  }

  get paginatedInvoices(): Invoice[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredInvoices.slice(startIndex, startIndex + this.itemsPerPage);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  // Modal Handlers
  openInvoiceModal(invoice: Invoice): void {
    this.activeInvoicePreview = invoice;
    const modalElement = document.getElementById('invoiceModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  generateBatchInvoices(): void {
    const targetMonth = this.monthOptions.at(-1);
    if (!targetMonth) {
      return;
    }

    let sampleBatch = this.invoices.filter(inv => inv.monthCode === targetMonth.code);

    if (sampleBatch.length === 0) {
      sampleBatch = this.organizerOptions.map((organizer, index) => {
        const attendeesCount = 30 + index * 5;
        const ratePerAttendee = 1200;
        return {
          id: `generated_${targetMonth.code}_${organizer.id}`,
          invoiceNumber: `INV-${targetMonth.code.replace('-', '')}-${index + 1}`,
          organizerId: organizer.id,
          organizerName: organizer.name,
          organizerEmail: organizer.email,
          period: targetMonth.name,
          monthCode: targetMonth.code,
          issueDate: this.getMonthEndDate(targetMonth.code),
          dueDate: this.addDays(this.getMonthEndDate(targetMonth.code), 30),
          attendeesCount,
          ratePerAttendee,
          amount: attendeesCount * ratePerAttendee,
          status: 'Pending' as const
        };
      });
    }

    this.activeBatchSummary = {
      period: targetMonth.name,
      organizersCount: sampleBatch.length,
      totalAttendees: sampleBatch.reduce((sum, item) => sum + item.attendeesCount, 0),
      totalAmount: sampleBatch.reduce((sum, item) => sum + item.amount, 0),
      invoices: sampleBatch
    };

    const modalElement = document.getElementById('batchSummaryModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  generateSingleInvoice(): void {
    if (!this.selectedGenInvoiceId) {
      return;
    }

    const selectedInvoice = this.selectedGenerationInvoice;

    if (!selectedInvoice) {
      return;
    }

    const issueDate = this.getMonthEndDate(selectedInvoice.monthCode);
    const dueDate = this.addDays(issueDate, 30);
    const previousInvoices = this.invoices.filter(inv => inv.organizerId === selectedInvoice.organizerId);
    const previousRate = previousInvoices[0]?.ratePerAttendee ?? 1000;
    const attendeesCount = previousInvoices.length > 0
      ? Math.round(previousInvoices.reduce((sum, inv) => sum + inv.attendeesCount, 0) / previousInvoices.length)
      : 30;
    const sequence = String(this.generatedInvoiceSequence++).padStart(4, '0');

    const newInvoice: Invoice = {
      id: `generated_${Date.now()}`,
      invoiceNumber: `INV-${selectedInvoice.monthCode.replace('-', '')}-${sequence}`,
      organizerId: selectedInvoice.organizerId,
      organizerName: selectedInvoice.organizerName,
      organizerEmail: selectedInvoice.organizerEmail,
      period: selectedInvoice.period,
      monthCode: selectedInvoice.monthCode,
      issueDate,
      dueDate,
      attendeesCount,
      ratePerAttendee: previousRate,
      amount: attendeesCount * previousRate,
      status: 'Pending'
    };

    this.invoices.unshift(newInvoice);
    this.applyFilter();
    this.openInvoiceModal(newInvoice);

    this.selectedGenInvoiceId = '';
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'Paid': return 'text-success';
      case 'Pending': return 'text-warning';
      case 'Overdue': return 'text-danger';
      default: return 'text-secondary';
    }
  }
}
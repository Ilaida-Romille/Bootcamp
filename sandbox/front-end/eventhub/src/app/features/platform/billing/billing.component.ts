import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
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

  private readonly billingDataService = inject(BillingDataService);
  private readonly cdr = inject(ChangeDetectorRef);

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
    return this.selectedGenInvoiceId ? this.getCurrentBillingPeriod().label : '-------------';
  }

  private loadBillingData(): void {
    this.dataLoadingError = '';

    this.billingDataService.getAllInvoices().subscribe({
      next: (invoices: Invoice[]) => {
        this.invoices = invoices;
        this.applyFilter();
        this.cdr.detectChanges();
      },
      error: () => {
        this.invoices = [];
        this.filteredInvoices = [];
        this.dataLoadingError = 'Unable to load billing data. Please check your connection.';
        this.cdr.detectChanges();
      }
    });
  }

  private getCurrentBillingPeriod(): { periodStart: string; periodEnd: string; label: string } {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const mm = String(month).padStart(2, '0');
    const lastDay = new Date(year, month, 0).getDate();
    const label = now.toLocaleString('default', { month: 'long', year: 'numeric' });
    return {
      periodStart: `${year}-${mm}-01`,
      periodEnd: `${year}-${mm}-${String(lastDay).padStart(2, '0')}`,
      label
    };
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
    const { periodStart, periodEnd, label } = this.getCurrentBillingPeriod();

    this.billingDataService.generateBatchInvoices(periodStart, periodEnd).subscribe({
      next: (batchInvoices: Invoice[]) => {
        this.invoices.unshift(...batchInvoices);
        this.applyFilter();

        this.activeBatchSummary = {
          period: label,
          organizersCount: batchInvoices.length,
          totalAttendees: batchInvoices.reduce((sum, inv) => sum + inv.attendeesCount, 0),
          totalAmount: batchInvoices.reduce((sum, inv) => sum + inv.amount, 0),
          invoices: batchInvoices
        };

        const modalElement = document.getElementById('batchSummaryModal');
        if (modalElement) {
          const modal = new bootstrap.Modal(modalElement);
          modal.show();
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.dataLoadingError = 'Failed to generate batch invoices.';
        this.cdr.detectChanges();
      }
    });
  }

  generateSingleInvoice(): void {
    if (!this.selectedGenInvoiceId) {
      return;
    }

    const selectedInvoice = this.selectedGenerationInvoice;
    if (!selectedInvoice) {
      return;
    }

    const { periodStart, periodEnd } = this.getCurrentBillingPeriod();

    this.billingDataService.generateInvoice(selectedInvoice.organizationId, periodStart, periodEnd).subscribe({
      next: (newInvoice: Invoice) => {
        this.invoices.unshift(newInvoice);
        this.applyFilter();
        this.openInvoiceModal(newInvoice);
        this.selectedGenInvoiceId = '';
        this.cdr.detectChanges();
      },
      error: () => {
        this.dataLoadingError = 'Failed to generate invoice.';
        this.cdr.detectChanges();
      }
    });
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
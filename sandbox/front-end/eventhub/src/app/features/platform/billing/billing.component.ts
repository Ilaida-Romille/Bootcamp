import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarItem } from '../../../layout/sidebar/sidebar.component';
import { ROUTE_PATHS } from '../../../app.routes';
import { PLATFORM_ROUTE_PATHS } from '../platform.routes';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { InvoiceModalComponent } from './components/invoice-modal/invoice-modal.component';
import { BatchSummaryModalComponent } from './components/batch-summary-modal/batch-summary-modal.component';
import { Invoice, BatchSummary } from './models/billing-model/billing-model.component';

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
  selectedGenOrganizer: string = '';
  selectedGenMonth: string = '';
  isOrgDropdownOpen: boolean = false;
  isMonthDropdownOpen: boolean = false;

  // Dropdown Options
  organizerOptions: string[] = ['Tech Events Inc.', 'Global Summit Co.', 'Alpha Logistics'];
  monthOptions: string[] = [
    'January 2026', 'February 2026', 'March 2026', 'April 2026',
    'May 2026', 'June 2026', 'July 2026', 'August 2026',
    'September 2026', 'October 2026', 'November 2026', 'December 2026'
  ];

  // Pagination State
  currentPage: number = 1;
  itemsPerPage: number = 5;

  // Selected Items for Modals
  activeInvoicePreview: Invoice | null = null;
  activeBatchSummary: BatchSummary | null = null;

  // Master Data
  invoices: Invoice[] = [
    {
      invoiceNumber: 'INV-2026-0001',
      organizerName: 'Tech Events Inc.',
      organizerEmail: 'billing@techevents.com',
      period: 'May 2026',
      issueDate: '2026-05-31',
      dueDate: '2026-06-30',
      attendeesCount: 450,
      ratePerAttendee: 15,
      amount: 6750,
      status: 'Paid'
    },
    {
      invoiceNumber: 'INV-2026-0002',
      organizerName: 'Global Summit Co.',
      organizerEmail: 'accounts@globalsummit.com',
      period: 'May 2026',
      issueDate: '2026-05-31',
      dueDate: '2026-06-30',
      attendeesCount: 200,
      ratePerAttendee: 15,
      amount: 3000,
      status: 'Pending'
    },
    {
      invoiceNumber: 'INV-2026-0003',
      organizerName: 'Alpha Logistics',
      organizerEmail: 'finance@alphalogistics.com',
      period: 'April 2026',
      issueDate: '2026-04-30',
      dueDate: '2026-05-30',
      attendeesCount: 120,
      ratePerAttendee: 15,
      amount: 1800,
      status: 'Overdue'
    }
  ];

  filteredInvoices: Invoice[] = [];

  ngOnInit(): void {
    this.applyFilter();
  }

  applyFilter(): void {
    this.filteredInvoices = this.invoices.filter(inv => {
      const matchesSearch = inv.organizerName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                            inv.invoiceNumber.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      let matchesFrom = true;
      let matchesTo = true;

      if (this.fromMonth) {
        matchesFrom = new Date(inv.issueDate) >= new Date(this.fromMonth + '-01');
      }
      if (this.toMonth) {
        matchesTo = new Date(inv.issueDate) <= new Date(this.toMonth + '-31');
      }

      return matchesSearch && matchesFrom && matchesTo;
    });

    this.currentPage = 1;
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
    const period = 'June 2026';
    const sampleBatch: Invoice[] = [
      {
        invoiceNumber: 'INV-2026-0004',
        organizerName: 'Tech Events Inc.',
        organizerEmail: 'billing@techevents.com',
        period,
        issueDate: '2026-06-30',
        dueDate: '2026-07-30',
        attendeesCount: 500,
        ratePerAttendee: 15,
        amount: 7500,
        status: 'Pending'
      },
      {
        invoiceNumber: 'INV-2026-0005',
        organizerName: 'Global Summit Co.',
        organizerEmail: 'accounts@globalsummit.com',
        period,
        issueDate: '2026-06-30',
        dueDate: '2026-07-30',
        attendeesCount: 300,
        ratePerAttendee: 15,
        amount: 4500,
        status: 'Pending'
      }
    ];

    this.activeBatchSummary = {
      period,
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
    if (!this.selectedGenOrganizer || !this.selectedGenMonth) return;

    const newInvoice: Invoice = {
      invoiceNumber: `INV-2026-${Math.floor(1000 + Math.random() * 9000)}`,
      organizerName: this.selectedGenOrganizer,
      organizerEmail: 'organizer@company.com',
      period: this.selectedGenMonth,
      issueDate: '2026-07-01',
      dueDate: '2026-07-31',
      attendeesCount: 150,
      ratePerAttendee: 15,
      amount: 2250,
      status: 'Pending'
    };

    this.invoices.unshift(newInvoice);
    this.applyFilter();
    this.openInvoiceModal(newInvoice);
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
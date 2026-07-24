// features/billing/components/billing-table/billing-table.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface BillingInvoice {
  id: string;
  companyName: string;
  amount: number;
  status: 'Paid' | 'Pending' | 'Overdue';
  dueDate: string;
}

@Component({
  selector: 'app-billing-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './billing-table.component.html',
})
export class BillingTableComponent {
  @Input() invoices: BillingInvoice[] = [];
  @Output() viewInvoice = new EventEmitter<string>();
  @Output() downloadInvoice = new EventEmitter<string>();

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'Paid': return 'text-success';
      case 'Pending': return 'text-warning';
      case 'Overdue': return 'text-danger';
      default: return 'text-secondary';
    }
  }
}
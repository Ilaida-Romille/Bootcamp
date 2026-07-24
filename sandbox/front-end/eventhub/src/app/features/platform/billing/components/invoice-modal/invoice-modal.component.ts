import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Invoice } from '../../models/billing-model/billing-model.component';

@Component({
  selector: 'app-invoice-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './invoice-modal.component.html',
  styleUrl: './invoice-modal.component.css'
})
export class InvoiceModalComponent {
  @Input() invoice: Invoice | null = null;

  printInvoice(): void {
    window.print();
  }
}
// features/billing/components/billing-filters/billing-filters.component.ts
import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface BillingFilterCriteria {
  searchTerm: string;
  status: string;
}

@Component({
  selector: 'app-billing-filters',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './billing-filters.component.html'
})
export class BillingFiltersComponent {
  searchTerm: string = '';
  selectedStatus: string = 'All';
  isDropdownOpen: boolean = false;
  statusOptions: string[] = ['All', 'Paid', 'Pending', 'Overdue'];

  @Output() filterChanged = new EventEmitter<BillingFilterCriteria>();

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  selectStatus(status: string): void {
    this.selectedStatus = status;
    this.isDropdownOpen = false;
    this.emitFilter();
  }

  emitFilter(): void {
    this.filterChanged.emit({
      searchTerm: this.searchTerm,
      status: this.selectedStatus
    });
  }
}
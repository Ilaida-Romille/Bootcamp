import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarItem } from '../../../layout/sidebar/sidebar.component';
import { ROUTE_PATHS } from '../../../app.routes';
import { PLATFORM_ROUTE_PATHS } from '../platform.routes';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import {
  PlatformOwnerOrganizerService,
  OrganizerApiResponse,
  UpdateOrganizerRequest
} from './services/platform-owner-organizer.service';

declare var bootstrap: any;

interface Organizer {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  company: string;
  organizationId: number | null;
  organizationName: string;
  primaryContactEmail: string;
  totalEvents: number;
  userStatus: 'ACTIVE' | 'INACTIVE';
}

@Component({
  selector: 'app-organizers',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './organizers.component.html',
  styleUrl: './organizers.component.css',
})
export class OrganizersComponent implements OnInit {
  private readonly base = `/${ROUTE_PATHS.platformOwner}`;

  adminNavItems: SidebarItem[] = [
    { label: 'Dashboard', route: `${this.base}/${PLATFORM_ROUTE_PATHS.dashboard}` },
    { label: 'Organizers', route: `${this.base}/${PLATFORM_ROUTE_PATHS.organizers}` },
    { label: 'Billing & Invoices', route: `${this.base}/${PLATFORM_ROUTE_PATHS.billing}` },
    { label: 'Tickets & Requests', route: `${this.base}/${PLATFORM_ROUTE_PATHS.tickets}` }
  ];

  // Filter state
  isDropdownOpen: boolean = false;
  selectedStatus: string = 'All';
  searchTerm: string = '';
  statusOptions: string[] = ['All', 'ACTIVE', 'INACTIVE'];
  dataLoadingError: string = '';
  actionError: string = '';

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 5;

  // Data
  organizers: Organizer[] = [];
  filteredOrganizers: Organizer[] = [];

  // Edit modal state
  editForm: UpdateOrganizerRequest = { firstName: '', lastName: '', company: '', organizationName: '' };
  editingOrganizerId: number | null = null;
  isSubmitting: boolean = false;

  // Delete modal state
  deletingOrganizerId: number | null = null;
  deletingOrganizerName: string = '';

  private readonly organizerService = inject(PlatformOwnerOrganizerService);
  private readonly cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
    this.loadOrganizers();
  }

  private loadOrganizers(): void {
    this.dataLoadingError = '';
    this.organizerService.getOrganizers().subscribe({
      next: (data) => {
        this.organizers = data.map(o => this.mapToOrganizer(o));
        this.applyFilter();
        this.cdr.detectChanges();
      },
      error: () => {
        this.organizers = [];
        this.filteredOrganizers = [];
        this.dataLoadingError = 'Unable to load organizers. Please check your connection.';
        this.cdr.detectChanges();
      }
    });
  }

  private mapToOrganizer(o: OrganizerApiResponse): Organizer {
    return {
      id: o.id,
      email: o.email,
      firstName: o.firstName,
      lastName: o.lastName,
      fullName: `${o.firstName} ${o.lastName}`,
      company: o.company ?? '',
      organizationId: o.organizationId,
      organizationName: o.organizationName ?? '',
      primaryContactEmail: o.primaryContactEmail ?? o.email,
      totalEvents: o.totalEvents,
      userStatus: o.userStatus
    };
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  selectStatus(status: string): void {
    this.selectedStatus = status;
    this.isDropdownOpen = false;
    this.applyFilter();
  }

  onSearchChange(event: Event): void {
    this.searchTerm = (event.target as HTMLInputElement).value;
    this.applyFilter();
  }

  applyFilter(): void {
    this.filteredOrganizers = this.organizers.filter(o => {
      const term = this.searchTerm.toLowerCase();
      const matchesSearch =
        o.fullName.toLowerCase().includes(term) ||
        o.organizationName.toLowerCase().includes(term) ||
        o.email.toLowerCase().includes(term);
      const matchesStatus = this.selectedStatus === 'All' || o.userStatus === this.selectedStatus;
      return matchesSearch && matchesStatus;
    });
    this.currentPage = 1;
  }

  get paginatedOrganizers(): Organizer[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredOrganizers.slice(startIndex, startIndex + this.itemsPerPage);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  getStatusBadgeClass(status: 'ACTIVE' | 'INACTIVE'): string {
    return status === 'ACTIVE' ? 'text-success' : 'text-danger';
  }

  getStatusLabel(status: 'ACTIVE' | 'INACTIVE'): string {
    return status === 'ACTIVE' ? 'Active' : 'Inactive';
  }

  // Edit modal
  openEditModal(organizer: Organizer): void {
    this.editingOrganizerId = organizer.id;
    this.editForm = {
      firstName: organizer.firstName,
      lastName: organizer.lastName,
      company: organizer.company,
      organizationName: organizer.organizationName
    };
    this.actionError = '';
    const modalEl = document.getElementById('editOrganizerModal');
    if (modalEl) {
      new bootstrap.Modal(modalEl).show();
    }
  }

  submitEdit(): void {
    if (!this.editingOrganizerId || this.isSubmitting) {
      return;
    }
    this.isSubmitting = true;
    this.actionError = '';

    this.organizerService.updateOrganizer(this.editingOrganizerId, this.editForm).subscribe({
      next: (updated) => {
        const idx = this.organizers.findIndex(o => o.id === this.editingOrganizerId);
        if (idx !== -1) {
          this.organizers[idx] = this.mapToOrganizer(updated);
        }
        this.applyFilter();
        this.isSubmitting = false;
        const modalEl = document.getElementById('editOrganizerModal');
        if (modalEl) {
          bootstrap.Modal.getInstance(modalEl)?.hide();
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.actionError = 'Failed to update organizer. Please try again.';
        this.isSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  // Status toggle
  toggleStatus(organizer: Organizer): void {
    const newStatus = organizer.userStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    const action = newStatus === 'INACTIVE' ? 'deactivate' : 'reactivate';
    const warning = newStatus === 'INACTIVE' ? ' They will immediately lose access to the platform.' : '';
    if (!confirm(`Are you sure you want to ${action} ${organizer.fullName}?${warning}`)) {
      return;
    }
    this.organizerService.toggleStatus(organizer.id, newStatus).subscribe({
      next: (updated) => {
        const idx = this.organizers.findIndex(o => o.id === organizer.id);
        if (idx !== -1) {
          this.organizers[idx] = this.mapToOrganizer(updated);
        }
        this.applyFilter();
        this.cdr.detectChanges();
      },
      error: () => {
        this.dataLoadingError = 'Failed to update organizer status. Please try again.';
        this.cdr.detectChanges();
      }
    });
  }

  // Delete modal
  openDeleteModal(organizer: Organizer): void {
    this.deletingOrganizerId = organizer.id;
    this.deletingOrganizerName = organizer.fullName || organizer.organizationName;
    this.actionError = '';
    const modalEl = document.getElementById('deleteOrganizerModal');
    if (modalEl) {
      new bootstrap.Modal(modalEl).show();
    }
  }

  confirmDelete(): void {
    if (!this.deletingOrganizerId || this.isSubmitting) {
      return;
    }
    this.isSubmitting = true;

    this.organizerService.deleteOrganizer(this.deletingOrganizerId).subscribe({
      next: () => {
        this.organizers = this.organizers.filter(o => o.id !== this.deletingOrganizerId);
        this.applyFilter();
        this.isSubmitting = false;
        const modalEl = document.getElementById('deleteOrganizerModal');
        if (modalEl) {
          bootstrap.Modal.getInstance(modalEl)?.hide();
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.actionError = 'Failed to delete organizer. They may have associated records.';
        this.isSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }
}

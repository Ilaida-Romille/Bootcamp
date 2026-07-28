import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarItem } from '../../../layout/sidebar/sidebar.component';
import { ROUTE_PATHS } from '../../../app.routes';
import { PLATFORM_ROUTE_PATHS } from '../platform.routes';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { OrganizersDataService, OrganizerStatus } from './services/organizers-data.service';

export interface Company {
  id: string;
  name: string;
  eventsCount: number;
  status: 'Active' | 'Suspended' | 'Pending';
}

export interface OrganizerFilterCriteria {
  searchTerm: string;
  status: string;
}

@Component({
  selector: 'app-organizers',
  standalone: true,
  imports: [CommonModule, PaginationComponent],
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

  // Dropdown UI State
  isDropdownOpen: boolean = false;
  selectedStatus: string = 'All';
  searchTerm: string = '';
  statusOptions: string[] = ['All', 'Active', 'Suspended', 'Pending'];
  dataLoadingError: string = '';

  // Pagination State
  currentPage: number = 1;
  itemsPerPage: number = 5;

  // Data State
  companies: Company[] = [];

  filteredCompanies: Company[] = [];

  constructor(
    private readonly organizersDataService: OrganizersDataService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrganizers();
  }

  private loadOrganizers(): void {
    this.dataLoadingError = '';

    this.organizersDataService.getOrganizers().subscribe({
      next: (organizers) => {
        this.companies = organizers.map((organizer: OrganizerStatus) => ({
          id: organizer.id,
          name: organizer.name,
          eventsCount: organizer.eventIds.length,
          status: organizer.status
        }));
        this.applyFilter();
        this.cdr.detectChanges();
      },
      error: () => {
        this.companies = [];
        this.filteredCompanies = [];
        this.dataLoadingError = 'Unable to load organizers data from /data/organizers.json.';
        this.cdr.detectChanges();
      }
    });
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
    this.filteredCompanies = this.companies.filter(company => {
      const matchesSearch = company.name.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesStatus = this.selectedStatus === 'All' || company.status === this.selectedStatus;
      return matchesSearch && matchesStatus;
    });
    
    // Reset pagination to the first page when filters change
    this.currentPage = 1; 
  }

  // Get current slice of paginated companies for the table
  get paginatedCompanies(): Company[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredCompanies.slice(startIndex, startIndex + this.itemsPerPage);
  }

  // Pagination Handler
  onPageChange(page: number): void {
    this.currentPage = page;
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'Active': return 'text-success';
      case 'Pending': return 'text-warning';
      case 'Suspended': return 'text-danger';
      default: return 'text-secondary';
    }
  }
}
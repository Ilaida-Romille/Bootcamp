import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { Event, EventStatus } from './models/event.model';
import { OrganizerEventsApiService } from './services/organizer-events-api.service';

@Component({
  selector: 'app-organizer-events',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css',
})
export class OrganizerEventsComponent implements OnInit {
  private readonly eventsApi = inject(OrganizerEventsApiService);
  private readonly cdr = inject(ChangeDetectorRef);

  events: Event[] = [];
  filteredEvents: Event[] = [];

  searchTerm = '';
  selectedOrganizer = 'All';
  selectedStatus = 'All';
  eventIdQuery = '';

  organizerOptions: string[] = ['All'];
  statusOptions: string[] = ['All'];

  currentPage = 1;
  itemsPerPage = 8;

  isLoading = false;
  isLookupLoading = false;
  isLookupMode = false;
  dataLoadingError = '';

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.isLoading = true;
    this.dataLoadingError = '';

    this.eventsApi.getEvents().subscribe({
      next: (events) => {
        this.events = [...events].sort(
          (a, b) => new Date(a.startDateTime).getTime() - new Date(b.startDateTime).getTime()
        );

        this.organizerOptions = ['All', ...new Set(this.events.map((item) => item.organizerName))];
        this.statusOptions = ['All', ...new Set(this.events.map((item) => this.formatStatus(item.status)))];

        this.applyFilters();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.events = [];
        this.filteredEvents = [];
        this.isLoading = false;
        this.dataLoadingError = this.getApiErrorMessage(error, 'Unable to load events from the API.');
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    this.isLookupMode = false;

    const normalizedSearch = this.searchTerm.trim().toLowerCase();

    this.filteredEvents = this.events.filter((event) => {
      const matchesSearch =
        normalizedSearch.length === 0 ||
        event.title.toLowerCase().includes(normalizedSearch) ||
        event.description.toLowerCase().includes(normalizedSearch) ||
        event.venue.toLowerCase().includes(normalizedSearch);

      const matchesOrganizer = this.selectedOrganizer === 'All' || event.organizerName === this.selectedOrganizer;
      const formattedStatus = this.formatStatus(event.status);
      const matchesStatus = this.selectedStatus === 'All' || formattedStatus === this.selectedStatus;

      return matchesSearch && matchesOrganizer && matchesStatus;
    });

    this.currentPage = 1;
  }

  lookupById(): void {
    const id = this.eventIdQuery.trim();
    if (!id) {
      return;
    }

    this.isLookupLoading = true;
    this.dataLoadingError = '';

    this.eventsApi.getEventById(id).subscribe({
      next: (event) => {
        this.filteredEvents = [event];
        this.isLookupMode = true;
        this.currentPage = 1;
        this.isLookupLoading = false;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.filteredEvents = [];
        this.isLookupMode = true;
        this.isLookupLoading = false;
        this.dataLoadingError = this.getApiErrorMessage(error, `No event found for ID "${id}".`);
        this.cdr.detectChanges();
      }
    });
  }

  clearLookupAndFilters(): void {
    this.eventIdQuery = '';
    this.searchTerm = '';
    this.selectedOrganizer = 'All';
    this.selectedStatus = 'All';
    this.isLookupMode = false;
    this.applyFilters();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  get paginatedEvents(): Event[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredEvents.slice(start, start + this.itemsPerPage);
  }

  formatStatus(status: EventStatus): string {
    return status
      .split('_')
      .map((token) => token.charAt(0).toUpperCase() + token.slice(1))
      .join(' ');
  }

  getStatusBadgeClass(status: EventStatus): string {
    switch (status) {
      case 'draft':
        return 'status-draft';
      case 'registration_open':
        return 'status-registration-open';
      case 'registration_closed':
        return 'status-registration-closed';
      case 'ongoing':
        return 'status-ongoing';
      case 'completed':
        return 'status-completed';
      case 'cancelled':
        return 'status-cancelled';
      default:
        return 'status-draft';
    }
  }

  private getApiErrorMessage(error: HttpErrorResponse, fallback: string): string {
    const message = error.error?.message;
    return typeof message === 'string' && message.trim().length > 0 ? message : fallback;
  }
}

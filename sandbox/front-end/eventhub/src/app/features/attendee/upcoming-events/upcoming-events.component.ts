import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EventCardComponent } from './components/event-card/event-card.component';
import { EventsDataService } from '../services/events-data.service';
import { NavbarContextService } from '../../../core/services/navbar-context.service';
import { EventItemDisplay, EventDetail, EventStatus } from '../models/attendee.model';

@Component({
  selector: 'app-upcoming-events',
  standalone: true,
  imports: [CommonModule, FormsModule, EventCardComponent],
  templateUrl: './upcoming-events.component.html',
  styleUrls: ['./upcoming-events.component.css']
})
export class UpcomingEventsComponent implements OnInit {
  currentPage = 1;
  itemsPerPage = 6;
  totalPages = 1;
  pages: number[] = [];

  // Filters
  searchTerm = '';
  selectedStatus = 'All';
  selectedOrganizer = 'All';

  // Filter options
  statusOptions: string[] = ['All'];
  organizerOptions: string[] = ['All'];

  allDisplayEvents: EventItemDisplay[] = [];
  filteredEvents: EventItemDisplay[] = [];
  dataLoadingError: string = '';

  constructor(
    private readonly eventsDataService: EventsDataService,
    private readonly navbarContext: NavbarContextService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Update navbar context for events page
    this.navbarContext.setCurrentPage('events');
    this.navbarContext.setEventName(null);
    this.loadEvents();
  }

  private loadEvents(): void {
    this.dataLoadingError = '';

    this.eventsDataService.getEvents().subscribe({
      next: (events: EventDetail[]) => {
        this.allDisplayEvents = events
          .filter((e) => e.status !== 'draft')
          .map((e) => this.eventsDataService.mapToDisplayEvent(e));

        this.statusOptions = ['All', ...new Set(this.allDisplayEvents.map((e) => this.formatStatus(e.status)))];
        this.organizerOptions = ['All', ...new Set(this.allDisplayEvents.map((e) => e.organizerName))];

        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: () => {
        this.dataLoadingError = 'Unable to load events from the API.';
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    const term = this.searchTerm.trim().toLowerCase();
    this.filteredEvents = this.allDisplayEvents.filter((event) => {
      const matchesSearch =
        !term ||
        event.title.toLowerCase().includes(term) ||
        event.venue.toLowerCase().includes(term) ||
        event.organizerName.toLowerCase().includes(term);
      const matchesStatus = this.selectedStatus === 'All' || this.formatStatus(event.status) === this.selectedStatus;
      const matchesOrganizer = this.selectedOrganizer === 'All' || event.organizerName === this.selectedOrganizer;
      return matchesSearch && matchesStatus && matchesOrganizer;
    });
    this.calculatePagination();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.selectedStatus = 'All';
    this.selectedOrganizer = 'All';
    this.applyFilters();
  }

  formatStatus(status: EventStatus): string {
    return status.split('_').map((t) => t.charAt(0).toUpperCase() + t.slice(1)).join(' ');
  }

  private calculatePagination(): void {
    this.totalPages = Math.ceil(this.filteredEvents.length / this.itemsPerPage);
    this.pages = Array.from({ length: this.totalPages }, (_, i) => i + 1);
    this.currentPage = 1;
  }

  get paginatedEvents(): EventItemDisplay[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredEvents.slice(startIndex, startIndex + this.itemsPerPage);
  }

  setPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }
}
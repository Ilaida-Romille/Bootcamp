import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { Event, EventStatus } from './models/event.model';
import { OrganizerEventsApiService, EventInput } from './services/organizer-events-api.service';

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

  // Data
  events: Event[] = [];
  filteredEvents: Event[] = [];

  // Filters
  searchTerm = '';
  selectedOrganizer = 'All';
  selectedStatus = 'All';
  eventIdQuery = '';

  // Filter options
  organizerOptions: string[] = ['All'];
  statusOptions: string[] = ['All'];

  // Pagination
  currentPage = 1;
  itemsPerPage = 8;

  // Loading states
  isLoading = false;
  isLookupLoading = false;
  isFormSubmitting = false;
  isDeleting = false;
  dataLoadingError = '';
  formError = '';

  // Modal state
  isModalOpen = false;
  isEditMode = false;
  editingEventId: string | null = null;

  // Confirmation dialog
  showDeleteConfirm = false;
  deleteConfirmId: string | null = null;
  deleteConfirmName: string = '';

  // Employee lookup mode
  isLookupMode = false;

  // Form data
  formData: EventInput = this.getDefaultFormData();

  ngOnInit(): void {
    this.loadEvents();
  }

  // ============ Loading & Filtering ============
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

  // ============ Modal & Form Management ============
  openAddEventModal(): void {
    this.isEditMode = false;
    this.editingEventId = null;
    this.formData = this.getDefaultFormData();
    this.formError = '';
    this.isModalOpen = true;
  }

  openEditEventModal(event: Event): void {
    this.isEditMode = true;
    this.editingEventId = event.id;
    this.formData = { ...event };
    this.formError = '';
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.isEditMode = false;
    this.editingEventId = null;
    this.formData = this.getDefaultFormData();
    this.formError = '';
  }

  submitForm(): void {
    if (this.isFormValid()) {
      if (this.isEditMode && this.editingEventId) {
        this.updateEvent();
      } else {
        this.createEvent();
      }
    }
  }

  private createEvent(): void {
    this.isFormSubmitting = true;
    this.formError = '';

    this.eventsApi.createEvent(this.formData).subscribe({
      next: (newEvent) => {
        this.events.push(newEvent);
        this.events.sort((a, b) => new Date(a.startDateTime).getTime() - new Date(b.startDateTime).getTime());
        this.applyFilters();
        this.isFormSubmitting = false;
        this.isModalOpen = false;
        this.editingEventId = null;
        this.formData = this.getDefaultFormData();
        this.formError = '';
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.formError = this.getApiErrorMessage(error, 'Failed to create event. Please try again.');
        this.isFormSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  private updateEvent(): void {
    if (!this.editingEventId) return;

    this.isFormSubmitting = true;
    this.formError = '';

    this.eventsApi.updateEvent(this.editingEventId, this.formData).subscribe({
      next: (updatedEvent) => {
        const index = this.events.findIndex((evt) => evt.id === this.editingEventId);
        if (index !== -1) {
          this.events[index] = updatedEvent;
        }
        this.applyFilters();
        this.isFormSubmitting = false;
        this.isModalOpen = false;
        this.isEditMode = false;
        this.editingEventId = null;
        this.formData = this.getDefaultFormData();
        this.formError = '';
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.formError = this.getApiErrorMessage(error, 'Failed to update event. Please try again.');
        this.isFormSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ============ Delete Functionality ============
  openDeleteConfirm(event: Event): void {
    this.deleteConfirmId = event.id;
    this.deleteConfirmName = event.title;
    this.showDeleteConfirm = true;
  }

  closeDeleteConfirm(): void {
    this.showDeleteConfirm = false;
    this.deleteConfirmId = null;
    this.deleteConfirmName = '';
  }

  confirmDelete(): void {
    if (!this.deleteConfirmId) return;

    this.isDeleting = true;
    const eventId = this.deleteConfirmId;

    this.eventsApi.deleteEvent(eventId).subscribe({
      next: () => {
        this.events = this.events.filter((evt) => evt.id !== eventId);
        this.applyFilters();
        this.isDeleting = false;
        this.showDeleteConfirm = false;
        this.deleteConfirmId = null;
        this.deleteConfirmName = '';
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        const errorMsg = this.getApiErrorMessage(error, 'Failed to delete event. Please try again.');
        this.dataLoadingError = errorMsg;
        this.isDeleting = false;
        this.showDeleteConfirm = false;
        this.deleteConfirmId = null;
        this.deleteConfirmName = '';
        this.cdr.detectChanges();
      }
    });
  }

  // ============ View Details ============
  viewEventDetails(event: Event): void {
    this.eventIdQuery = event.id;
    this.lookupById();
  }

  // ============ Helper Methods ============
  private getDefaultFormData(): EventInput {
    return {
      title: '',
      description: '',
      organizerId: '',
      organizerName: '',
      status: 'draft',
      startDateTime: '',
      endDateTime: '',
      registrationOpensAt: '',
      registrationClosesAt: '',
      venue: '',
      bannerImageUrl: '',
      capacity: { maximum: 0, registered: 0 },
      agenda: []
    };
  }

  private isFormValid(): boolean {
    const { title, description, organizerId, organizerName, startDateTime, endDateTime, venue, capacity } = this.formData;

    if (
      !title.trim() ||
      !description.trim() ||
      !organizerId.trim() ||
      !organizerName.trim() ||
      !startDateTime.trim() ||
      !endDateTime.trim() ||
      !venue.trim() ||
      capacity.maximum <= 0
    ) {
      this.formError = 'All required fields must be filled with valid values.';
      this.cdr.detectChanges();
      return false;
    }

    return true;
  }

  private getApiErrorMessage(error: HttpErrorResponse, fallback: string): string {
    const message = error.error?.message;
    return typeof message === 'string' && message.trim().length > 0 ? message : fallback;
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
}

import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { DataTableComponent, ColumnDef, SortEvent } from '../../../shared/components/data-table/data-table.component';
import { DataTableCellDirective } from '../../../shared/components/data-table/data-table-cell.directive';
import { Event, EventStatus } from './models/event.model';
import { OrganizerEventsApiService, EventInput } from './services/organizer-events-api.service';
import {
  validateEventSchedule,
  validateEventCapacity,
  validateAgendaItems,
  getFirstScheduleError
} from '../../../core/validators/event-schedule.validator';

@Component({
  selector: 'app-organizer-events',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent, DataTableComponent, DataTableCellDirective],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css',
})
export class OrganizerEventsComponent implements OnInit {
  private readonly eventsApi = inject(OrganizerEventsApiService);
  private readonly cdr = inject(ChangeDetectorRef);

  // Data
  events: Event[] = [];
  filteredEvents: Event[] = [];

  readonly columns: ColumnDef[] = [
    { key: 'event',        header: 'Event' },
    { key: 'organizer',    header: 'Organizer' },
    { key: 'status',       header: 'Status',              sortable: false },
    { key: 'schedule',     header: 'Schedule' },
    { key: 'registration', header: 'Registration Window' },
    { key: 'venue',        header: 'Venue' },
    { key: 'actions',      header: 'Actions',             sortable: false, cssClass: 'text-center' }
  ];

  sortKey = 'schedule';
  sortDir: 'asc' | 'desc' = 'asc';

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

  // Toast
  showToastNotif = false;
  toastMessage = '';
  toastTitle = '';
  toastType: 'success' | 'danger' = 'success';
  private toastTimer: ReturnType<typeof setTimeout> | null = null;

  // Modal state
  isModalOpen = false;
  isEditMode = false;
  editingEventId: string | null = null;

  // Wizard
  wizardStep = 1;
  readonly totalWizardSteps = 5;

  // Confirmation dialog
  showDeleteConfirm = false;
  deleteConfirmId: string | null = null;
  deleteConfirmName: string = '';

  // Employee lookup mode
  isLookupMode = false;

  // View details modal
  isViewModalOpen = false;
  viewingEvent: Event | null = null;

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
    const getSortVal = (e: Event): string => {
      switch (this.sortKey) {
        case 'event':        return e.title;
        case 'organizer':    return e.organizerName;
        case 'schedule':     return e.startDateTime;
        case 'registration': return e.registrationOpensAt;
        case 'venue':        return e.venue;
        default:             return '';
      }
    };
    const sorted = [...this.filteredEvents].sort((a, b) => {
      const cmp = getSortVal(a).localeCompare(getSortVal(b));
      return this.sortDir === 'asc' ? cmp : -cmp;
    });
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return sorted.slice(start, start + this.itemsPerPage);
  }

  onSortChange(event: SortEvent): void {
    this.sortKey = event.key;
    this.sortDir = event.dir;
    this.currentPage = 1;
  }

  // ============ Modal & Form Management ============
  openAddEventModal(): void {
    this.isEditMode = false;
    this.editingEventId = null;
    this.formData = this.getDefaultFormData();
    this.formError = '';
    this.wizardStep = 1;
    this.isModalOpen = true;
  }

  openEditEventModal(event: Event): void {
    this.isEditMode = true;
    this.editingEventId = event.id;
    this.formData = { ...event };
    this.formError = '';
    this.wizardStep = 1;
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.isEditMode = false;
    this.editingEventId = null;
    this.formData = this.getDefaultFormData();
    this.formError = '';
    this.wizardStep = 1;
  }

  nextStep(): void {
    if (this.isCurrentStepValid() && this.wizardStep < this.totalWizardSteps) {
      this.wizardStep++;
    }
  }

  prevStep(): void {
    if (this.wizardStep > 1) {
      this.formError = '';
      this.wizardStep--;
    }
  }

  private isCurrentStepValid(): boolean {
    const { title, description, organizerId, organizerName, startDateTime, endDateTime, venue, capacity } = this.formData;
    let error = '';

    switch (this.wizardStep) {
      case 1:
        if (!title.trim() || !description.trim()) {
          error = 'Event title and description are required.';
        }
        break;
      case 2:
        if (!organizerId.trim() || !organizerName.trim()) {
          error = 'Organizer ID and organizer name are required.';
        }
        break;
      case 3:
        if (!startDateTime.trim() || !endDateTime.trim()) {
          error = 'Start date/time and end date/time are required.';
        } else if (!this.formData.registrationOpensAt.trim() || !this.formData.registrationClosesAt.trim()) {
          error = 'Registration open and close dates are required.';
        } else {
          const scheduleErrors = validateEventSchedule(
            startDateTime,
            endDateTime,
            this.formData.registrationOpensAt,
            this.formData.registrationClosesAt
          );
          error = getFirstScheduleError(scheduleErrors) ?? '';
        }
        break;
      case 4:
        if (!venue.trim()) {
          error = 'Venue is required.';
        } else {
          error = validateEventCapacity(capacity.maximum) ?? '';
        }
        break;
      case 5:
        error = validateAgendaItems(this.formData.agenda) ?? '';
        break;
    }

    if (error) {
      this.formError = error;
      this.cdr.detectChanges();
      return false;
    }

    this.formError = '';
    return true;
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

    this.eventsApi.createEvent(this.buildEventPayload()).subscribe({
      next: (newEvent) => {
        this.events.push(newEvent);
        this.events.sort((a, b) => new Date(a.startDateTime).getTime() - new Date(b.startDateTime).getTime());
        this.applyFilters();
        this.isFormSubmitting = false;
        this.isModalOpen = false;
        this.editingEventId = null;
        this.formData = this.getDefaultFormData();
        this.formError = '';
        this.triggerToast('Event created successfully.', 'success', 'Event Created');
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

    this.eventsApi.updateEvent(this.editingEventId, this.buildEventPayload()).subscribe({
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
        this.triggerToast('Event updated successfully.', 'success', 'Event Updated');
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
        this.triggerToast('Event deleted successfully.', 'danger', 'Event Deleted');
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

  addAgendaItem(): void {
    this.formData.agenda = [
      ...this.formData.agenda,
      {
        startDateTime: '',
        endDateTime: '',
        title: '',
        description: '',
        location: '',
        speaker: '',
        isBreak: false
      }
    ];
  }

  removeAgendaItem(index: number): void {
    this.formData.agenda = this.formData.agenda.filter((_, i) => i !== index);
  }

  // ============ View Details ============
  viewEventDetails(event: Event): void {
    this.viewingEvent = event;
    this.isViewModalOpen = true;
  }

  closeViewModal(): void {
    this.isViewModalOpen = false;
    this.viewingEvent = null;
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

  // Strips id from agenda items without a server-assigned id to satisfy the API
  private buildEventPayload(): EventInput {
    return {
      ...this.formData,
      agenda: this.formData.agenda.map(({ id, ...rest }) => (id ? { id, ...rest } : rest))
    };
  }

  private isFormValid(): boolean {
    const { title, description, organizerId, organizerName, startDateTime, endDateTime,
            registrationOpensAt, registrationClosesAt, venue, capacity } = this.formData;

    if (
      !title.trim() ||
      !description.trim() ||
      !organizerId.trim() ||
      !organizerName.trim() ||
      !startDateTime.trim() ||
      !endDateTime.trim() ||
      !venue.trim() ||
      capacity.maximum < 1
    ) {
      this.formError = 'All required fields must be filled with valid values.';
      this.cdr.detectChanges();
      return false;
    }

    const scheduleError = getFirstScheduleError(
      validateEventSchedule(startDateTime, endDateTime, registrationOpensAt, registrationClosesAt)
    );
    if (scheduleError) {
      this.formError = scheduleError;
      this.cdr.detectChanges();
      return false;
    }

    const capacityError = validateEventCapacity(capacity.maximum);
    if (capacityError) {
      this.formError = capacityError;
      this.cdr.detectChanges();
      return false;
    }

    const agendaError = validateAgendaItems(this.formData.agenda);
    if (agendaError) {
      this.formError = agendaError;
      this.cdr.detectChanges();
      return false;
    }

    return true;
  }

  private triggerToast(message: string, type: 'success' | 'danger', title: string): void {
    if (this.toastTimer) {
      clearTimeout(this.toastTimer);
    }
    this.toastMessage = message;
    this.toastTitle = title;
    this.toastType = type;
    this.showToastNotif = true;
    this.cdr.detectChanges();
    this.toastTimer = setTimeout(() => {
      this.showToastNotif = false;
      this.cdr.detectChanges();
    }, 4000);
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

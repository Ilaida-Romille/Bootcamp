import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { concatMap, forkJoin, from, map, Observable, of, switchMap, tap, toArray } from 'rxjs';

import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import {
  DataTableComponent,
  ColumnDef,
  SortEvent
} from '../../../shared/components/data-table/data-table.component';
import { DataTableCellDirective } from '../../../shared/components/data-table/data-table-cell.directive';

import {
  Event,
  EventResponse,
  Eventstatus,
  EventType,
  Agenda,
  Track,
  Session
} from './models/event.model';

import {
  OrganizerEventsApiService,
  EventInput,
  EventPatch
} from './services/organizer-events-api.service';

import { AgendaApiService } from './services/organizer-agenda-api-service';

import { NotificationApiService } from './services/notification-api.service';

import {
  validateEventSchedule,
  validateEventCapacity,
  getFirstScheduleError
} from '../../../core/validators/event-schedule.validator';

interface LocalSession {
  id?: number;
  title: string;
  description: string;
  locationOrRoom: string;
}

interface LocalTrack {
  id?: number;
  name: string;
  description: string;
  startTime: string;
  endTime: string;
  sessions: LocalSession[];
}

@Component({
  selector: 'app-organizer-events',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    PaginationComponent,
    DataTableComponent,
    DataTableCellDirective
  ],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css',
})
export class OrganizerEventsComponent implements OnInit {

  private readonly eventsApi = inject(OrganizerEventsApiService);
  private readonly agendaApi = inject(AgendaApiService);
  private readonly notificationApi = inject(NotificationApiService);
  private readonly cdr = inject(ChangeDetectorRef);

  // ============================================================
  // Event Data
  // ============================================================

  events: EventResponse[] = [];
  totalElements = 0;
  currentPage = 0;
  itemsPerPage = 10;
  filteredEvents: Event[] = [];

  agendaFormData = {
    title: '',
    description: ''
  };

  existingAgendaId: number | null = null;
  deletedTrackIds: number[] = [];
  deletedSessionIds: number[] = [];

  localTracks: LocalTrack[] = [];

  readonly columns: ColumnDef[] = [
    { key: 'event', header: 'Event' },
    { key: 'organizer', header: 'Organizer' },
    { key: 'status', header: 'Status', sortable: false },
    { key: 'schedule', header: 'Schedule' },
    { key: 'venue', header: 'Venue' },
    {
      key: 'actions',
      header: 'Actions',
      sortable: false,
      cssClass: 'text-center'
    }
  ];

  sortKey = 'startTime';
  sortDir: 'asc' | 'desc' = 'asc';

  // ============================================================
  // Filters
  // ============================================================

  searchTerm = '';
  selectedEventType: EventType | 'All' = 'All';
  selectedStatus: Eventstatus | 'All' = 'All';

  eventTypeOptions: (EventType | 'All')[] = [
    'All',
    'PHYSICAL',
    'VIRTUAL',
    'HYBRID'
  ];

  statusOptions: (Eventstatus | 'All')[] = [
    'All',
    'DRAFT',
    'PUBLISHED',
    'COMPLETED',
    'CANCELLED'
  ];

  // ============================================================
  // Loading States
  // ============================================================

  isLoading = false;
  isTableLoading = false;
  isLookupLoading = false;
  isFormSubmitting = false;
  isDeleting = false;
  isAgendaLoading = false;
  isAgendaSubmitting = false;

  dataLoadingError = '';
  formError = '';
  agendaError = '';

  // ============================================================
  // Toast
  // ============================================================

  showToastNotif = false;
  toastMessage = '';
  toastTitle = '';
  toastType: 'success' | 'danger' = 'success';

  private toastTimer: ReturnType<typeof setTimeout> | null = null;

  // ============================================================
  // Event Create/Edit Modal
  // ============================================================

  isModalOpen = false;
  isEditMode = false;
  editingEventId: number | null = null;

  // ============================================================
  // Event Creation Wizard
  // ============================================================

  wizardStep = 1;

  /**
   * Event creation is intentionally limited to four steps.
   *
   * Agenda management happens after the Event has been created
   * because Agenda, Track, and Session are now independent entities.
   */
  readonly totalWizardSteps = 4;

  // ============================================================
  // Delete Confirmation
  // ============================================================

  showDeleteConfirm = false;
  deleteConfirmId: number | null = null;
  deleteConfirmName = '';

  // ============================================================
  // View Event Details
  // ============================================================

  isViewModalOpen = false;
  viewingEvent: Event | null = null;

  /**
   * Agendas are NOT read from viewingEvent.agenda anymore.
   *
   * They are loaded independently using AgendaApiService.
   */
  viewingEventAgendas: Agenda[] = [];

  // ============================================================
  // Notify Registrants
  // ============================================================

  isNotifyModalOpen = false;
  isNotifying = false;
  notifyError = '';
  notifySubject = '';
  notifyMessageBody = '';

  // ============================================================
  // Post-Creation Agenda Prompt
  // ============================================================

  showAgendaPrompt = false;

  /**
   * Stores the event that was just created so the user can
   * immediately continue to agenda management.
   */
  newlyCreatedEvent: Event | null = null;

  // ============================================================
  // Agenda Management
  // ============================================================

  isAgendaModalOpen = false;

  agendaEvent: EventResponse | Event | null = null;

  agendas: Agenda[] = [];

  selectedAgenda: Agenda | null = null;

  tracks: Track[] = [];

  selectedTrack: Track | null = null;

  sessions: Session[] = [];

  // Agenda form
  agendaForm = {
    agendaDate: '',
    title: '',
    description: ''
  };

  // Track form
  trackForm = {
    name: '',
    description: '',
    displayOrder: 1
  };

  // Session form
  sessionForm = {
    title: '',
    description: '',
    startTime: '',
    endTime: '',
    locationOrRoom: '',
    speakerIds: ''
  };

  // ============================================================
  // Event Form Data
  // ============================================================

  formData: EventInput = this.getDefaultFormData();

  // ============================================================
  // Lifecycle
  // ============================================================

  ngOnInit(): void {
    this.loadEvents();
  }

  // ============================================================
  // Loading & Filtering
  // ============================================================

  loadEvents(): void {
    this.isTableLoading = true;
    this.dataLoadingError = '';

    const statusParam = this.selectedStatus === 'All'
      ? undefined
      : this.selectedStatus.toUpperCase() as Eventstatus;

    const eventTypeParam = this.selectedEventType === 'All'
      ? undefined
      : this.selectedEventType;

    const sortParam = `${this.sortKey},${this.sortDir}`;

    this.eventsApi.getEvents(
      this.currentPage,
      this.itemsPerPage,
      sortParam,
      statusParam,
      eventTypeParam,
      this.searchTerm.trim() === ''
        ? undefined
        : this.searchTerm.trim()
    ).subscribe({
      next: (response) => {

        this.filteredEvents = response.content.map(
          (item: EventResponse) => ({
            id: item.id,
            title: item.title,
            description: item.description,

            organizerId: String(item.organizationId),
            organizerName: item.organizationName,

            status: item.status
              ? item.status
              : 'DRAFT',

            startTime: item.startTime,
            endTime: item.endTime,

            registrationOpensAt: item.registrationStartTime,
            registrationClosesAt: item.registrationEndTime,

            locationAddress:
              item.locationAddress ||
              item.virtualMeetingUrl ||
              'N/A',

            bannerImageUrl: item.bannerImageUrl,

            capacity: {
              maximum: item.maxCapacity || 0,
              registered: 0
            }
          })
        );

        this.totalElements = response.totalElements;
      },

      error: () => {
        this.filteredEvents = [];
        this.totalElements = 0;
        this.dataLoadingError = 'Unable to load events.';
      },

      complete: () => {
        this.isTableLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadEvents();
  }

  clearLookupAndFilters(): void {
    this.searchTerm = '';
    this.selectedEventType = 'All';
    this.selectedStatus = 'All';
    this.currentPage = 0;

    this.loadEvents();
  }

  onPageChange(page: number): void {
    this.currentPage = page - 1;
    this.loadEvents();
  }

  get paginatedEvents(): Event[] {
    return this.filteredEvents;
  }

  onSortChange(event: SortEvent): void {
    this.sortKey = event.key;
    this.sortDir = event.dir;
    this.currentPage = 0;

    this.loadEvents();
  }

  // ============================================================
  // Event Modal & Form Management
  // ============================================================

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

    // Pre-populate all available fields from event model
    this.formData = {
      title: event.title || '',
      description: event.description || '',
      bannerImageUrl: event.bannerImageUrl || '',
      eventType: 'PHYSICAL',
      isPrivate: false,
      cateringProvided: false,
      startTime: event.startTime ? event.startTime.substring(0, 16) : '',
      endTime: event.endTime ? event.endTime.substring(0, 16) : '',
      registrationOpensAt: event.registrationOpensAt ? event.registrationOpensAt.substring(0, 16) : '',
      registrationClosesAt: event.registrationClosesAt ? event.registrationClosesAt.substring(0, 16) : '',
      locationAddress: event.locationAddress !== 'N/A' ? event.locationAddress : '',
      virtualMeetingUrl: '',
      maxCapacity: event.capacity?.maximum || 0
    };

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

  // ============================================================
  // Wizard Navigation
  // ============================================================

  nextStep(): void {
    if (
      this.isCurrentStepValid() &&
      this.wizardStep < this.totalWizardSteps
    ) {
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

    const {
      title,
      description,
      eventType,
      startTime,
      endTime,
      registrationOpensAt,
      registrationClosesAt,
      maxCapacity,
      locationAddress,
      virtualMeetingUrl
    } = this.formData;

    let error = '';

    switch (this.wizardStep) {

      case 1:
        if (
          !title.trim() ||
          !(description ?? '').trim()
        ) {
          error = 'Event title and description are required.';
        }
        break;

      case 2:
        if (!eventType) {
          error = 'Event type is required.';
        }
        break;

      case 3:
        if (
          !startTime.trim() ||
          !endTime.trim()
        ) {
          error =
            'Start date/time and end date/time are required.';
        } else {

          const scheduleErrors =
            validateEventSchedule(
              startTime,
              endTime,
              registrationOpensAt,
              registrationClosesAt
            );

          error =
            getFirstScheduleError(scheduleErrors) ?? '';
        }
        break;

      case 4:

        if (
          (eventType === 'PHYSICAL' ||
            eventType === 'HYBRID') &&
          !locationAddress?.trim()
        ) {
          error =
            'Location address is required for physical/hybrid events.';
        }

        else if (
          (eventType === 'VIRTUAL' ||
            eventType === 'HYBRID') &&
          !virtualMeetingUrl?.trim()
        ) {
          error =
            'Virtual meeting URL is required for virtual/hybrid events.';
        }

        else {
          error =
            validateEventCapacity(maxCapacity ?? 0) ?? '';
        }

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

    if (!this.isFormValid()) {
      return;
    }

    if (
      this.isEditMode &&
      this.editingEventId
    ) {
      this.updateEvent();
    } else {
      this.createEvent();
    }
  }

  // ============================================================
  // Event Creation
  // ============================================================

  private createEvent(): void {

    this.isFormSubmitting = true;
    this.eventsApi.createEvent(this.formData).subscribe({
      next: (createdEvent: Event) => {
        this.newlyCreatedEvent = createdEvent; // Store the event ID for the Agenda API
        this.isFormSubmitting = false;
        this.isModalOpen = false;
        this.showAgendaPrompt = true; // Trigger the prompt
        this.loadEvents();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isFormSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ============================================================
  // Event Update
  // ============================================================

  private updateEvent(): void {

    if (!this.editingEventId) return;

    this.isFormSubmitting = true;
    this.formError = '';

    // Send only defined fields for partial update
    const patchPayload: EventPatch = {
      title: this.formData.title,
      description: this.formData.description,
      bannerImageUrl: this.formData.bannerImageUrl,
      eventType: this.formData.eventType,
      locationAddress: this.formData.locationAddress,
      virtualMeetingUrl: this.formData.virtualMeetingUrl,
      startTime: this.formData.startTime,
      endTime: this.formData.endTime,
      isPrivate: this.formData.isPrivate,
      cateringProvided: this.formData.cateringProvided,
      maxCapacity: this.formData.maxCapacity
    };

    this.eventsApi.patchEvent(this.editingEventId, patchPayload).subscribe({
      next: () => {
        this.loadEvents();
        this.isFormSubmitting = false;
        this.isModalOpen = false;
        this.isEditMode = false;
        this.editingEventId = null;
        this.formData = this.getDefaultFormData();
        this.triggerToast('Event updated successfully.', 'success', 'Event Updated');
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.formError = this.getApiErrorMessage(error, 'Failed to update event.');
        this.isFormSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ============================================================
  // Post-Creation Agenda Prompt
  // ============================================================

  closeAgendaPrompt(): void {
    this.showAgendaPrompt = false;
    this.newlyCreatedEvent = null;
  }

  openAgendaBuilder(): void {

    if (!this.newlyCreatedEvent) return;

    const event = this.newlyCreatedEvent;
    this.showAgendaPrompt = false;
    this.openAgendaManagement(event); // Open the agenda wizard
    this.newlyCreatedEvent = null;
  }

  // ============================================================
  // Agenda Management
  // ============================================================

  openAgendaManagement(event: EventResponse | Event): void {
    this.agendaEvent = event;
    this.isAgendaModalOpen = true;
    this.agendaError = '';
    this.existingAgendaId = null;
    this.localTracks = [];
    this.deletedTrackIds = [];
    this.deletedSessionIds = [];

    this.agendaApi.getAgendasByEventId(Number(event.id)).subscribe({
      next: (agendas) => {
        if (agendas && agendas.length > 0) {
          const firstAgenda = agendas[0];
          this.existingAgendaId = firstAgenda.id;
          this.agendaFormData = {
            title: firstAgenda.title,
            description: firstAgenda.description || ''
          };

          if (firstAgenda.tracks) {
            this.localTracks = firstAgenda.tracks.map(t => {
              // Grab start/end time from first session if available
              const firstSession = t.sessions?.[0];
              return {
                id: t.id,
                name: t.name,
                description: t.description || '',
                startTime: firstSession?.startTime ? firstSession.startTime.substring(0, 16) : '',
                endTime: firstSession?.endTime ? firstSession.endTime.substring(0, 16) : '',
                sessions: (t.sessions || []).map(s => ({
                  id: s.id,
                  title: s.title,
                  description: '',
                  locationOrRoom: s.locationOrRoom || ''
                }))
              };
            });
          }
        } else {
          this.agendaFormData = { title: '', description: '' };
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.agendaFormData = { title: '', description: '' };
      }
    });
  }

  addTrackToAgenda(): void {
    this.localTracks.push({
      name: '',
      description: '',
      startTime: '',
      endTime: '',
      sessions: []
    });
  }

  removeTrackFromAgenda(index: number): void {
    const track = this.localTracks[index];
    if (track.id) {
      this.deletedTrackIds.push(track.id);
    }
    this.localTracks.splice(index, 1);
  }

  addSessionToTrack(trackIndex: number): void {
    const parentTrack = this.localTracks[trackIndex];
    this.localTracks[trackIndex].sessions.push({
      title: '',
      description: '',
      locationOrRoom: ''
    });
  }



  closeAgendaManagement(): void {

    this.isAgendaModalOpen = false;

    this.agendaEvent = null;

    this.agendas = [];

    this.selectedAgenda = null;
    this.selectedTrack = null;

    this.tracks = [];
    this.sessions = [];

    this.agendaError = '';

    this.resetAgendaForm();
    this.resetTrackForm();
    this.resetSessionForm();
  }

  loadAgendas(eventId: number): void {

    this.isAgendaLoading = true;
    this.agendaError = '';

    this.agendaApi
      .getAgendasByEventId(eventId)
      .subscribe({

        next: (agendas) => {

          this.agendas = agendas;

          this.viewingEventAgendas =
            agendas;

          this.isAgendaLoading = false;

          this.cdr.detectChanges();
        },

        error: (error: HttpErrorResponse) => {

          this.agendas = [];
          this.viewingEventAgendas = [];

          this.agendaError =
            this.getApiErrorMessage(
              error,
              'Unable to load agendas.'
            );

          this.isAgendaLoading = false;

          this.cdr.detectChanges();
        }
      });
  }

  createAgenda(): void {

    if (!this.agendaEvent) {
      return;
    }

    if (!this.agendaForm.agendaDate.trim()) {
      this.agendaError =
        'Agenda date is required.';

      return;
    }

    if (!this.agendaForm.title.trim()) {
      this.agendaError =
        'Agenda title is required.';

      return;
    }

    this.isAgendaSubmitting = true;
    this.agendaError = '';

    this.agendaApi
      .createAgenda(
        this.agendaEvent.id,
        {
          agendaDate:
            this.agendaForm.agendaDate,

          title:
            this.agendaForm.title.trim(),

          description:
            this.agendaForm.description.trim() ||
            undefined
        }
      )
      .subscribe({

        next: (agenda) => {

          this.agendas = [
            ...this.agendas,
            agenda
          ];

          this.selectedAgenda = agenda;

          this.resetAgendaForm();

          this.isAgendaSubmitting = false;

          this.triggerToast(
            'Agenda created successfully.',
            'success',
            'Agenda Created'
          );

          this.cdr.detectChanges();
        },

        error: (error: HttpErrorResponse) => {

          this.agendaError =
            this.getApiErrorMessage(
              error,
              'Failed to create agenda.'
            );

          this.isAgendaSubmitting = false;

          this.cdr.detectChanges();
        }
      });
  }

  selectAgenda(agenda: Agenda): void {

    this.selectedAgenda = agenda;

    this.selectedTrack = null;

    this.tracks = [];
    this.sessions = [];

    this.resetTrackForm();
    this.resetSessionForm();

    this.agendaError = '';
  }

  // createTrack(): void {

  //   if (!this.selectedAgenda) {
  //     this.agendaError =
  //       'Select an agenda first.';

  //     return;
  //   }

  //   if (!this.trackForm.name.trim()) {
  //     this.agendaError =
  //       'Track name is required.';

  //     return;
  //   }

  //   this.isAgendaSubmitting = true;
  //   this.agendaError = '';

  //   this.agendaApi
  //     .createTrack(
  //       this.selectedAgenda.id,
  //       {
  //         name:
  //           this.trackForm.name.trim(),

  //         description:
  //           this.trackForm.description.trim() ||
  //           undefined,

  //         displayOrder:
  //           this.trackForm.displayOrder
  //       }
  //     )
  //     .subscribe({

  //       next: (track) => {

  //         this.tracks = [
  //           ...this.tracks,
  //           track
  //         ];

  //         this.selectedTrack = track;

  //         this.resetTrackForm();

  //         this.isAgendaSubmitting = false;

  //         this.triggerToast(
  //           'Track created successfully.',
  //           'success',
  //           'Track Created'
  //         );

  //         this.cdr.detectChanges();
  //       },

  //       error: (error: HttpErrorResponse) => {

  //         this.agendaError =
  //           this.getApiErrorMessage(
  //             error,
  //             'Failed to create track.'
  //           );

  //         this.isAgendaSubmitting = false;

  //         this.cdr.detectChanges();
  //       }
  //     });
  // }

  selectTrack(track: Track): void {

    this.selectedTrack = track;

    this.sessions = [];

    this.resetSessionForm();

    this.agendaError = '';
  }

  removeSessionFromTrack(trackIndex: number, sessionIndex: number): void {
    const track = this.localTracks[trackIndex];
    const session = track?.sessions?.[sessionIndex];

    if (!track || !session) {
      return;
    }

    // If this session already exists in the backend,
    // remember it for deletion when Save Agenda is clicked.
    if (session.id) {
      this.deletedSessionIds.push(session.id);
    }

    // Remove only from the local modal state.
    track.sessions.splice(sessionIndex, 1);
  }

  deleteCurrentAgenda(): void {
    if (!this.existingAgendaId) return;

    this.isAgendaSubmitting = true;
    this.agendaApi.deleteAgenda(this.existingAgendaId).subscribe({
      next: () => {
        this.isAgendaSubmitting = false;
        this.triggerToast('Agenda deleted successfully.', 'danger', 'Agenda Deleted');
        this.closeAgendaManagement();
        if (this.agendaEvent) {
          this.loadAgendas(Number(this.agendaEvent.id));
        }
        this.cdr.detectChanges();
      },
      error: (err: HttpErrorResponse) => {
        this.isAgendaSubmitting = false;
        this.agendaError = this.getApiErrorMessage(err, 'Failed to delete agenda.');
        this.cdr.detectChanges();
      }
    });
  }

  // createSession(): void {

  //   if (!this.selectedTrack) {
  //     this.agendaError =
  //       'Select a track first.';

  //     return;
  //   }

  //   if (!this.sessionForm.title.trim()) {
  //     this.agendaError =
  //       'Session title is required.';

  //     return;
  //   }

  //   if (
  //     !this.sessionForm.startTime ||
  //     !this.sessionForm.endTime
  //   ) {
  //     this.agendaError =
  //       'Session start and end time are required.';

  //     return;
  //   }

  //   const speakerIds =
  //     this.sessionForm.speakerIds
  //       .split(',')
  //       .map(id => Number(id.trim()))
  //       .filter(id => !Number.isNaN(id));

  //   this.isAgendaSubmitting = true;
  //   this.agendaError = '';

  //   this.agendaApi
  //     .createSession(
  //       this.selectedTrack.id,
  //       {
  //         title:
  //           this.sessionForm.title.trim(),

  //         description:
  //           this.sessionForm.description.trim() ||
  //           undefined,

  //         startTime:
  //           this.sessionForm.startTime,

  //         endTime:
  //           this.sessionForm.endTime,

  //         locationOrRoom:
  //           this.sessionForm.locationOrRoom.trim() ||
  //           undefined,

  //         speakerIds:
  //           speakerIds.length > 0
  //             ? speakerIds
  //             : undefined
  //       }
  //     )
  //     .subscribe({

  //       next: (session) => {

  //         this.sessions = [
  //           ...this.sessions,
  //           session
  //         ];

  //         this.resetSessionForm();

  //         this.isAgendaSubmitting = false;

  //         this.triggerToast(
  //           'Session created successfully.',
  //           'success',
  //           'Session Created'
  //         );

  //         this.cdr.detectChanges();
  //       },

  //       error: (error: HttpErrorResponse) => {

  //         this.agendaError =
  //           this.getApiErrorMessage(
  //             error,
  //             'Failed to create session.'
  //           );

  //         this.isAgendaSubmitting = false;

  //         this.cdr.detectChanges();
  //       }
  //     });
  // }

  //Agenda Builder


  buildCompleteScheduleItem(): void {
    if (!this.agendaEvent) return;

    if (!this.agendaFormData.title.trim()) {
      this.agendaError = 'Agenda title is required.';
      return;
    }

    // Validate dates on tracks
    for (const [tIdx, track] of this.localTracks.entries()) {
      if (!track.startTime || !track.endTime) {
        this.agendaError = `Track ${tIdx + 1} requires valid start and end times.`;
        return;
      }
    }

    this.isAgendaSubmitting = true;
    this.agendaError = '';
    const eventId = Number(this.agendaEvent.id);

    const autoAgendaDate = this.agendaEvent.startTime
      ? this.agendaEvent.startTime.split('T')[0]
      : new Date().toISOString().split('T')[0];

    const sessionDeletions$: Observable<void> =
      this.deletedSessionIds.length > 0
        ? from(this.deletedSessionIds).pipe(
          concatMap(sessionId =>
            this.agendaApi.deleteSession(sessionId)
          ),
          toArray(),
          map(() => void 0)
        )
        : of(undefined);

    const trackDeletions$: Observable<void> = this.deletedTrackIds.length > 0
      ? from(this.deletedTrackIds).pipe(
        concatMap(id => this.agendaApi.deleteTrack(id)),
        toArray(),
        map(() => void 0)
      )
      : of(undefined);

    sessionDeletions$.pipe(
      concatMap(() => trackDeletions$),

      concatMap(() => {
        const payload = {
          agendaDate: autoAgendaDate,
          title: this.agendaFormData.title.trim(),
          description: this.agendaFormData.description.trim() || undefined
        };

        return this.existingAgendaId
          ? this.agendaApi.updateAgenda(this.existingAgendaId, payload)
          : this.agendaApi.createAgenda(eventId, payload);
      }),
      concatMap((agenda) => {
        if (this.localTracks.length === 0) return of(undefined);

        return from(this.localTracks).pipe(
          concatMap((track, trackIdx) => {
            const trackPayload = {
              name: track.name || `Track ${trackIdx + 1}`,
              description: track.description || undefined,
              displayOrder: trackIdx + 1
            };

            const track$ = track.id
              ? this.agendaApi.updateTrack(track.id, trackPayload)
              : this.agendaApi.createTrack(agenda.id, trackPayload);

            console.log('TRACK REQUEST CREATED:', track);
            console.log('TRACK OBSERVABLE:', track$);

            return track$.pipe(
              tap({
                next: (result) => {
                  console.log('TRACK RESPONSE:', result);
                },
                error: (err) => {
                  console.error('TRACK REQUEST ERROR:', err);
                },
                complete: () => {
                  console.log('TRACK REQUEST COMPLETE');
                }
              }),

              concatMap((createdTrack) => {
                console.log('TRACK BEING SAVED:', track);
                console.log('CREATED TRACK:', createdTrack);
                console.log('SESSIONS:', track.sessions);
                console.log('SESSION COUNT:', track.sessions?.length);

                if (!track.sessions || track.sessions.length === 0) {
                  return of(createdTrack);
                }

                const sessionRequests$ = track.sessions.map((sess) => {
                  const sessionPayload = {
                    trackId: createdTrack.id,
                    title: sess.title?.trim() || 'Untitled Session',
                    description: sess.description?.trim() || undefined,
                    startTime: track.startTime
                      ? new Date(track.startTime).toISOString()
                      : new Date().toISOString(),
                    endTime: track.endTime
                      ? new Date(track.endTime).toISOString()
                      : new Date().toISOString(),
                    locationOrRoom: sess.locationOrRoom?.trim() || undefined
                  };

                  console.log('SESSION PAYLOAD:', sessionPayload);
                  console.log('SESSION ID:', sess.id);

                  return sess.id
                    ? this.agendaApi.updateSession(sess.id, sessionPayload)
                    : this.agendaApi.createSession(
                      createdTrack.id,
                      sessionPayload
                    );
                });

                return forkJoin(sessionRequests$).pipe(
                  tap((responses) => {
                    console.log('SESSION RESPONSES:', responses);
                  }),
                  map(() => createdTrack)
                );
              })
            );
          }),
          toArray()
        );
      })
    ).subscribe({
      next: () => {
        this.isAgendaSubmitting = false;
        this.triggerToast('Agenda saved successfully!', 'success', 'Agenda Saved');
        this.loadAgendas(eventId);
        this.closeAgendaManagement();
        this.cdr.detectChanges();
      },
      error: (err: HttpErrorResponse) => {
        this.isAgendaSubmitting = false;
        this.agendaError = this.getApiErrorMessage(err, 'Failed to save agenda schedule.');
        this.cdr.detectChanges();
      }
    });
  }

  // ============================================================
  // Agenda Form Helpers
  // ============================================================

  private resetAgendaForm(): void {

    this.agendaForm = {
      agendaDate: '',
      title: '',
      description: ''
    };
  }

  private resetTrackForm(): void {

    this.trackForm = {
      name: '',
      description: '',
      displayOrder: 1
    };
  }

  private resetSessionForm(): void {

    this.sessionForm = {
      title: '',
      description: '',
      startTime: '',
      endTime: '',
      locationOrRoom: '',
      speakerIds: ''
    };
  }

  // ============================================================
  // Delete Functionality
  // ============================================================

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
        this.loadEvents();
        this.isDeleting = false;
        this.showDeleteConfirm = false;
        this.deleteConfirmId = null;
        this.deleteConfirmName = '';
        this.triggerToast('Event deleted successfully.', 'danger', 'Event Deleted');
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.dataLoadingError = this.getApiErrorMessage(error, 'Failed to delete event.');
        this.isDeleting = false;
        this.showDeleteConfirm = false;
        this.deleteConfirmId = null;
        this.deleteConfirmName = '';
        this.cdr.detectChanges();
      }
    });
  }

  // ============================================================
  // View Details
  // ============================================================

  viewEventDetails(event: Event): void {

    this.viewingEvent = event;

    this.viewingEventAgendas = [];

    this.isViewModalOpen = true;

    /**
     * Fetch agendas independently from the Event.
     */
    this.loadAgendas(Number(event.id));
  }

  closeViewModal(): void {

    this.isViewModalOpen = false;

    this.viewingEvent = null;

    this.viewingEventAgendas = [];
  }

  // ============================================================
  // Notify Registrants
  // ============================================================

  openNotifyModal(): void {

    if (!this.viewingEvent) return;

    this.notifyError = '';
    this.notifySubject = `Reminder: ${this.viewingEvent.title}`;
    this.notifyMessageBody = '';

    this.isNotifyModalOpen = true;
  }

  closeNotifyModal(): void {

    this.isNotifyModalOpen = false;

    this.notifyError = '';
    this.notifySubject = '';
    this.notifyMessageBody = '';
  }

  sendNotifications(): void {

    if (!this.viewingEvent) return;

    if (!this.notifySubject.trim()) {
      this.notifyError = 'Subject is required.';
      return;
    }

    if (!this.notifyMessageBody.trim()) {
      this.notifyError = 'Message body is required.';
      return;
    }

    this.isNotifying = true;
    this.notifyError = '';

    const eventId = Number(this.viewingEvent.id);

    this.notificationApi.sendNotificationEmail({
      eventId,
      recipientUserId: null,
      subject: this.notifySubject.trim(),
      messageBody: this.notifyMessageBody.trim()
    }).subscribe({
      next: () => {
        this.isNotifying = false;
        this.closeNotifyModal();
        this.triggerToast('Notifications sent successfully to all registered attendees.', 'success', 'Registrants Notified');
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.isNotifying = false;
        this.notifyError = this.getApiErrorMessage(error, 'Unable to send notifications.');
        this.cdr.detectChanges();
      }
    });
  }

  // ============================================================
  // Default Event Form
  // ============================================================

  private getDefaultFormData(): EventInput {

    return {
      title: '',
      description: '',
      bannerImageUrl: '',

      eventType: 'PHYSICAL',

      isPrivate: false,
      cateringProvided: false,

      startTime: '',
      endTime: '',

      registrationOpensAt: '',
      registrationClosesAt: '',

      locationAddress: '',
      virtualMeetingUrl: '',

      maxCapacity: 0
    };
  }

  // ============================================================
  // Event Form Validation
  // ============================================================

  private isFormValid(): boolean {

    const {
      title,
      description,
      eventType,
      startTime,
      endTime,
      registrationOpensAt,
      registrationClosesAt,
      maxCapacity,
      locationAddress,
      virtualMeetingUrl
    } = this.formData;

    if (
      !title.trim() ||
      !(description ?? '').trim() ||
      !eventType ||
      !startTime.trim() ||
      !endTime.trim() ||
      (maxCapacity ?? 0) < 1
    ) {

      this.formError =
        'All required fields must be filled with valid values.';

      this.cdr.detectChanges();

      return false;
    }

    if (
      (eventType === 'PHYSICAL' ||
        eventType === 'HYBRID') &&
      !locationAddress?.trim()
    ) {

      this.formError =
        'Location address is required.';

      this.cdr.detectChanges();

      return false;
    }

    if (
      (eventType === 'VIRTUAL' ||
        eventType === 'HYBRID') &&
      !virtualMeetingUrl?.trim()
    ) {

      this.formError =
        'Virtual meeting URL is required.';

      this.cdr.detectChanges();

      return false;
    }

    const scheduleError =
      getFirstScheduleError(
        validateEventSchedule(
          startTime,
          endTime,
          registrationOpensAt,
          registrationClosesAt
        )
      );

    if (scheduleError) {

      this.formError =
        scheduleError;

      this.cdr.detectChanges();

      return false;
    }

    const capacityError =
      validateEventCapacity(
        maxCapacity ?? 0
      );

    if (capacityError) {

      this.formError =
        capacityError;

      this.cdr.detectChanges();

      return false;
    }

    return true;
  }

  // ============================================================
  // Toast
  // ============================================================

  private triggerToast(
    message: string,
    type: 'success' | 'danger',
    title: string
  ): void {

    if (this.toastTimer) {
      clearTimeout(this.toastTimer);
    }

    this.toastMessage = message;
    this.toastTitle = title;
    this.toastType = type;

    this.showToastNotif = true;

    this.cdr.detectChanges();

    this.toastTimer =
      setTimeout(() => {

        this.showToastNotif = false;

        this.cdr.detectChanges();

      }, 4000);
  }

  // ============================================================
  // API Error Helper
  // ============================================================

  private getApiErrorMessage(
    error: HttpErrorResponse,
    fallback: string
  ): string {

    const message =
      error.error?.message;

    return typeof message === 'string' &&
      message.trim().length > 0
      ? message
      : fallback;
  }

  // ============================================================
  // Display Helpers
  // ============================================================

  formatStatus(
    status: Eventstatus | string
  ): string {

    if (!status) {
      return 'Draft';
    }

    return (
      status.charAt(0).toUpperCase() +
      status.slice(1).toLowerCase()
    );
  }

  getStatusBadgeClass(
    status: Eventstatus | string
  ): string {

    switch (status?.toUpperCase()) {

      case 'DRAFT':
        return 'status-draft';

      case 'PUBLISHED':
        return 'status-registration-open';

      case 'COMPLETED':
        return 'status-completed';

      case 'CANCELLED':
        return 'status-cancelled';

      default:
        return 'status-draft';
    }
  }
}
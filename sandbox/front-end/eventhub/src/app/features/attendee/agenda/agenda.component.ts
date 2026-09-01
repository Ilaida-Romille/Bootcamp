import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { AgendaTimelineComponent } from './components/agenda-timeline/agenda-timeline.component';
import { AttendeeListWidgetComponent } from './components/attendee-list-widget/attendee-list-widget.component';
import { VenueMapWidgetComponent } from './components/venue-map-widget/venue-map-widget.component';
import { ContactOrganizerWidgetComponent } from './components/contact-organizer-widget/contact-organizer-widget.component';
import { RegistrationService } from '../services/registration.service';
import { EventsDataService, AgendaResponseDto } from '../services/events-data.service';
import { NavbarContextService } from '../../../core/services/navbar-context.service';
import { RegisteredAttendee, EventDetail } from '../models/attendee.model';

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [
    CommonModule,
    AgendaTimelineComponent,
    AttendeeListWidgetComponent,
    VenueMapWidgetComponent,
    ContactOrganizerWidgetComponent
  ],
  templateUrl: './agenda.component.html',
  styleUrls: ['./agenda.component.css']
})
export class AgendaComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly registrationService = inject(RegistrationService);
  private readonly eventsDataService = inject(EventsDataService);
  private readonly navbarContext = inject(NavbarContextService);
  private readonly cdr = inject(ChangeDetectorRef);

  eventId: string | null = null;
  eventDetail: EventDetail | null = null;
  agendas: AgendaResponseDto[] = [];
  attendees: RegisteredAttendee[] = [];
  loadingError: string = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('eventId');
      if (id && id !== this.eventId) {
        this.eventId = id;
        this.loadEventDetails();
        this.loadAttendees();
      }
    });
  }

  private loadEventDetails(): void {
    if (!this.eventId) return;

    const eventId = Number(this.eventId);

    forkJoin({
      event: this.eventsDataService.getDiscoverableEvent(eventId),
      agendas: this.eventsDataService.getAgendasByEvent(eventId)
    }).subscribe({
      next: ({ event, agendas }) => {
        this.eventDetail = this.eventsDataService.mapToDisplayEvent(event);
        this.agendas = agendas;
        this.navbarContext.setEventName(event.title);
        this.cdr.detectChanges();
      },
      error: () => {
        this.eventDetail = null;
        this.agendas = [];
        this.loadingError = 'Unable to load event details.';
        this.cdr.detectChanges();
      }
    });
  }

  private loadAttendees(): void {
    if (!this.eventId) return;

    this.registrationService.getRegistrationsByEventId(this.eventId).subscribe({
      next: (attendees) => {
        this.attendees = attendees;
        this.cdr.detectChanges();
      },
      error: () => {
        this.attendees = [];
        this.loadingError = 'Unable to load attendees.';
        this.cdr.detectChanges();
      }
    });
  }
}
import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { AgendaTimelineComponent, AgendaItem } from './components/agenda-timeline/agenda-timeline.component';
import { AttendeeListWidgetComponent } from './components/attendee-list-widget/attendee-list-widget.component';
import { VenueMapWidgetComponent } from './components/venue-map-widget/venue-map-widget.component';
import { ContactOrganizerWidgetComponent } from './components/contact-organizer-widget/contact-organizer-widget.component';
import { RegistrationService } from '../services/registration.service';
import { EventsDataService } from '../services/events-data.service';
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
  agendaItems: AgendaItem[] = [
    { time: '9:00 AM', details: 'Registration & Welcome Coffee' },
    { time: '9:30 AM', details: 'Opening Keynote — Main Hall' },
    { time: '10:45 AM', details: 'Breakout: Cloud Architecture' },
    { time: '1:00 PM', details: 'Lunch & Networking' },
    { time: '2:00 PM', details: 'Panel: The Future of AI' },
    { time: '4:00 PM', details: 'Closing Remarks' }
  ];

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

    this.eventsDataService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.eventDetail = event;
        // Update navbar with event name
        this.navbarContext.setEventName(event.title);
        this.cdr.detectChanges();
      },
      error: () => {
        this.eventDetail = null;
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
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

// Import Child Components
import { AgendaTimelineComponent, AgendaItem } from './components/agenda-timeline/agenda-timeline.component';
import { AttendeeListWidgetComponent, Attendee } from './components/attendee-list-widget/attendee-list-widget.component';
import { VenueMapWidgetComponent } from './components/venue-map-widget/venue-map-widget.component';
import { ContactOrganizerWidgetComponent } from './components/contact-organizer-widget/contact-organizer-widget.component';

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
  agendaItems: AgendaItem[] = [
    { time: '9:00 AM', details: 'Registration & Welcome Coffee' },
    { time: '9:30 AM', details: 'Opening Keynote — Main Hall' },
    { time: '10:45 AM', details: 'Breakout: Cloud Architecture' },
    { time: '1:00 PM', details: 'Lunch & Networking' },
    { time: '2:00 PM', details: 'Panel: The Future of AI' },
    { time: '4:00 PM', details: 'Closing Remarks' }
  ];

  attendees: Attendee[] = [];

  ngOnInit(): void {
    this.loadRegistrants();
  }

  private loadRegistrants(): void {
    const activeEventId = localStorage.getItem('eventHub_currentEventId') || '1';
    const registrantsRaw = localStorage.getItem('eventHub_registrants');

    if (registrantsRaw) {
      const allRegistrants: Attendee[] = JSON.parse(registrantsRaw);
      this.attendees = allRegistrants.filter(
        (attendee: any) => attendee.eventId === activeEventId
      );
    }
  }
}
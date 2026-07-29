import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventCardComponent } from './components/event-card/event-card.component';
import { EventsDataService } from '../services/events-data.service';
import { NavbarContextService } from '../../../core/services/navbar-context.service';
import { EventItemDisplay, EventDetail } from '../models/attendee.model';

@Component({
  selector: 'app-upcoming-events',
  standalone: true,
  imports: [CommonModule, EventCardComponent],
  templateUrl: './upcoming-events.component.html',
  styleUrls: ['./upcoming-events.component.css']
})
export class UpcomingEventsComponent implements OnInit {
  currentPage = 1;
  itemsPerPage = 6;
  totalPages = 1;
  pages: number[] = [];

  events: EventItemDisplay[] = [];
  allEvents: EventDetail[] = [];
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
        this.allEvents = events;
        this.events = events.map((e) => this.eventsDataService.mapToDisplayEvent(e));
        this.calculatePagination();
        this.cdr.detectChanges();
      },
      error: () => {
        this.dataLoadingError = 'Unable to load events from /data/events.json.';
        this.cdr.detectChanges();
      }
    });
  }

  private calculatePagination(): void {
    this.totalPages = Math.ceil(this.events.length / this.itemsPerPage);
    this.pages = Array.from({ length: this.totalPages }, (_, i) => i + 1);
    this.currentPage = 1;
  }

  get paginatedEvents(): EventItemDisplay[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.events.slice(startIndex, startIndex + this.itemsPerPage);
  }

  setPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }
}
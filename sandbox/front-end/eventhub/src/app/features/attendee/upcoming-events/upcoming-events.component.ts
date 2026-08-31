import {
  Component,
  OnInit,
  ChangeDetectorRef,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { EventCardComponent } from './components/event-card/event-card.component';

import {
  EventsDataService,
  EventDiscoveryResponseDto,
  SpringPageResponse,
  EventType
} from '../services/events-data.service';

import { NavbarContextService } from '../../../core/services/navbar-context.service';

import { EventItemDisplay } from '../models/attendee.model';


@Component({
  selector: 'app-upcoming-events',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    EventCardComponent
  ],
  templateUrl: './upcoming-events.component.html',
  styleUrls: ['./upcoming-events.component.css']
})
export class UpcomingEventsComponent implements OnInit {

  // ============================================================
  // Pagination
  // ============================================================

  currentPage = 1;

  itemsPerPage = 6;

  totalPages = 1;

  totalElements = 0;

  pages: number[] = [];


  // ============================================================
  // Filters
  // ============================================================

  searchTerm = '';

  selectedEventType: EventType | 'All' = 'All';

  locationFilter = '';

  startFrom = '';

  startTo = '';


  // ============================================================
  // Filter Options
  // ============================================================

  eventTypeOptions:
    Array<EventType | 'All'> = [
      'All',
      'PHYSICAL',
      'VIRTUAL',
      'HYBRID'
    ];


  // ============================================================
  // Events
  // ============================================================

  filteredEvents: EventItemDisplay[] = [];


  // ============================================================
  // UI State
  // ============================================================

  dataLoadingError = '';

  isLoading = false;


  // ============================================================
  // Services
  // ============================================================

  private readonly eventsDataService =
    inject(EventsDataService);

  private readonly navbarContext =
    inject(NavbarContextService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // ============================================================
  // Initialization
  // ============================================================

  ngOnInit(): void {

    this.navbarContext.setCurrentPage('events');

    this.navbarContext.setEventName(null);

    this.loadEvents();
  }


  // ============================================================
  // API
  // ============================================================

  private loadEvents(): void {

    this.dataLoadingError = '';

    this.isLoading = true;


    /*
     * Angular UI:
     *   page 1, 2, 3...
     *
     * Spring:
     *   page 0, 1, 2...
     */
    const backendPage =
      this.currentPage - 1;


    this.eventsDataService
      .getDiscoverableEvents(
        backendPage,
        this.itemsPerPage,
        {
          keyword: this.searchTerm,

          eventType:
            this.selectedEventType,

          startFrom:
            this.startFrom,

          startTo:
            this.startTo,

          location:
            this.locationFilter
        }
      )
      .subscribe({

        next: (
          response:
            SpringPageResponse<EventDiscoveryResponseDto>
        ) => {

          this.filteredEvents =
            response.content.map(
              dto =>
                this.eventsDataService
                  .mapToDisplayEvent(dto)
            );


          this.totalElements =
            response.totalElements;


          this.totalPages =
            response.totalPages;


          /*
           * Keep pagination sane if the backend
           * reports zero pages.
           */
          if (this.totalPages < 1) {
            this.totalPages = 1;
          }


          this.updatePagination();


          this.isLoading = false;

          this.cdr.detectChanges();
        },


        error: (error) => {

          console.error(
            'Unable to load discoverable events:',
            error
          );


          this.filteredEvents = [];

          this.totalElements = 0;

          this.totalPages = 1;

          this.pages = [1];

          this.isLoading = false;

          this.dataLoadingError =
            'Unable to load discoverable events. Please try again.';

          this.cdr.detectChanges();
        }

      });
  }


  // ============================================================
  // Filters
  // ============================================================

  applyFilters(): void {

    /*
     * Every new filter combination starts
     * from the first backend page.
     */
    this.currentPage = 1;

    this.loadEvents();
  }


  clearFilters(): void {

    this.searchTerm = '';

    this.selectedEventType = 'All';

    this.locationFilter = '';

    this.startFrom = '';

    this.startTo = '';

    this.currentPage = 1;

    this.loadEvents();
  }


  // ============================================================
  // Pagination
  // ============================================================

  setPage(page: number): void {

    if (
      page < 1 ||
      page > this.totalPages ||
      page === this.currentPage
    ) {
      return;
    }


    this.currentPage = page;

    this.loadEvents();
  }


  private updatePagination(): void {

    this.pages = Array.from(
      {
        length: this.totalPages
      },
      (_, index) => index + 1
    );
  }


  // ============================================================
  // Display Helpers
  // ============================================================

  get showingStart(): number {

    if (this.totalElements === 0) {
      return 0;
    }


    return (
      (this.currentPage - 1) *
      this.itemsPerPage
    ) + 1;
  }


  get showingEnd(): number {

    return Math.min(
      this.currentPage * this.itemsPerPage,
      this.totalElements
    );
  }
}
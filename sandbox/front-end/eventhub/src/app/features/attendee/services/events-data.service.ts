import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventItemDisplay } from '../models/attendee.model';

export type EventType = 'PHYSICAL' | 'VIRTUAL' | 'HYBRID';

export interface EventDiscoveryResponseDto {
  id: number;
  organizationId: number;
  organizationName: string;
  title: string;
  description: string;
  bannerImageUrl: string;
  eventType: EventType;
  locationAddress: string;
  virtualMeetingUrl: string;
  startTime: string;
  endTime: string;
  cateringProvided: boolean;
  maxCapacity: number;
  availableSlots: number;
  registrationOpen: boolean;
}

export interface SpringPageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class EventsDataService {

  private readonly http = inject(HttpClient);

  private readonly baseUrl = '/api/events/discover';


  /**
   * GET /api/events/discover
   *
   * All filtering and pagination are handled by the backend.
   */
  getDiscoverableEvents(
    page: number,
    size: number,
    filters: {
      keyword?: string;
      eventType?: EventType | 'All';
      startFrom?: string;
      startTo?: string;
      location?: string;
    }
  ): Observable<SpringPageResponse<EventDiscoveryResponseDto>> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'startTime,asc');

    if (filters.keyword?.trim()) {
      params = params.set(
        'keyword',
        filters.keyword.trim()
      );
    }

    if (
      filters.eventType &&
      filters.eventType !== 'All'
    ) {
      params = params.set(
        'eventType',
        filters.eventType
      );
    }

    if (filters.startFrom?.trim()) {
      params = params.set(
        'startFrom',
        filters.startFrom
      );
    }

    if (filters.startTo?.trim()) {
      params = params.set(
        'startTo',
        filters.startTo
      );
    }

    if (filters.location?.trim()) {
      params = params.set(
        'location',
        filters.location.trim()
      );
    }

    return this.http.get<
      SpringPageResponse<EventDiscoveryResponseDto>
    >(this.baseUrl, { params });
  }


  /**
   * GET /api/events/discover/{eventId}
   */
  getDiscoverableEvent(
    eventId: number
  ): Observable<EventDiscoveryResponseDto> {

    return this.http.get<EventDiscoveryResponseDto>(
      `${this.baseUrl}/${eventId}`
    );
  }


  /**
   * Convert backend discovery DTO into the
   * EventItemDisplay model used by EventCardComponent.
   */
  mapToDisplayEvent(
    dto: EventDiscoveryResponseDto
  ): EventItemDisplay {

    const maximumCapacity = dto.maxCapacity ?? 0;

    const remainingSlots = dto.availableSlots ?? 0;

    const registered =
      Math.max(
        0,
        maximumCapacity - remainingSlots
      );


    let status: EventItemDisplay['status'];

    let statusLabel: string;

    let statusClass: EventItemDisplay['statusClass'];


    if (dto.registrationOpen && remainingSlots > 0) {

      status = 'registration_open';

      statusLabel = 'Registration Open';

      statusClass = 'status-open';

    } else {

      status = 'registration_closed';

      statusLabel = 'Registration Closed';

      statusClass = 'status-closed';
    }


    return {

      id: dto.id.toString(),

      title: dto.title,

      description: dto.description ?? '',

      organizerId: dto.organizationId.toString(),

      organizerName: dto.organizationName,

      status,

      startDateTime: dto.startTime,

      endDateTime: dto.endTime,

      /*
       * The discovery DTO does not currently expose
       * registrationOpensAt / registrationClosesAt.
       *
       * Therefore we don't invent those values.
       */
      registrationOpensAt: '',

      registrationClosesAt: '',

      venue:
        dto.eventType === 'VIRTUAL'
          ? (dto.virtualMeetingUrl || 'Online Event')
          : (dto.locationAddress || 'Online Event'),

      bannerImageUrl:
        dto.bannerImageUrl ?? '',

      capacity: {
        maximum: maximumCapacity,
        registered
      },

      agenda: [],

      statusLabel,

      statusClass,

      remainingSlots
    };
  }
}
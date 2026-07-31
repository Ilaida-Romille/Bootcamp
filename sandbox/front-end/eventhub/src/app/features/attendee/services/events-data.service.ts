import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { EventDetail, EventItemDisplay } from '../models/attendee.model';

type EventsListResponse = EventDetail[] | { content?: EventDetail[]; items?: EventDetail[]; data?: EventDetail[] };

@Injectable({ providedIn: 'root' })
export class EventsDataService {
  private readonly baseUrl = '/api/events';

  constructor(private readonly http: HttpClient) {}

  getEvents(): Observable<EventDetail[]> {
    return this.http.get<EventsListResponse>(`${this.baseUrl}?all=true`).pipe(
      map((response) => {
        if (Array.isArray(response)) return response;
        return response.content ?? response.items ?? response.data ?? [];
      })
    );
  }

  getEventById(eventId: string): Observable<EventDetail> {
    return this.http.get<EventDetail>(`${this.baseUrl}/${encodeURIComponent(eventId)}`);
  }

  mapToDisplayEvent(event: EventDetail): EventItemDisplay {
    const remaining = event.capacity.maximum - event.capacity.registered;
    const fillRatio = event.capacity.maximum > 0 ? event.capacity.registered / event.capacity.maximum : 0;

    let statusLabel: string;
    let statusClass: EventItemDisplay['statusClass'];

    switch (event.status) {
      case 'registration_open':
        if (fillRatio >= 0.9) { statusLabel = 'Almost Full'; statusClass = 'status-full'; }
        else if (fillRatio >= 0.7) { statusLabel = 'Filling Fast'; statusClass = 'status-filling'; }
        else { statusLabel = 'Registration Open'; statusClass = 'status-open'; }
        break;
      case 'registration_closed':
        statusLabel = 'Registration Closed'; statusClass = 'status-closed'; break;
      case 'ongoing':
        statusLabel = 'Ongoing'; statusClass = 'status-open'; break;
      case 'completed':
        statusLabel = 'Completed'; statusClass = 'status-closed'; break;
      case 'cancelled':
        statusLabel = 'Cancelled'; statusClass = 'status-cancelled'; break;
      default:
        statusLabel = 'Draft'; statusClass = 'status-draft';
    }

    return { ...event, statusLabel, statusClass, remainingSlots: Math.max(0, remaining) };
  }
}

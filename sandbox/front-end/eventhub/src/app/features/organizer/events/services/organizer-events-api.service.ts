import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Event } from '../models/event.model';

type EventsListResponse =
  | Event[]
  | { content?: Event[]; items?: Event[]; data?: Event[] };

export interface EventInput {
  title: string;
  description: string;
  organizerId: string;
  organizerName: string;
  status: string;
  startDateTime: string;
  endDateTime: string;
  registrationOpensAt: string;
  registrationClosesAt: string;
  venue: string;
  bannerImageUrl: string;
  capacity: { maximum: number; registered: number };
  agenda: Array<{
    id?: string;
    startDateTime: string;
    endDateTime: string;
    title: string;
    description?: string;
    location?: string;
    speaker?: string;
    isBreak: boolean;
  }>;
}

export interface EventPatch {
  title?: string;
  description?: string;
  organizerId?: string;
  organizerName?: string;
  status?: string;
  startDateTime?: string;
  endDateTime?: string;
  registrationOpensAt?: string;
  registrationClosesAt?: string;
  venue?: string;
  bannerImageUrl?: string;
  capacity?: { maximum: number; registered: number };
  agenda?: Array<{
    id?: string;
    startDateTime: string;
    endDateTime: string;
    title: string;
    description?: string;
    location?: string;
    speaker?: string;
    isBreak: boolean;
  }>;
}

@Injectable({ providedIn: 'root' })
export class OrganizerEventsApiService {
  private readonly baseUrl = '/api/events';

  constructor(private readonly http: HttpClient) {}

  getEvents(): Observable<Event[]> {
    return this.http.get<EventsListResponse>(this.baseUrl).pipe(
      map((response) => {
        if (Array.isArray(response)) {
          return response;
        }

        return response.content ?? response.items ?? response.data ?? [];
      })
    );
  }

  getEventById(id: string): Observable<Event> {
    return this.http.get<Event>(`${this.baseUrl}/${encodeURIComponent(id)}`);
  }

  createEvent(event: EventInput): Observable<Event> {
    return this.http.post<Event>(this.baseUrl, event);
  }

  updateEvent(id: string, event: EventInput): Observable<Event> {
    return this.http.put<Event>(`${this.baseUrl}/${encodeURIComponent(id)}`, event);
  }

  patchEvent(id: string, event: EventPatch): Observable<Event> {
    return this.http.patch<Event>(`${this.baseUrl}/${encodeURIComponent(id)}`, event);
  }

  deleteEvent(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${encodeURIComponent(id)}`);
  }
}

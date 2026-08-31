import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map, tap, catchError, throwError } from 'rxjs';
import { Event, EventResponse, EventStatus, EventType, PaginatedResponse } from '../models/event.model';

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

  private readonly http = inject(HttpClient);

  readonly events = signal<Event[]>([]);
  readonly selectedEvent = signal<Event | null>(null);
  readonly loading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  private handleError(err: unknown): Observable<never> {
    const message = err instanceof Error ? err.message : 'An error occurred';
    this.error.set(message);
    this.loading.set(false);
    return throwError(() => err);
  }

  getEvents(
    page: number = 0,
    size: number = 10,
    sort: string = 'startTime,desc',
    status?: EventStatus,
    eventType?: EventType,
    search?: string
  ): Observable<PaginatedResponse<EventResponse>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    if (status) params = params.set('status', status);
    if (eventType) params = params.set('eventType', eventType);
    if (search) params = params.set('search', search);

    return this.http.get<PaginatedResponse<EventResponse>>(this.baseUrl, { params });
  }

  getEventById(id: string): Observable<Event> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.get<Event>(`${this.baseUrl}/${encodeURIComponent(id)}`).pipe(
      tap((event) => {
        this.selectedEvent.set(event);
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  createEvent(event: EventInput): Observable<Event> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.post<Event>(this.baseUrl, event).pipe(
      tap((created) => {
        this.events.update((list) => [...list, created]);
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  updateEvent(id: string, event: EventInput): Observable<Event> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.put<Event>(`${this.baseUrl}/${encodeURIComponent(id)}`, event).pipe(
      tap((updated) => {
        this.events.update((list) => list.map((e) => (e.id === id ? updated : e)));
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  patchEvent(id: string, event: EventPatch): Observable<Event> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.patch<Event>(`${this.baseUrl}/${encodeURIComponent(id)}`, event).pipe(
      tap((patched) => {
        this.events.update((list) => list.map((e) => (e.id === id ? patched : e)));
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  deleteEvent(id: string): Observable<void> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.delete<void>(`${this.baseUrl}/${encodeURIComponent(id)}`).pipe(
      tap(() => {
        this.events.update((list) => list.filter((e) => e.id !== id));
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }
}

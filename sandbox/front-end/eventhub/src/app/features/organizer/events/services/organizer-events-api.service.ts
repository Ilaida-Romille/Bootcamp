import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap, catchError, throwError } from 'rxjs';
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

  getEvents(): Observable<Event[]> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.get<EventsListResponse>(`${this.baseUrl}?all=true`).pipe(
      map((response) => {
        if (Array.isArray(response)) {
          return response;
        }

        return response.content ?? response.items ?? response.data ?? [];
      }),
      tap((result) => {
        this.events.set(result);
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
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

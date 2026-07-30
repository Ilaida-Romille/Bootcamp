import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Event } from '../models/event.model';

type EventsListResponse =
  | Event[]
  | { content?: Event[]; items?: Event[]; data?: Event[] };

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
}

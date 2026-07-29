import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { EventDetail, EventItemDisplay } from '../models/attendee.model';

@Injectable({ providedIn: 'root' })
export class EventsDataService {
  private readonly dataUrl = '/data/events.json';
  private eventsCache$ = new BehaviorSubject<EventDetail[]>([]);

  constructor(private readonly http: HttpClient) {}

  getEvents(): Observable<EventDetail[]> {
    if (this.eventsCache$.value.length > 0) {
      return this.eventsCache$.asObservable();
    }

    return this.http.get<EventDetail[]>(this.dataUrl).pipe(
      tap((events) => this.eventsCache$.next(events))
    );
  }

  getEventById(eventId: string): Observable<EventDetail> {
    return new Observable((observer) => {
      this.getEvents().subscribe((events) => {
        const event = events.find((e) => e.id === eventId);
        if (event) {
          observer.next(event);
          observer.complete();
        } else {
          observer.error(new Error(`Event ${eventId} not found`));
        }
      });
    });
  }

  mapToDisplayEvent(event: EventDetail): EventItemDisplay {
    let statusClass: 'status-open' | 'status-filling' | 'status-full';
    if (event.status === 'Almost Full') {
      statusClass = 'status-full';
    } else if (event.status === 'Filling Fast') {
      statusClass = 'status-filling';
    } else {
      statusClass = 'status-open';
    }

    return {
      ...event,
      statusClass
    };
  }
}

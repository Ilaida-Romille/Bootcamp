import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { RegisteredAttendee } from '../models/attendee.model';
import { EventDiscoveryResponseDto } from './events-data.service';

export interface RegistrationResponseDto {
  id: number;
  eventId: number;
  userId: number;
  registeredAt: string;
}

export interface AttendeeRegistrationDto {
  id: number;
  attendeeId: number;
  attendeeName: string;
  eventId: number;
  registeredAt: string;
}

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private readonly http = inject(HttpClient);

  getEventDetails(eventId: number): Observable<EventDiscoveryResponseDto> {
    return this.http.get<EventDiscoveryResponseDto>(`/api/events/discover/${eventId}`);
  }

  register(eventId: number): Observable<RegistrationResponseDto> {
    return this.http.post<RegistrationResponseDto>('/api/registrations', { eventId });
  }

  getEventAttendees(eventId: number): Observable<{ content: AttendeeRegistrationDto[] }> {
    return this.http.get<{ content: AttendeeRegistrationDto[] }>(
      `/api/events/${eventId}/registrations/attendees`
    );
  }

  getRegistrationsByEventId(_eventId: string): Observable<RegisteredAttendee[]> {
    return of([]);
  }
}

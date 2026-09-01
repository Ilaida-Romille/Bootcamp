import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RegistrationResponseDto {
  id: number;
  eventId: number;
  eventTitle: string;
  attendeeId: number;
  attendeeName: string;
  status: string;
  checkedInAt?: string;
  registeredAt?: string;
}

export interface PaginatedRegistrations {
  content: RegistrationResponseDto[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface EmailSendRequest {
  eventId: number;
  recipientUserId?: number | null;
  subject: string;
  messageBody: string;
}

export interface NotificationLogResponseDto {
  id: number;
  eventId: number;
  senderUserId: number;
  senderName: string;
  recipientUserId: number | null;
  notificationType: string;
  subject: string;
  messageBody: string;
  sentAt: string;
  deliveryStatus: string;
}

@Injectable({ providedIn: 'root' })
export class NotificationApiService {
  private readonly http = inject(HttpClient);

  getEventRegistrations(eventId: number): Observable<PaginatedRegistrations> {
    const params = new HttpParams()
      .set('page', '0')
      .set('size', '500');

    return this.http.get<PaginatedRegistrations>(
      `/api/events/${eventId}/registrations`,
      { params }
    );
  }

  sendNotificationEmail(request: EmailSendRequest): Observable<NotificationLogResponseDto> {
    return this.http.post<NotificationLogResponseDto>(
      '/api/notifications/email',
      request
    );
  }
}
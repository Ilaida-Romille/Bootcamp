// export type EventStatus =
//   | 'draft'
//   | 'registration_open'
//   | 'registration_closed'
//   | 'ongoing'
//   | 'completed'
//   | 'cancelled';

export interface EventCapacity {
  maximum: number;
  registered: number;
}

export interface AgendaItem {
  id: string;
  startTime: string;
  endTime: string;
  title: string;
  description?: string;
  location?: string;
  speaker?: string;
  isBreak: boolean;
}

export interface Event {
  id: number;
  title: string;
  description: string;
  organizerId: string;
  organizerName: string;
  status: Eventstatus;
  startTime: string;
  endTime: string;
  registrationOpensAt: string;
  registrationClosesAt: string;
  locationAddress: string;
  bannerImageUrl: string;
  capacity: EventCapacity;
}

//Back-end integration

// event.models.ts

export type EventType = 'PHYSICAL' | 'VIRTUAL' | 'HYBRID';
export type Eventstatus = 'DRAFT' | 'PUBLISHED' | 'COMPLETED' | 'CANCELLED';

export interface EventResponse {
  id: number;
  organizationId: number;
  organizationName: string;
  createdByUserId: number;
  createdByName: string;
  title: string;
  description: string;
  bannerImageUrl: string;
  eventType: EventType;
  locationAddress: string;
  virtualMeetingUrl: string;
  startTime: string;
  endTime: string;
  registrationStartTime: string;
  registrationEndTime: string;
  isPrivate: boolean;
  cateringProvided: boolean;
  maxCapacity: number;
  status: Eventstatus;
  createdAt: string;
  updatedAt: string;
}

export interface EventCreateRequest {
  title: string;
  description: string;
  bannerImageUrl: string;
  eventType: EventType;
  locationAddress: string;
  virtualMeetingUrl: string;
  startTime: string;
  endTime: string;
  isPrivate: boolean;
  cateringProvided: boolean;
  maxCapacity: number;
}

// Partial wrapper for Spring Data's Page<T>
export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface Agenda {
  id: number;
  eventId: number;
  agendaDate: string; // YYYY-MM-DD
  title: string;
  description?: string;
}

export interface Track {
  id: number;
  agendaId: number;
  name: string;
  description?: string;
  displayOrder: number;
}

export interface Session {
  id: number;
  trackId: number;
  title: string;
  description?: string;
  startTime: string;
  endTime: string;
  locationOrRoom?: string;
  speakerIds?: number[];
}

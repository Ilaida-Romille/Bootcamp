export type EventStatus =
  | 'draft'
  | 'registration_open'
  | 'registration_closed'
  | 'ongoing'
  | 'completed'
  | 'cancelled';

export interface EventCapacity {
  maximum: number;
  registered: number;
}

export interface AgendaItem {
  id: string;
  startDateTime: string;
  endDateTime: string;
  title: string;
  description?: string;
  location?: string;
  speaker?: string;
  isBreak: boolean;
}

export interface Event {
  id: string;
  title: string;
  description: string;
  organizerId: string;
  organizerName: string;
  status: Eventstatus;
  startDateTime: string;
  endDateTime: string;
  registrationOpensAt: string;
  registrationClosesAt: string;
  venue: string;
  bannerImageUrl: string;
  capacity: EventCapacity;
  agenda: AgendaItem[];
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
  isPrivate: boolean;
  cateringProvided: boolean;
  maxCapacity: number;
  status: EventStatus;
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

export type EventStatus =
  | 'draft'
  | 'registration_open'
  | 'registration_closed'
  | 'ongoing'
  | 'completed'
  | 'cancelled';


export interface ApiAgendaItem {
  id: string;
  startDateTime: string;
  endDateTime: string;
  title: string;
  description?: string;
  location?: string;
  speaker?: string;
  isBreak: boolean;
}


export interface EventDetail {
  id: string;
  title: string;
  description: string;
  organizerId: string;
  organizerName: string;
  status: EventStatus;
  startDateTime: string;
  endDateTime: string;
  registrationOpensAt: string;
  registrationClosesAt: string;
  venue: string;
  bannerImageUrl: string;
  capacity: {
    maximum: number;
    registered: number;
  };
  agenda: ApiAgendaItem[];
}


export interface EventItemDisplay extends EventDetail {

  statusLabel: string;

  statusClass:
  | 'status-open'
  | 'status-filling'
  | 'status-full'
  | 'status-closed'
  | 'status-draft'
  | 'status-cancelled';

  remainingSlots: number;

  isRegistered: boolean;
}


export interface Registration {
  id: string;
  eventId: string;
  fullName: string;
  emailAddress: string;
  companyDept: string;
  dietary: string;
  additionalNotes: string;
  registeredAt: string;
}


export interface RegisteredAttendee {
  id: string;
  name: string;
  company: string;
  email: string;
  eventId: string;
}
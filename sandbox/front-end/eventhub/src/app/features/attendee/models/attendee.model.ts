export interface EventDetail {
  id: string;
  title: string;
  date: string;
  organizerId: string;
  organizerName: string;
  currentAttendees: number;
  status: 'Registration Open' | 'Filling Fast' | 'Almost Full';
  remainingSlots: number;
  capacity: number;
  cateringProvided: boolean;
  description: string;
  attendeeIds: string[];
}

export interface EventItemDisplay extends EventDetail {
  statusClass: 'status-open' | 'status-filling' | 'status-full';
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

export interface EmployeeRegistrationSummary {
  registrationId: string;
  eventId: string;
  title: string;
  canCancelRegistration?: boolean;
}

export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  company: string;
  avatarUrl?: string;
  role?: string;
  registeredEvents: EmployeeRegistrationSummary[];
}

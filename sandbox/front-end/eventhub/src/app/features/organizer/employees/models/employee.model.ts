export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  company: string;
  department: string;
  jobTitle: string;
  avatarUrl?: string;
  registeredEventIds: string[];
}

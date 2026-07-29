export type UserRole = 'attendee' | 'organizer' | 'platformOwner';

export interface SessionUser {
  id: string;
  email: string;
  name: string;
  role: UserRole;
  createdAt: string;
}

export interface Session {
  user: SessionUser;
  token: string; // Mock JWT token for demo
  expiresAt: string;
}

export interface LoginCredentials {
  email: string;
  password: string;
  role: UserRole;
}

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

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  user: any;
}

export interface JwtPayload {
  sub: string;
  email: string;
  role: string;
  authorities: string[];
  exp: number;
}

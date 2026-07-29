import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Session, SessionUser, LoginCredentials, UserRole } from '../models/session.model';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly storageKey = 'eventhub_session';
  private readonly sessionSubject$ = new BehaviorSubject<Session | null>(null);
  private readonly isAuthenticatedSubject$ = new BehaviorSubject<boolean>(false);

  constructor() {
    this.loadSessionFromStorage();
  }

  /**
   * Load session from localStorage on service initialization
   */
  private loadSessionFromStorage(): void {
    const stored = localStorage.getItem(this.storageKey);
    if (stored) {
      try {
        const session = JSON.parse(stored) as Session;
        // Check if session has expired
        if (new Date(session.expiresAt) > new Date()) {
          this.sessionSubject$.next(session);
          this.isAuthenticatedSubject$.next(true);
        } else {
          // Session expired, clear it
          this.logout();
        }
      } catch {
        // Invalid session data, clear it
        this.logout();
      }
    }
  }

  /**
   * Create a new session for the user (mock login)
   */
  login(credentials: LoginCredentials): Session {
    const now = new Date();
    const expiresAt = new Date(now.getTime() + 24 * 60 * 60 * 1000); // 24 hours from now

    const user: SessionUser = {
      id: `USER-${Date.now()}`,
      email: credentials.email,
      name: credentials.email.split('@')[0], // Extract name from email
      role: credentials.role,
      createdAt: now.toISOString()
    };

    const mockToken = btoa(`${user.id}:${credentials.email}:${Date.now()}`);

    const session: Session = {
      user,
      token: mockToken,
      expiresAt: expiresAt.toISOString()
    };

    // Persist to localStorage
    localStorage.setItem(this.storageKey, JSON.stringify(session));

    // Update observables
    this.sessionSubject$.next(session);
    this.isAuthenticatedSubject$.next(true);

    return session;
  }

  /**
   * Destroy the current session (logout)
   */
  logout(): void {
    localStorage.removeItem(this.storageKey);
    this.sessionSubject$.next(null);
    this.isAuthenticatedSubject$.next(false);
  }

  /**
   * Get the current session as observable
   */
  getSession(): Observable<Session | null> {
    return this.sessionSubject$.asObservable();
  }

  /**
   * Get the current session synchronously (for guards)
   */
  getCurrentSession(): Session | null {
    return this.sessionSubject$.value;
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject$.asObservable();
  }

  /**
   * Check if user is authenticated synchronously (for guards)
   */
  isAuthenticatedSync(): boolean {
    return this.isAuthenticatedSubject$.value;
  }

  /**
   * Get current user's role
   */
  getCurrentUserRole(): UserRole | null {
    return this.sessionSubject$.value?.user.role ?? null;
  }

  /**
   * Check if user has a specific role
   */
  hasRole(role: UserRole): boolean {
    return this.getCurrentUserRole() === role;
  }

  /**
   * Check if user has any of the specified roles
   */
  hasAnyRole(roles: UserRole[]): boolean {
    const currentRole = this.getCurrentUserRole();
    return currentRole !== null && roles.includes(currentRole);
  }
}

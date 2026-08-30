import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { Session, SessionUser, LoginCredentials, UserRole, AuthResponse, JwtPayload } from '../models/session.model';
import { AuthRoutingService } from './auth-routing.service';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly storageKey = 'eventhub_session';
  private readonly sessionSubject$ = new BehaviorSubject<Session | null>(null);
  private readonly isAuthenticatedSubject$ = new BehaviorSubject<boolean>(false);

  private readonly http = inject(HttpClient);
  private readonly authRouting = inject(AuthRoutingService);

  constructor() {
    this.loadSessionFromStorage();
  }

  private loadSessionFromStorage(): void {
    const stored = localStorage.getItem(this.storageKey);
    if (stored) {
      try {
        const session = JSON.parse(stored) as Session;
        if (new Date(session.expiresAt) > new Date()) {
          this.sessionSubject$.next(session);
          this.isAuthenticatedSubject$.next(true);
        } else {
          this.logout();
        }
      } catch {
        this.logout();
      }
    }
  }

  /**
   * Authenticates with Spring Boot backend and stores JWT session context
   */
  login(credentials: Omit<LoginCredentials, 'role'>): Observable<AuthResponse> {
    const payload = {
      email: credentials.email,
      password: credentials.password
    };

    return this.http.post<AuthResponse>('http://localhost:8080/api/auth/login', payload).pipe(
      tap((response) => {
        const decoded = jwtDecode<JwtPayload>(response.accessToken);
        const userRole = this.authRouting.mapBackendRoleToUserRole(decoded.role);

        if (!userRole) {
          throw new Error('Unrecognized backend role claim');
        }

        const user: SessionUser = {
          id: decoded.sub,
          email: decoded.email,
          name: decoded.email.split('@')[0],
          role: userRole,
          createdAt: new Date().toISOString()
        };

        const session: Session = {
          user,
          token: response.accessToken,
          expiresAt: new Date(decoded.exp * 1000).toISOString()
        };

        localStorage.setItem(this.storageKey, JSON.stringify(session));
        this.sessionSubject$.next(session);
        this.isAuthenticatedSubject$.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    this.sessionSubject$.next(null);
    this.isAuthenticatedSubject$.next(false);
  }

  getSession(): Observable<Session | null> {
    return this.sessionSubject$.asObservable();
  }

  getCurrentSession(): Session | null {
    return this.sessionSubject$.value;
  }

  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject$.asObservable();
  }

  isAuthenticatedSync(): boolean {
    return this.isAuthenticatedSubject$.value;
  }

  getCurrentUserRole(): UserRole | null {
    return this.sessionSubject$.value?.user.role ?? null;
  }

  hasRole(role: UserRole): boolean {
    return this.getCurrentUserRole() === role;
  }

  hasAnyRole(roles: UserRole[]): boolean {
    const currentRole = this.getCurrentUserRole();
    return currentRole !== null && roles.includes(currentRole);
  }
}
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from './session.service';

/**
 * Session Utility Service - provides convenient methods for session management
 * and logout functionality across the application
 */
@Injectable({ providedIn: 'root' })
export class SessionUtilityService {
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);

  /**
   * Logout the current user and redirect to landing page
   */
  logout(): void {
    this.sessionService.logout();
    this.router.navigateByUrl('');
  }

  /**
   * Get current user email (convenience method)
   */
  getCurrentUserEmail(): string | null {
    return this.sessionService.getCurrentSession()?.user.email ?? null;
  }

  /**
   * Get current user name (convenience method)
   */
  getCurrentUserName(): string | null {
    return this.sessionService.getCurrentSession()?.user.name ?? null;
  }

  /**
   * Get current user role (convenience method)
   */
  getCurrentUserRole(): string | null {
    return this.sessionService.getCurrentSession()?.user.role ?? null;
  }

  /**
   * Get session expiry time (convenience method)
   */
  getSessionExpiryTime(): Date | null {
    const expiresAt = this.sessionService.getCurrentSession()?.expiresAt;
    return expiresAt ? new Date(expiresAt) : null;
  }

  /**
   * Check if session is expiring soon (within 5 minutes)
   */
  isSessionExpiringsoon(): boolean {
    const expiryTime = this.getSessionExpiryTime();
    if (!expiryTime) return false;

    const now = new Date();
    const fiveMinutesFromNow = new Date(now.getTime() + 5 * 60 * 1000);
    
    return expiryTime <= fiveMinutesFromNow;
  }
}

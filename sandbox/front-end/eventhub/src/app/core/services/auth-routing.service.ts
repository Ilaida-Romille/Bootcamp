import { Injectable } from '@angular/core';
import { ROUTE_PATHS } from '../../app.routes';
import { UserRole } from '../models/session.model';

/**
 * Resolves which destination a given email belongs to.
 */
@Injectable({ providedIn: 'root' })
export class AuthRoutingService {
  /**
   * Returns the role for the given email, or `null` when access is denied.
   */
  resolveRole(rawEmail: string): UserRole | null {
    const email = rawEmail.trim().toLowerCase();

    // 1. Platform Owner Domain -> 'platformOwner'
    if (email.endsWith('@eventhub.com')) {
      return 'platformOwner';
    }

    // 2. Organizer Domain/User -> 'organizer'
    const isNamedOrgAdmin = email === 'admin@company.com';
    const isDomainAdmin =
      email.startsWith('admin') && 
      (email.includes('@foundation.') || email.includes('@school.') || email.includes('@org'));

    if (isNamedOrgAdmin || isDomainAdmin) {
      return 'organizer';
    }

    // 3. Attendee Domain -> 'attendee'
    const isRegisteredCorporateDomain =
      email.includes('@company.com') || email.includes('@school.') || email.includes('@foundation.');

    if (isRegisteredCorporateDomain) {
      return 'attendee';
    }

    return null;
  }

  /**
   * Returns the top-level route path the given email should land on, 
   * or `null` when access is denied.
   */
  resolveRoute(rawEmail: string): string | null {
    const email = rawEmail.trim().toLowerCase();

    // 1. Platform Owner Domain -> Navigate to '/platform-owner'
    if (email.endsWith('@eventhub.com')) {
      return ROUTE_PATHS.platformOwner;
    }

    // 2. Organizer Domain/User -> Navigate to '/organizer'
    const isNamedOrgAdmin = email === 'admin@company.com';
    const isDomainAdmin =
      email.startsWith('admin') && 
      (email.includes('@foundation.') || email.includes('@school.') || email.includes('@org'));

    if (isNamedOrgAdmin || isDomainAdmin) {
      return ROUTE_PATHS.organizer;
    }

    // 3. Attendee Domain -> Navigate to '/dashboard'
    const isRegisteredCorporateDomain =
      email.includes('@company.com') || email.includes('@school.') || email.includes('@foundation.');

    if (isRegisteredCorporateDomain) {
      return ROUTE_PATHS.attendee;
    }

    return null;
  }
}
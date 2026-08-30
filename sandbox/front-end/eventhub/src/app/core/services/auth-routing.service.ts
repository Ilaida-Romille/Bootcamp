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
  // resolveRole(rawEmail: string): UserRole | null {
  //   const email = rawEmail.trim().toLowerCase();

  //   // 1. Platform Owner Domain -> 'platformOwner'
  //   if (email.endsWith('@eventhub.com')) {
  //     return 'platformOwner';
  //   }

  //   // 2. Organizer Domain/User -> 'organizer'
  //   const isNamedOrgAdmin = email === 'admin@company.com';
  //   const isDomainAdmin =
  //     email.startsWith('admin') && 
  //     (email.includes('@foundation.') || email.includes('@school.') || email.includes('@org'));

  //   if (isNamedOrgAdmin || isDomainAdmin) {
  //     return 'organizer';
  //   }

  //   // 3. Attendee Domain -> 'attendee'
  //   const isRegisteredCorporateDomain =
  //     email.includes('@company.com') || email.includes('@school.') || email.includes('@foundation.');

  //   if (isRegisteredCorporateDomain) {
  //     return 'attendee';
  //   }

  //   return null;
  // }

  /**
   * Returns the top-level route path the given email should land on, 
   * or `null` when access is denied.
   */
  resolveRoute(role: UserRole): string | null {
    switch (role) {
      case 'platformOwner':
        return ROUTE_PATHS.platformOwner;
      case 'organizer':
        return ROUTE_PATHS.organizer;
      case 'attendee':
        return ROUTE_PATHS.attendee;
      default:
        return null;
    }
  }

  mapBackendRoleToUserRole(backendRole: string): UserRole | null {
    switch (backendRole) {
      case 'PLATFORM_ADMIN':
        return 'platformOwner';
      case 'ORGANIZER_ADMIN':
      case 'ORGANIZER_STAFF':
        return 'organizer';
      case 'ATTENDEE':
        return 'attendee';
      default:
        return null;
    }
  }
}
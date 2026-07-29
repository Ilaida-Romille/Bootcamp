import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { SessionService } from '../services/session.service';
import { UserRole } from '../models/session.model';

/**
 * Role-based guard - ensures user has the required role for a route
 * Usage in route data: { requiredRole: 'attendee' }
 * If user doesn't have the required role, redirects to landing page
 */
export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  // First check if user is authenticated
  if (!sessionService.isAuthenticatedSync()) {
    router.navigate(['']);
    return false;
  }

  // Get required role from route data
  const requiredRole = route.data['requiredRole'] as UserRole | undefined;

  // If no role is specified, allow access (auth guard already checked authentication)
  if (!requiredRole) {
    return true;
  }

  // Check if user has the required role
  const userRole = sessionService.getCurrentUserRole();
  if (userRole === requiredRole) {
    return true;
  }

  // User doesn't have the required role, redirect to landing page
  router.navigate(['']);
  return false;
};

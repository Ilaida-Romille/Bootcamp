import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionService } from '../services/session.service';

/**
 * Authentication guard - ensures user is logged in before accessing protected routes
 * If not authenticated, redirects to landing page
 */
export const authGuard: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  if (sessionService.isAuthenticatedSync()) {
    return true;
  }

  // Not authenticated, redirect to landing page
  router.navigate(['']);
  return false;
};

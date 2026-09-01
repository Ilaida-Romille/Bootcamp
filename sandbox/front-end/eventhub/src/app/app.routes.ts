import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const ROUTE_PATHS = {
  landing: '',
  signup: 'signup',
  attendee: 'dashboard',
  organizer: 'organizer',
  platformOwner: 'platform-owner',
} as const;

export const routes: Routes = [
  // 1. Public / Auth Routes
  {
    path: ROUTE_PATHS.landing,
    loadComponent: () =>
      import('./features/landing/landing.component').then((m) => m.LandingComponent),
    title: 'EventHub | Sign In',
  },
  {
    path: ROUTE_PATHS.signup,
    loadComponent: () =>
      import('./features/landing/signup/signup.component').then((m) => m.SignupComponent),
    title: 'EventHub | Sign Up',
  },

  // 2. Attendee Feature Section (Protected - requires login as attendee)
  {
    path: ROUTE_PATHS.attendee,
    canActivate: [authGuard, roleGuard],
    data: { requiredRole: 'attendee' },
    loadChildren: () =>
      import('./features/attendee/attendee.routes').then((m) => m.ATTENDEE_ROUTES),
  },

  // 3. Organizer Feature Section (Protected - requires login as organizer)
  {
    path: ROUTE_PATHS.organizer,
    canActivate: [authGuard, roleGuard],
    data: { requiredRole: 'organizer' },
    loadChildren: () =>
      import('./features/organizer/organizer.routes').then((m) => m.ORGANIZER_ROUTES),
  },

  // 4. Platform Owner Feature Section (Protected - requires login as platformOwner)
  {
    path: ROUTE_PATHS.platformOwner,
    canActivate: [authGuard, roleGuard],
    data: { requiredRole: 'platformOwner' },
    loadChildren: () =>
      import('./features/platform/platform.routes').then((m) => m.PLATFORM_ROUTES),
  },

  // 5. Catch-all Wildcard Route
  { 
    path: '**', 
    redirectTo: ROUTE_PATHS.landing 
  },
];
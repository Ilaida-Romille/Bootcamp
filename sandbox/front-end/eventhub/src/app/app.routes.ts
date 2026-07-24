import { Routes } from '@angular/router';

export const ROUTE_PATHS = {
  landing: '',
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

  // 2. Attendee Feature Section
  {
    path: ROUTE_PATHS.attendee,
    loadChildren: () =>
      import('./features/attendee/attendee.routes').then((m) => m.ATTENDEE_ROUTES),
  },

  // 3. Organizer Feature Section (with optional layout wrapper)
  {
    path: ROUTE_PATHS.organizer,
    // loadComponent: () => import('./layouts/organizer-layout.component').then((m) => m.OrganizerLayoutComponent),
    loadChildren: () =>
      import('./features/organizer/organizer.routes').then((m) => m.ORGANIZER_ROUTES),
  },

  // 4. Platform Owner Feature Section (with Sidebar layout wrapper)
  {
    path: ROUTE_PATHS.platformOwner,
    // loadComponent: () => import('./layouts/platform-layout.component').then((m) => m.PlatformLayoutComponent),
    loadChildren: () =>
      import('./features/platform/platform.routes').then((m) => m.PLATFORM_ROUTES),
  },

  // 5. Catch-all Wildcard Route
  { 
    path: '**', 
    redirectTo: ROUTE_PATHS.landing 
  },
];
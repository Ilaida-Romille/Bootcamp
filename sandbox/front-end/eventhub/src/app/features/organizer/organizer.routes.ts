import { Routes } from '@angular/router';

export const ORGANIZER_ROUTE_PATHS = {
  dashboard: 'dashboard',
} as const;

export const ORGANIZER_ROUTES: Routes = [
  {path: '', 
    loadComponent: () =>
      import('./organizer-layout.component').then((m) => m.OrganizerLayoutComponent),
    children: [
      {path: '', redirectTo: ORGANIZER_ROUTE_PATHS.dashboard, pathMatch: 'full'},
      {
        path: ORGANIZER_ROUTE_PATHS.dashboard,
        loadComponent: () =>
          import('./dashboard/dashboard.component').then((m) => m.OrganizerDashboardComponent),
        title: 'EventHub | Organizer Dashboard'
      }
    ]
  }
];
import { Routes } from '@angular/router';

export const ORGANIZER_ROUTES: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./dashboard/dashboard').then((m) => m.OrganizerDashboardComponent),
    title: 'EventHub | Platform Owner Dashboard',
  }
];
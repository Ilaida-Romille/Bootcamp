import { Routes } from '@angular/router';

export const ATTENDEE_ROUTES: Routes = [
  { path: '', redirectTo: 'upcoming_events', pathMatch: 'full' },
  {
    path: 'upcoming_events',
    loadComponent: () =>
      import('./upcoming-events/upcoming-events.component').then((m) => m.UpcomingEventsComponent),
    title: 'EventHub | Upcoming Events',
  },
  {
    path: 'registration',
    loadComponent: () =>
      import('./registration/registration.component').then((m) => m.RegistrationComponent),
    title: 'EventHub | Registration',
  },
  {
    path: 'agenda',
    loadComponent: () =>
      import('./agenda/agenda.component').then((m) => m.AgendaComponent),
    title: 'EventHub | Agenda',
  }
];
import { Routes } from '@angular/router';
import { SidebarItem } from '../../layout/sidebar/sidebar.component';
import { ROUTE_PATHS } from '../../app.routes';

export const ORGANIZER_ROUTE_PATHS = {
  dashboard: 'dashboard',
  employees: 'employees',
  events: 'events'
} as const;

export const ORGANIZER_NAV_ITEMS: SidebarItem[] = [
  { label: 'Dashboard', route: `/${ROUTE_PATHS.organizer}/${ORGANIZER_ROUTE_PATHS.dashboard}` },
  { label: 'Employees', route: `/${ROUTE_PATHS.organizer}/${ORGANIZER_ROUTE_PATHS.employees}` },
  { label: 'Events', route: `/${ROUTE_PATHS.organizer}/${ORGANIZER_ROUTE_PATHS.events}` },
];

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
      },
      {
        path: ORGANIZER_ROUTE_PATHS.employees,
        loadComponent: () =>
          import('./employees/employees.component').then((m) => m.OrganizerEmployeesComponent),
        title: 'EventHub | Organizer Employees'
      },
      {
        path: ORGANIZER_ROUTE_PATHS.events,
        loadComponent: () =>
          import('./events/events.component').then((m) => m.OrganizerEventsComponent),
        title: 'EventHub | Organizer Events'
      }
    ]
  }
];
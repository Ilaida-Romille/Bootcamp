import { Routes } from '@angular/router';
import { SidebarItem } from '../../layout/sidebar/sidebar.component';
import { ROUTE_PATHS}  from '../../app.routes';

export const PLATFORM_ROUTE_PATHS = {
  dashboard: 'dashboard',
  organizers: 'organizers',
  billing: 'billing',
  tickets: 'tickets',
} as const;

export const PLATFORM_NAV_ITEMS: SidebarItem[] = [
  { label: 'Dashboard', route: `/${ROUTE_PATHS.platformOwner}/${PLATFORM_ROUTE_PATHS.dashboard}` },
  { label: 'Organizers', route: `/${ROUTE_PATHS.platformOwner}/${PLATFORM_ROUTE_PATHS.organizers}` },
  { label: 'Billing & Invoices', route: `/${ROUTE_PATHS.platformOwner}/${PLATFORM_ROUTE_PATHS.billing}` },
  { label: 'Tickets & Requests', route: `/${ROUTE_PATHS.platformOwner}/${PLATFORM_ROUTE_PATHS.tickets}` },
];

export const PLATFORM_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./platform-layout.component').then((m) => m.PlatformLayoutComponent),
    children: [
      { path: '', redirectTo: PLATFORM_ROUTE_PATHS.dashboard, pathMatch: 'full' },
      {
        path: PLATFORM_ROUTE_PATHS.dashboard,
        loadComponent: () =>
          import('./dashboard/dashboard.component').then((m) => m.PlatformOwnerDashboardComponent),
        title: 'EventHub | Dashboard',
      },
      {
        path: PLATFORM_ROUTE_PATHS.organizers,
        loadComponent: () =>
          import('./organizers/organizers.component').then((m) => m.OrganizersComponent),
        title: 'EventHub | Organizers',
      },
      {
        path: PLATFORM_ROUTE_PATHS.billing,
        loadComponent: () =>
          import('./billing/billing.component').then((m) => m.BillingComponent),
        title: 'EventHub | Billing',
      },
      {
        path: PLATFORM_ROUTE_PATHS.tickets,
        loadComponent: () =>
          import('./tickets/tickets.component').then((m) => m.TicketsComponent),
        title: 'EventHub | Tickets',
      },
    ],
  },
];
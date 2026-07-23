import { Routes } from '@angular/router';

/**
 * Route paths kept in one place so AuthRoutingService and the router
 * table can never drift apart. Update a destination in exactly one spot.
 */
export const ROUTE_PATHS = {
    landing: '',
    attendeeDashboard: 'dashboard',
    organizerDashboard: 'organizer/dashboard',
    platformOwnerDashboard: 'platform-owner/dashboard',
} as const;

export const PLATFORM_ROUTE_PATHS = {
    dashboard: 'dashboard',
    organizers: 'organizers',
    billing: 'billing',
    tickets: 'tickets',
} as const;

export const routes: Routes = [
    {
        path: ROUTE_PATHS.landing,
        loadComponent: () =>
            import('./features/landing/landing.component').then((m) => m.LandingComponent),
        title: 'EventHub | Sign In',
    },
    {
        // Not built yet — routed to on purpose per the "@eventhub.com" domain rule,
        // stubbed so the routing logic is real and testable today.
        path: ROUTE_PATHS.attendeeDashboard,
        loadComponent: () =>
            import('./features/attendee-upcoming-events/attendee-upcoming-events.component').then(
                (m) => m.UpcomingEventsComponent,
            ),
        title: 'EventHub | Dashboard',
    },
    {
        path: ROUTE_PATHS.organizerDashboard,
        loadComponent: () =>
            import('./features/organizer-dashboard/organizer-dashboard').then(
                (m) => m.OrganizerDashboardComponent,
            ),
        title: 'EventHub | Organizer Dashboard',
    },
    {
        path: ROUTE_PATHS.platformOwnerDashboard,
        loadComponent: () =>
            import('./features/platform/dashboard/dashboard.component').then(
                (m) => m.PlatformOwnerDashboardComponent,
            ),
        title: 'EventHub | Upcoming Events',
    },

    {
        path: ROUTE_PATHS.platformOwnerDashboard,
        children: [
            { path: '', redirectTo: PLATFORM_ROUTE_PATHS.dashboard, pathMatch: 'full' },
            {
                path: PLATFORM_ROUTE_PATHS.dashboard,
                loadComponent: () => import('./features/platform/dashboard/dashboard.component').then(m => m.PlatformOwnerDashboardComponent),
                title: 'EventHub | Platform Owner Dashboard',
            },
            {
                path: PLATFORM_ROUTE_PATHS.organizers,
                loadComponent: () => import('./features/platform/organizers/organizers.component').then(m => m.OrganizersComponent),
                title: 'EventHub | Organizers',
            },
            {
                path: PLATFORM_ROUTE_PATHS.billing,
                loadComponent: () => import('./features/platform/billing/billing.component').then(m => m.BillingComponent),
                title: 'EventHub | Billing & Invoices',
            },
            {
                path: PLATFORM_ROUTE_PATHS.tickets,
                loadComponent: () => import('./features/platform/tickets/tickets.component').then(m => m.TicketsComponent),
                title: 'EventHub | Tickets & Requests',
            },  
        ]
    },
    { path: '**', redirectTo: './features/landing.component' },
];
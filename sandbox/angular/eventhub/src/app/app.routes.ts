import { Routes } from '@angular/router';

/**
 * Route paths kept in one place so AuthRoutingService and the router
 * table can never drift apart. Update a destination in exactly one spot.
 */
export const ROUTE_PATHS = {
    landing: '',
    attendeeDashboard: 'dashboard',
    organizerDashboard: 'organizer/dashboard',
    upcomingEvents: 'users/upcoming-events',
} as const;

export const routes: Routes = [
    {
        path: ROUTE_PATHS.landing,
        loadComponent: () =>
            import('./features/landing/landing').then((m) => m.LandingComponent),
        title: 'EventHub | Sign In',
    },
    {
        // Not built yet — routed to on purpose per the "@eventhub.com" domain rule,
        // stubbed so the routing logic is real and testable today.
        path: ROUTE_PATHS.attendeeDashboard,
        loadComponent: () =>
            import('./features/attendee-upcoming-events/attendee-upcoming-events').then(
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
        path: ROUTE_PATHS.upcomingEvents,
        loadComponent: () =>
            import('./features/platform-owner-dashboard/platform-owner-dashboard').then(
                (m) => m.AttendeeDashboardComponent,
            ),
        title: 'EventHub | Upcoming Events',
    },
    { path: '**', redirectTo: '' },
];
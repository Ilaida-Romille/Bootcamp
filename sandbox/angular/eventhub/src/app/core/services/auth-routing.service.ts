import { Injectable } from '@angular/core';

import { ROUTE_PATHS } from '../../app.routes';

/**
 * Resolves which destination a given email belongs to. This is a stand-in
 * for a real backend auth/role lookup: today it's domain-pattern based,
 * exactly like the original page, but it lives behind one seam so a real
 * API call can replace the body later without touching the form component
 * or the route table.
 */
@Injectable({ providedIn: 'root' })
export class AuthRoutingService {
    /**
     * Returns the route path the given email should land on, or `null` when
     * the domain isn't a registered corporate domain (the original page's
     * "Access Denied" case).
     */
    resolveRoute(rawEmail: string): string | null {
        const email = rawEmail.trim().toLowerCase();

        if (email.endsWith('@eventhub.com')) {
            return ROUTE_PATHS.platformOwnerDashboard;
        }

        const isNamedOrgAdmin = email === 'admin@company.com';
        const isDomainAdmin =
            email.startsWith('admin') && (email.includes('@foundation.') || email.includes('@school.') || email.includes('@org'));

        if (isNamedOrgAdmin || isDomainAdmin) {
            return ROUTE_PATHS.organizerDashboard;
        }

        const isRegisteredCorporateDomain =
            email.includes('@company.com') || email.includes('@school.') || email.includes('@foundation.');

        if (isRegisteredCorporateDomain) {
            return ROUTE_PATHS.attendeeDashboard;
        }

        return null;
    }
}
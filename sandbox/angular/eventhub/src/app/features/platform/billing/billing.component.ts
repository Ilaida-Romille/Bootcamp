import { Component } from '@angular/core';
import { SidebarComponent, SidebarItem } from '../../../layout/sidebar/sidebar.component'
import { ROUTE_PATHS, PLATFORM_ROUTE_PATHS } from '../../../app.routes';

@Component({
  selector: 'app-billing',
  imports: [SidebarComponent],
  templateUrl: './billing.component.html',
  styleUrl: './billing.component.css',
})
export class BillingComponent {
  private base = `/${ROUTE_PATHS.platformOwnerDashboard}`
    
        adminNavItems: SidebarItem[] = [
        { label: 'Dashboard', route: `${this.base}/${PLATFORM_ROUTE_PATHS.dashboard}` },
        { label: 'Organizers', route: `${this.base}/${PLATFORM_ROUTE_PATHS.organizers}` },
        { label: 'Billing & Invoices', route: `${this.base}/${PLATFORM_ROUTE_PATHS.billing}` },
        { label: 'Tickets & Requests', route: `${this.base}/${PLATFORM_ROUTE_PATHS.tickets}` }
      ];
}

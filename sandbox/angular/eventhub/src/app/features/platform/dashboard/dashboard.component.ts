import { ChangeDetectionStrategy, Component } from '@angular/core';
import { SidebarComponent, SidebarItem } from '../../../layout/sidebar/sidebar.component'
import { ROUTE_PATHS, PLATFORM_ROUTE_PATHS } from '../../../app.routes';
import { CommonModule } from '@angular/common';
import { MetricCardComponent } from '../../..//shared/components/metric-card/metric-card.component';
import { ActionCardComponent } from '../../../shared/components/action-card/action-card.component';
import { ChartPanelComponent, ChartDataPoint } from './components/chart-panel/chart-panel.component';

@Component({
  selector: 'app-platform-owner-dashboard',
  standalone: true,
  imports: [
    SidebarComponent, 
    CommonModule, 
    MetricCardComponent, 
    ActionCardComponent, 
    ChartPanelComponent],
  template: `
    <div class="d-flex">
      <!-- 2. Bind the array here -->
      <app-sidebar [navItems]="adminNavItems"></app-sidebar>

      <main class="container-xl py-5 flex-grow-1">
        <h1 class="h3 fw-bold text-white">Platform Owner dashboard</h1>
        <p class="text-secondary">Coming soon — this route is wired up but not built yet.</p>
      </main>
    </div>
  `,
  templateUrl: './dashboard.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlatformOwnerDashboardComponent { 

    private readonly base = `/${ROUTE_PATHS.platformOwnerDashboard}`

    adminNavItems: SidebarItem[] = [
    { label: 'Dashboard', route: `${this.base}/${PLATFORM_ROUTE_PATHS.dashboard}` },
    { label: 'Organizers', route: `${this.base}/${PLATFORM_ROUTE_PATHS.organizers}` },
    { label: 'Billing & Invoices', route: `${this.base}/${PLATFORM_ROUTE_PATHS.billing}` },
    { label: 'Tickets & Requests', route: `${this.base}/${PLATFORM_ROUTE_PATHS.tickets}` }
  ];

  organizersCount = 128;
  eventsCount = 342;

  monthlyEventsData: ChartDataPoint[] = [
    { label: 'Jan', value: 35 },
    { label: 'Feb', value: 55 },
    { label: 'Mar', value: 45 },
    { label: 'Apr', value: 70 },
    { label: 'May', value: 50 },
    { label: 'Jun', value: 65 },
    { label: 'Jul', value: 30 },
    { label: 'Aug', value: 52 },
    { label: 'Sep', value: 40 },
    { label: 'Oct', value: 60 },
    { label: 'Nov', value: 68 },
    { label: 'Dec', value: 48 }
  ];
}
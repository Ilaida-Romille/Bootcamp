import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MetricCardComponent } from '../../../shared/components/metric-card/metric-card.component';
import { ActionCardComponent } from '../../../shared/components/action-card/action-card.component';
import { ChartPanelComponent, ChartDataPoint } from './components/chart-panel/chart-panel.component';
import { PlatformOwnerDashboardService } from './services/platform-owner-dashboard.service';

@Component({
  selector: 'app-platform-owner-dashboard',
  standalone: true,
  imports: [
    CommonModule, 
    MetricCardComponent, 
    ActionCardComponent, 
    ChartPanelComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css', // Optional if you have styles
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlatformOwnerDashboardComponent implements OnInit { 
  organizersCount = 0;
  eventsCount = 0;
  monthlyEventsData: ChartDataPoint[] = [];

  private readonly dashboardService = inject(PlatformOwnerDashboardService);
  private readonly cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
    this.dashboardService.getDashboardMetrics().subscribe({
      next: (metrics) => {
        this.organizersCount = metrics.organizersCount;
        this.eventsCount = metrics.eventsCount;
        this.monthlyEventsData = metrics.monthlyEventsData.map((entry) => ({
          label: entry.label,
          value: entry.value,
        }));
        this.cdr.markForCheck();
      },
    });
  }
}
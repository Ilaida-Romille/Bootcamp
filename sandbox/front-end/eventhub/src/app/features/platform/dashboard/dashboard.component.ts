import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MetricCardComponent } from '../../../shared/components/metric-card/metric-card.component';
import { ActionCardComponent } from '../../../shared/components/action-card/action-card.component';
import { ChartPanelComponent, ChartDataPoint } from './components/chart-panel/chart-panel.component';

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
export class PlatformOwnerDashboardComponent { 
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
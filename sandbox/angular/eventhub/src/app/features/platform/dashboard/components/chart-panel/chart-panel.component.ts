import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface ChartDataPoint {
  label: string;
  value: number;
}

@Component({
  selector: 'app-chart-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chart-panel.component.html',
  styleUrl: './chart-panel.component.css'
})
export class ChartPanelComponent {
  @Input({ required: true }) title!: string;
  @Input({ required: true }) data: ChartDataPoint[] = [];
  @Input() yAxisTicks: number[] = [100, 75, 50, 25, 0];
}
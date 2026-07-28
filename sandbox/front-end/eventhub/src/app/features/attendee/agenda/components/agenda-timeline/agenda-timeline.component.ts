import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface AgendaItem {
  time: string;
  details: string;
}

@Component({
  selector: 'app-agenda-timeline',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h1 class="custom-section-title">Agenda</h1>
    <div class="d-flex flex-column gap-2 custom-agenda-timeline">
      <div 
        *ngFor="let item of items" 
        class="glass-agenda-card rounded-1 overflow-hidden"
      >
        <div class="agenda-time">{{ item.time }}</div>
        <div class="agenda-details text-white">{{ item.details }}</div>
      </div>
    </div>
  `,
  styleUrls: ['../../agenda.component.css']
})
export class AgendaTimelineComponent {
  @Input() items: AgendaItem[] = [];
}
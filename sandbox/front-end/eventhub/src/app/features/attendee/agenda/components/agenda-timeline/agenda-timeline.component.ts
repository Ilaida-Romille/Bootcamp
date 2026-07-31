import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiAgendaItem } from '../../../models/attendee.model';

@Component({
  selector: 'app-agenda-timeline',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h1 class="custom-section-title">Agenda</h1>
    <div *ngIf="items.length === 0" class="text-secondary small py-3">
      No agenda items available.
    </div>
    <div class="d-flex flex-column gap-2 custom-agenda-timeline">
      <div
        *ngFor="let item of items"
        class="glass-agenda-card rounded-1 overflow-hidden"
        [class.agenda-break]="item.isBreak">
        <div class="agenda-time">
          <span>{{ item.startDateTime | date:'h:mm a' }}</span>
          <span class="agenda-time-divider">–</span>
          <span>{{ item.endDateTime | date:'h:mm a' }}</span>
        </div>
        <div class="agenda-body">
          <div class="agenda-title text-white" [class.agenda-break-label]="item.isBreak">
            {{ item.title }}
            <span *ngIf="item.isBreak" class="agenda-break-badge">Break</span>
          </div>
          <div *ngIf="item.description" class="agenda-description">{{ item.description }}</div>
          <div class="agenda-meta d-flex gap-3 flex-wrap mt-1">
            <span *ngIf="item.speaker" class="agenda-meta-item">
              <i class="bi bi-person"></i> {{ item.speaker }}
            </span>
            <span *ngIf="item.location" class="agenda-meta-item">
              <i class="bi bi-geo-alt"></i> {{ item.location }}
            </span>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['../../agenda.component.css']
})
export class AgendaTimelineComponent {
  @Input() items: ApiAgendaItem[] = [];
}
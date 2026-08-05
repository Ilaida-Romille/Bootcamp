import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiAgendaItem } from '../../../models/attendee.model';

@Component({
  selector: 'app-agenda-timeline',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h1 class="custom-section-title">Agenda</h1>
    @if (items.length === 0) {
      <div class="text-secondary small py-3">
        No agenda items available.
      </div>
    }
    <div class="d-flex flex-column gap-2 custom-agenda-timeline">
      @for (item of items; track $index) {
        <div
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
              @if (item.isBreak) {
                <span class="agenda-break-badge">Break</span>
              }
            </div>
            @if (item.description) {
              <div class="agenda-description">{{ item.description }}</div>
            }
            <div class="agenda-meta d-flex gap-3 flex-wrap mt-1">
              @if (item.speaker) {
                <span class="agenda-meta-item">
                  <i class="bi bi-person"></i> {{ item.speaker }}
                </span>
              }
              @if (item.location) {
                <span class="agenda-meta-item">
                  <i class="bi bi-geo-alt"></i> {{ item.location }}
                </span>
              }
            </div>
          </div>
        </div>
      }
    </div>
  `,
  styleUrls: ['../../agenda.component.css']
})
export class AgendaTimelineComponent {
  @Input() items: ApiAgendaItem[] = [];
}
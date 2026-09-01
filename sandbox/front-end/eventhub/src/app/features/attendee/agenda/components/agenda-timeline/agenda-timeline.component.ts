import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgendaResponseDto } from '../../../services/events-data.service';

@Component({
  selector: 'app-agenda-timeline',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h1 class="custom-section-title">Agenda</h1>
    @if (agendas.length === 0) {
      <div class="text-secondary small py-3">
        No agenda items available.
      </div>
    }
    @for (agenda of agendas; track agenda.id) {
      <div class="agenda-day-block">
        <div class="agenda-day-header">
          <span class="agenda-day-label">{{ agenda.agendaDate | date:'EEEE, MMMM d' }}</span>
          @if (agenda.title) {
            <span class="agenda-day-title">— {{ agenda.title }}</span>
          }
        </div>
        <div class="custom-agenda-timeline d-flex flex-column gap-2">
          @for (track of agenda.tracks; track track.id) {
            @if (track.sessions.length > 0) {
              <div class="glass-agenda-card agenda-track-row rounded-1 overflow-hidden">
                <div class="agenda-track-label">
                  <span class="agenda-track-name">{{ track.name }}</span>
                </div>
                <div class="agenda-track-sessions">
                  @for (session of track.sessions; track session.id) {
                    <div class="agenda-session-card">
                      <div class="agenda-session-time">
                        {{ session.startTime | date:'h:mm a' }}&nbsp;&ndash;&nbsp;{{ session.endTime | date:'h:mm a' }}
                      </div>
                      <div class="agenda-session-title">{{ session.title }}</div>
                      @if (session.locationOrRoom) {
                        <div class="agenda-meta-item">
                          <i class="bi bi-geo-alt"></i> {{ session.locationOrRoom }}
                        </div>
                      }
                    </div>
                  }
                </div>
              </div>
            }
          }
        </div>
      </div>
    }
  `,
  styleUrls: ['../../agenda.component.css']
})
export class AgendaTimelineComponent {
  @Input() agendas: AgendaResponseDto[] = [];
}
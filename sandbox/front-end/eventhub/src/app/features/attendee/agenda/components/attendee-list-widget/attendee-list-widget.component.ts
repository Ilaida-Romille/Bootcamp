import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface Attendee {
  id: string;
  name: string;
  company: string;
  email: string;
}

@Component({
  selector: 'app-attendee-list-widget',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="custom-widget-card mb-4">
      <h2 class="custom-section-title widget-title fs-6">Attendees</h2>
      <div class="glass-attendees-box rounded-3 overflow-hidden">
        <ul class="list-unstyled d-flex flex-column gap-3 mb-4 custom-attendee-list">
          <li *ngFor="let attendee of attendees" class="d-flex align-items-center gap-3 custom-attendee-item">
            <div class="avatar-placeholder">
              <svg class="attendee-avatar-img" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
              </svg>
            </div>
            <div class="d-flex flex-column overflow-hidden">
              <span class="text-white text-truncate fw-semibold" style="font-size: 0.85rem;">
                {{ attendee.name }}
              </span>
              <span class="text-secondary text-truncate" style="font-size: 0.75rem;">
                {{ attendee.company }}
              </span>
            </div>
          </li>
          <li *ngIf="attendees.length === 0" class="text-secondary small py-2">
            No other attendees registered yet.
          </li>
        </ul>
        <button 
          type="button" 
          class="btn btn-dark fw-semibold custom-widget-btn d-flex justify-content-center align-items-center"
          (click)="onConnectClick()"
        >
          View All &amp; Connect
        </button>
      </div>
    </div>
  `,
  styleUrls: ['../../agenda.component.css']
})
export class AttendeeListWidgetComponent {
  @Input() attendees: Attendee[] = [];

  onConnectClick(): void {
    // Action handler for connecting
  }
}
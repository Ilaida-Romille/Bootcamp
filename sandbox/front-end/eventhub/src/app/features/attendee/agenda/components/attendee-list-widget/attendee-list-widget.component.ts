import { Component, Input, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisteredAttendee } from '../../../models/attendee.model';
import { AttendeesModalComponent } from './attendees-modal.component';

@Component({
  selector: 'app-attendee-list-widget',
  standalone: true,
  imports: [CommonModule, AttendeesModalComponent],
  template: `
    <div class="custom-widget-card mb-4">
      <h2 class="custom-section-title widget-title fs-6">Attendees</h2>
      <div class="glass-attendees-box rounded-3 overflow-hidden">
        <ul class="list-unstyled d-flex flex-column gap-3 mb-4 custom-attendee-list">
          <li *ngFor="let attendee of displayedAttendees" class="d-flex align-items-center gap-3 custom-attendee-item">
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

          <!-- Show "n more" badge if attendees exceed limit -->
          <li *ngIf="hasMoreAttendees" class="d-flex align-items-center gap-3 custom-attendee-item">
            <div class="avatar-placeholder more-badge">
              <svg class="attendee-avatar-img" viewBox="0 0 24 24" fill="currentColor">
                <text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" font-size="12" font-weight="bold">
                  +{{ remainingCount }}
                </text>
              </svg>
            </div>
            <div class="d-flex flex-column overflow-hidden">
              <span class="text-white text-truncate fw-semibold" style="font-size: 0.85rem;">
                {{ remainingCount }} More Attendees
              </span>
              <span class="text-secondary text-truncate" style="font-size: 0.75rem;">
                Click to view all
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
          (click)="onViewAllClick()">
          View All &amp; Connect
        </button>
      </div>
    </div>

    <app-attendees-modal #attendeesModal [attendees]="attendees"></app-attendees-modal>
  `,
  styleUrls: ['../../agenda.component.css']
})
export class AttendeeListWidgetComponent {
  @Input() attendees: RegisteredAttendee[] = [];
  @ViewChild('attendeesModal') attendeesModal?: AttendeesModalComponent;

  readonly displayLimit = 3;

  get displayedAttendees(): RegisteredAttendee[] {
    return this.attendees.slice(0, this.displayLimit);
  }

  get hasMoreAttendees(): boolean {
    return this.attendees.length > this.displayLimit;
  }

  get remainingCount(): number {
    return Math.max(0, this.attendees.length - this.displayLimit);
  }

  onViewAllClick(): void {
    this.attendeesModal?.openModal();
  }
}

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
        <ul class="list-unstyled d-flex flex-column gap-2 mb-4 custom-attendee-list">

          @for (attendee of displayedAttendees; track attendee.id) {
            <li>
              <div class="d-flex align-items-center gap-3 custom-attendee-item">
                <div class="avatar-placeholder">
                  <svg class="attendee-avatar-img" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                  </svg>
                </div>
                <div class="d-flex flex-column overflow-hidden flex-grow-1">
                  <span class="text-white text-truncate fw-semibold" style="font-size: 0.85rem;">
                    {{ attendee.name }}
                  </span>
                  <span class="text-secondary text-truncate" style="font-size: 0.75rem;">
                    {{ attendee.company || 'Attendee' }}
                  </span>
                </div>
                <button
                  type="button"
                  class="card-toggle-btn"
                  [class.active]="selectedId === attendee.id"
                  (click)="toggleCard(attendee.id)"
                  title="View business card">
                  <i class="bi bi-person-vcard"></i>
                </button>
              </div>

              @if (selectedId === attendee.id) {
                <div class="mini-business-card">
                  <div class="mbc-accent"></div>
                  <div class="mbc-body">
                    <div class="mbc-initial">{{ attendee.name.charAt(0).toUpperCase() }}</div>
                    <div class="mbc-info">
                      <p class="mbc-name">{{ attendee.name }}</p>
                      @if (attendee.company) {
                        <p class="mbc-company">{{ attendee.company }}</p>
                      }
                      @if (attendee.email) {
                        <p class="mbc-email"><i class="bi bi-envelope me-1"></i>{{ attendee.email }}</p>
                      }
                    </div>
                  </div>
                  <div class="mbc-footer"><span class="mbc-dot"></span>EventHub</div>
                </div>
              }
            </li>
          }

          @if (hasMoreAttendees) {
            <li class="d-flex align-items-center gap-3 custom-attendee-item">
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
          }

          @if (attendees.length === 0) {
            <li class="text-secondary small py-2">
              No other attendees registered yet.
            </li>
          }

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
  selectedId: string | null = null;

  get displayedAttendees(): RegisteredAttendee[] {
    return this.attendees.slice(0, this.displayLimit);
  }

  get hasMoreAttendees(): boolean {
    return this.attendees.length > this.displayLimit;
  }

  get remainingCount(): number {
    return Math.max(0, this.attendees.length - this.displayLimit);
  }

  toggleCard(id: string): void {
    this.selectedId = this.selectedId === id ? null : id;
  }

  onViewAllClick(): void {
    this.attendeesModal?.openModal();
  }
}

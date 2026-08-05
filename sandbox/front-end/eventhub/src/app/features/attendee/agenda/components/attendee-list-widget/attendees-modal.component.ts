import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisteredAttendee } from '../../../models/attendee.model';

declare var bootstrap: any;

@Component({
  selector: 'app-attendees-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="modal fade" id="attendeesModal" tabindex="-1" aria-labelledby="attendeesModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable modal-lg">
        <div class="modal-content bg-dark text-light">
          <div class="modal-header border-secondary">
            <h5 class="modal-title fw-bold" id="attendeesModalLabel">
              Registered Attendees ({{ attendees.length }})
            </h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            @if (attendees.length === 0) {
              <div class="text-center text-muted py-4">
                No attendees registered yet.
              </div>
            } @else {
              <ul class="list-unstyled d-flex flex-column gap-3">
                @for (attendee of attendees; track attendee.id) {
                  <li class="d-flex align-items-start gap-3 pb-3 border-bottom border-secondary">
                    <div class="avatar-placeholder flex-shrink-0">
                      <svg class="attendee-avatar-img" viewBox="0 0 24 24" fill="currentColor" style="width: 40px; height: 40px;">
                        <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                      </svg>
                    </div>
                    <div class="flex-grow-1 overflow-hidden">
                      <p class="mb-1 text-white fw-semibold text-truncate">{{ attendee.name }}</p>
                      <p class="mb-1 text-secondary small text-truncate">{{ attendee.company }}</p>
                      <p class="text-secondary small text-truncate">{{ attendee.email }}</p>
                    </div>
                  </li>
                }
              </ul>
            }
          </div>
        </div>
      </div>
    </div>
  `
})
export class AttendeesModalComponent {
  @Input() attendees: RegisteredAttendee[] = [];

  openModal(): void {
    const modalElement = document.getElementById('attendeesModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }
}

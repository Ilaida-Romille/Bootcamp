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
              <ul class="list-unstyled d-flex flex-column gap-2">
                @for (attendee of attendees; track attendee.id) {
                  <li class="pb-2 border-bottom border-secondary">
                    <div class="d-flex align-items-center gap-3">
                      <div class="avatar-placeholder flex-shrink-0">
                        <svg class="attendee-avatar-img" viewBox="0 0 24 24" fill="currentColor" style="width: 40px; height: 40px;">
                          <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                        </svg>
                      </div>
                      <div class="flex-grow-1 overflow-hidden">
                        <p class="mb-0 text-white fw-semibold text-truncate" style="font-size:0.9rem;">{{ attendee.name }}</p>
                        @if (attendee.company) {
                          <p class="mb-0 text-secondary small text-truncate">{{ attendee.company }}</p>
                        }
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
                      <div class="mini-business-card mt-2">
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
              </ul>
            }
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card-toggle-btn {
      background: none;
      border: 1px solid rgba(100,181,246,0.2);
      border-radius: 6px;
      color: rgba(100,181,246,0.55);
      font-size: 1rem;
      padding: 0.25rem 0.45rem;
      cursor: pointer;
      flex-shrink: 0;
      transition: all 0.18s ease;
    }
    .card-toggle-btn:hover, .card-toggle-btn.active {
      background: rgba(100,181,246,0.12);
      border-color: rgba(100,181,246,0.5);
      color: #64b5f6;
    }
    .mini-business-card {
      border-radius: 8px;
      overflow: hidden;
      background: linear-gradient(135deg, rgba(100,181,246,0.07) 0%, rgba(255,255,255,0.02) 100%);
      border: 1px solid rgba(100,181,246,0.18);
      animation: slideDown 0.2s ease;
    }
    @keyframes slideDown {
      from { opacity: 0; transform: translateY(-6px); }
      to   { opacity: 1; transform: translateY(0); }
    }
    .mbc-accent { height: 3px; background: linear-gradient(90deg,#64b5f6,#1e88e5); }
    .mbc-body { display: flex; align-items: center; gap: 0.9rem; padding: 0.75rem 1rem; }
    .mbc-initial {
      width: 44px; height: 44px; border-radius: 50%; flex-shrink: 0;
      background: linear-gradient(135deg,#1565c0,#0d47a1);
      border: 2px solid rgba(100,181,246,0.3);
      display: flex; align-items: center; justify-content: center;
      font-size: 1.1rem; font-weight: 700; color: #90caf9;
    }
    .mbc-info { flex: 1; overflow: hidden; }
    .mbc-name { font-size: 0.95rem; font-weight: 700; color: #e8eaf0; margin: 0 0 0.1rem; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .mbc-company { font-size: 0.8rem; font-weight: 600; color: #64b5f6; margin: 0 0 0.1rem; }
    .mbc-email { font-size: 0.78rem; color: #8b9bb4; margin: 0; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .mbc-footer { padding: 0.35rem 1rem; border-top: 1px solid rgba(255,255,255,0.06); font-size: 0.67rem; font-weight: 700; letter-spacing: 0.08em; color: rgba(100,181,246,0.4); display: flex; align-items: center; gap: 0.35rem; }
    .mbc-dot { width: 5px; height: 5px; border-radius: 50%; background: #64b5f6; }
  `]
})
export class AttendeesModalComponent {
  @Input() attendees: RegisteredAttendee[] = [];
  selectedId: string | null = null;

  openModal(): void {
    const modalElement = document.getElementById('attendeesModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  toggleCard(id: string): void {
    this.selectedId = this.selectedId === id ? null : id;
  }
}

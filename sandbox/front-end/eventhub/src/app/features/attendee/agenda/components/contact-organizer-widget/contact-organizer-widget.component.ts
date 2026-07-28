import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contact-organizer-widget',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="custom-widget-card glass-contact-card p-4 rounded-3 text-start">
      <p class="small text-secondary mb-4">Have a question for the organizer?</p>
      <button 
        type="button" 
        class="btn btn-glass-outline w-100 fw-semibold custom-widget-btn rounded-2"
        (click)="onMessageClick()"
      >
        Message the Organizer
      </button>
    </div>
  `,
  styleUrls: ['../../agenda.component.css']
})
export class ContactOrganizerWidgetComponent {
  onMessageClick(): void {
    // Action handler for organizer contact
  }
}
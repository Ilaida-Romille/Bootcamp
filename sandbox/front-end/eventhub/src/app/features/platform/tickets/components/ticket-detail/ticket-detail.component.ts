import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Ticket } from '../../models/ticket.model';

@Component({
  selector: 'app-ticket-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ticket-detail.component.html',
  styleUrl: './ticket-detail.component.css'
})
export class TicketDetailComponent {
  @Input() ticket: Ticket | null = null;
  @Output() responseSubmitted = new EventEmitter<{ ticketId: string; response: string }>();
  @Output() statusChanged = new EventEmitter<{ ticketId: string; status: Ticket['status'] }>();

  userResponse: string = '';

  sendResponse(): void {
    if (!this.ticket || !this.userResponse.trim()) return;
    
    this.responseSubmitted.emit({
      ticketId: this.ticket.id,
      response: this.userResponse.trim()
    });
    
    this.userResponse = '';
  }

  markResolved(): void {
    if (!this.ticket) return;
    this.statusChanged.emit({
      ticketId: this.ticket.id,
      status: 'Resolved'
    });
  }
}
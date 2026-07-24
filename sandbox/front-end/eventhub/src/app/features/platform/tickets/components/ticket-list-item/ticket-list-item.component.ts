import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ticket } from '../../models/ticket.model';

@Component({
  selector: 'app-ticket-list-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ticket-list-item.component.html',
  styleUrl: './ticket-list-item.component.css'
})
export class TicketListItemComponent {
  @Input({ required: true }) ticket!: Ticket;
  @Input() isSelected: boolean = false;
  @Output() selectTicket = new EventEmitter<Ticket>();

  onSelect(): void {
    this.selectTicket.emit(this.ticket);
  }

  getPriorityBadgeClass(priority: string): string {
    switch (priority) {
      case 'High': return 'badge-danger';
      case 'Medium': return 'badge-warning';
      case 'Low': return 'badge-secondary';
      default: return 'badge-secondary';
    }
  }
}
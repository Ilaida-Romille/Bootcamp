import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarItem } from '../../../layout/sidebar/sidebar.component';
import { ROUTE_PATHS } from '../../../app.routes';
import { PLATFORM_ROUTE_PATHS } from '../platform.routes';
import { TicketListItemComponent } from './components/ticket-list-item/ticket-list-item.component';
import { TicketDetailComponent } from './components/ticket-detail/ticket-detail.component';
import { Ticket } from './models/ticket.model';

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [
    CommonModule, 
    TicketListItemComponent, 
    TicketDetailComponent
  ],
  templateUrl: './tickets.component.html',
  styleUrl: './tickets.component.css'
})
export class TicketsComponent implements OnInit {
  private readonly base = `/${ROUTE_PATHS.platformOwner}`;

  adminNavItems: SidebarItem[] = [
    { label: 'Dashboard', route: `${this.base}/${PLATFORM_ROUTE_PATHS.dashboard}` },
    { label: 'Organizers', route: `${this.base}/${PLATFORM_ROUTE_PATHS.organizers}` },
    { label: 'Billing & Invoices', route: `${this.base}/${PLATFORM_ROUTE_PATHS.billing}` },
    { label: 'Tickets & Requests', route: `${this.base}/${PLATFORM_ROUTE_PATHS.tickets}` }
  ];

  tickets: Ticket[] = [
    {
      id: '1',
      ticketNumber: 'TCK-1042',
      subject: "Can't edit event capacity",
      companyName: 'Acme Events Co.',
      openedTimeAgo: '3 days ago',
      status: 'Open',
      priority: 'High',
      message: "We're trying to raise our event capacity from 200 to 350 but the field won't save. Can someone check?"
    },
    {
      id: '2',
      ticketNumber: 'TCK-1041',
      subject: 'Billing period mismatch in PDF',
      companyName: 'Global Summit Co.',
      openedTimeAgo: '5 days ago',
      status: 'In Progress',
      priority: 'Medium',
      message: 'The printed PDF invoice shows May 2026 instead of April 2026 for invoice #INV-2026-0002.'
    },
    {
      id: '3',
      ticketNumber: 'TCK-1039',
      subject: 'Requesting API key access',
      companyName: 'Tech Events Inc.',
      openedTimeAgo: '1 week ago',
      status: 'Resolved',
      priority: 'Low',
      message: 'We need API access enabled for our automated integration system.'
    }
  ];

  selectedTicket: Ticket | null = null;

  ngOnInit(): void {
    if (this.tickets.length > 0) {
      this.selectedTicket = this.tickets[0];
    }
  }

  selectTicket(ticket: Ticket): void {
    this.selectedTicket = ticket;
  }

  handleResponseSubmitted(event: { ticketId: string; response: string }): void {
    const target = this.tickets.find(t => t.id === event.ticketId);
    if (target) {
      target.status = 'In Progress';
      // System logic to append response or notify backend
    }
  }

  handleStatusChanged(event: { ticketId: string; status: Ticket['status'] }): void {
    const target = this.tickets.find(t => t.id === event.ticketId);
    if (target) {
      target.status = event.status;
    }
  }
}
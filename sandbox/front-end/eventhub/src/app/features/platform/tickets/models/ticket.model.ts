export type TicketStatus = 'Open' | 'In Progress' | 'Resolved' | 'Closed';
export type TicketPriority = 'High' | 'Medium' | 'Low';

export interface Ticket {
  id: string;
  ticketNumber: string;
  subject: string;
  companyName: string;
  openedTimeAgo: string;
  status: TicketStatus;
  priority: TicketPriority;
  message: string;
}
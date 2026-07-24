export interface Invoice {
  invoiceNumber: string;
  organizerName: string;
  organizerEmail: string;
  period: string;
  issueDate: string;
  dueDate: string;
  attendeesCount: number;
  ratePerAttendee: number;
  amount: number;
  status: 'Paid' | 'Pending' | 'Overdue';
}

export interface BillingFilterCriteria {
  searchTerm: string;
  fromMonth: string;
  toMonth: string;
}

export interface BatchSummary {
  period: string;
  organizersCount: number;
  totalAttendees: number;
  totalAmount: number;
  invoices: Invoice[];
}
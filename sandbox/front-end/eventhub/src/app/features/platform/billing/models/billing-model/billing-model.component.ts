export interface Invoice {
  id: string;
  organizationId: number;
  invoiceNumber: string;
  organizerId: string;
  organizerName: string;
  organizerEmail: string;
  period: string;
  monthCode: string;
  issueDate: string;
  dueDate: string;
  attendeesCount: number;
  ratePerAttendee: number;
  amount: number;
  status: 'Paid' | 'Pending' | 'Overdue';
}

export interface ApiInvoiceLineItem {
  eventId: number;
  eventTitle: string;
  eventAttendeeCount: number;
  rateApplied: number;
  lineTotal: number;
}

export interface ApiInvoiceResponse {
  id: number;
  organizationId: number;
  organizationName: string;
  organizerEmail: string;
  invoiceNumber: string;
  billingPeriodStart: string;
  billingPeriodEnd: string;
  totalAttendeeCount: number;
  appliedRatePerAttendee: number;
  invoiceAmount: number;
  paymentStatus: 'UNPAID' | 'PAID' | 'OVERDUE';
  issuedAt: string;
  dueDate: string;
  items: ApiInvoiceLineItem[];
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

export interface BillingOrganizer {
  id: string;
  name: string;
  email: string;
}

export interface BillingMonth {
  code: string;
  name: string;
}
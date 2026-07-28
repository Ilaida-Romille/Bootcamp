export interface Invoice {
  id: string;
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

export interface BillingSourceInvoice {
  id: string;
  invoiceNumber: string;
  organizerId: string;
  organizerName: string;
  billingPeriod: string;
  monthCode: string;
  attendeeCount: number;
  ratePerAttendee: number;
  status: 'Paid' | 'Pending' | 'Overdue';
}

export interface BillingDataResponse {
  currency: string;
  currencySymbol: string;
  organizers: BillingOrganizer[];
  months: BillingMonth[];
  invoices: BillingSourceInvoice[];
}

export interface BillingFeatureData {
  currency: string;
  currencySymbol: string;
  organizers: BillingOrganizer[];
  months: BillingMonth[];
  invoices: Invoice[];
}
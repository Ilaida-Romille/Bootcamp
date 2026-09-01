import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ApiInvoiceResponse, Invoice } from '../models/billing-model/billing-model.component';

@Injectable({ providedIn: 'root' })
export class BillingDataService {
  private readonly baseUrl = '/api/billing';

  private readonly http = inject(HttpClient);

  getAllInvoices(): Observable<Invoice[]> {
    return this.http.get<ApiInvoiceResponse[]>(`${this.baseUrl}/invoices`).pipe(
      map((invoices) => invoices.map((source) => this.mapApiInvoice(source)))
    );
  }

  generateInvoice(organizationId: number, periodStart: string, periodEnd: string): Observable<Invoice> {
    const params = new HttpParams()
      .set('organizationId', String(organizationId))
      .set('periodStart', periodStart)
      .set('periodEnd', periodEnd);
    return this.http.post<ApiInvoiceResponse>(`${this.baseUrl}/invoices/generate`, null, { params }).pipe(
      map((source) => this.mapApiInvoice(source))
    );
  }

  generateBatchInvoices(periodStart: string, periodEnd: string): Observable<Invoice[]> {
    const params = new HttpParams()
      .set('periodStart', periodStart)
      .set('periodEnd', periodEnd);
    return this.http.post<ApiInvoiceResponse[]>(`${this.baseUrl}/invoices/generate/batch`, null, { params }).pipe(
      map((invoices) => invoices.map((source) => this.mapApiInvoice(source)))
    );
  }

  private mapApiInvoice(source: ApiInvoiceResponse): Invoice {
    let status: 'Paid' | 'Pending' | 'Overdue' = 'Pending';
    if (source.paymentStatus === 'PAID') {
      status = 'Paid';
    } else if (source.paymentStatus === 'OVERDUE') {
      status = 'Overdue';
    }

    const monthCode = source.billingPeriodStart.slice(0, 7);
    const period = new Date(source.billingPeriodStart + 'T00:00:00')
      .toLocaleString('default', { month: 'long', year: 'numeric' });

    return {
      id: String(source.id),
      organizationId: source.organizationId,
      organizerId: String(source.organizationId),
      invoiceNumber: source.invoiceNumber,
      organizerName: source.organizationName,
      organizerEmail: source.organizerEmail,
      period,
      monthCode,
      issueDate: source.issuedAt.slice(0, 10),
      dueDate: source.dueDate,
      attendeesCount: source.totalAttendeeCount,
      ratePerAttendee: source.appliedRatePerAttendee,
      amount: source.invoiceAmount,
      status
    };
  }
}

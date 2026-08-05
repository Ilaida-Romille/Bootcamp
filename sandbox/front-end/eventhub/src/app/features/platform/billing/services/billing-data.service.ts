import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import {
  BillingDataResponse,
  BillingFeatureData,
  BillingOrganizer,
  BillingSourceInvoice,
  Invoice
} from '../models/billing-model/billing-model.component';

@Injectable({ providedIn: 'root' })
export class BillingDataService {
  private readonly dataUrl = '/data/invoices.json';

  private readonly http = inject(HttpClient);

  getBillingData(): Observable<BillingFeatureData> {
    return this.http.get<BillingDataResponse>(this.dataUrl).pipe(
      map((payload) => ({
        currency: payload.currency,
        currencySymbol: payload.currencySymbol,
        organizers: payload.organizers,
        months: payload.months,
        invoices: payload.invoices.map((source) => this.mapSourceInvoice(source, payload.organizers))
      }))
    );
  }

  private mapSourceInvoice(source: BillingSourceInvoice, organizers: BillingOrganizer[]): Invoice {
    const organizer = organizers.find((item) => item.id === source.organizerId);
    const issueDate = this.getMonthEndDate(source.monthCode);
    const dueDate = this.addDays(issueDate, 30);

    return {
      id: source.id,
      invoiceNumber: source.invoiceNumber,
      organizerId: source.organizerId,
      organizerName: source.organizerName,
      organizerEmail: organizer?.email || 'billing@eventhub.com',
      period: source.billingPeriod,
      monthCode: source.monthCode,
      issueDate,
      dueDate,
      attendeesCount: source.attendeeCount,
      ratePerAttendee: source.ratePerAttendee,
      amount: source.attendeeCount * source.ratePerAttendee,
      status: source.status
    };
  }

  private getMonthEndDate(monthCode: string): string {
    const [yearText, monthText] = monthCode.split('-');
    const year = Number(yearText);
    const month = Number(monthText);
    const endDate = new Date(year, month, 0);
    return endDate.toISOString().slice(0, 10);
  }

  private addDays(dateIso: string, days: number): string {
    const date = new Date(dateIso);
    date.setDate(date.getDate() + days);
    return date.toISOString().slice(0, 10);
  }
}

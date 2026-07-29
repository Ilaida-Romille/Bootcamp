import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Registration, RegisteredAttendee } from '../models/attendee.model';

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private readonly storageKey = 'eventhub_registrations';
  private registrationsSubject$ = new BehaviorSubject<Registration[]>([]);

  constructor() {
    this.loadRegistrationsFromStorage();
  }

  private loadRegistrationsFromStorage(): void {
    const stored = localStorage.getItem(this.storageKey);
    if (stored) {
      try {
        this.registrationsSubject$.next(JSON.parse(stored));
      } catch {
        this.registrationsSubject$.next([]);
      }
    }
  }

  registerAttendee(registration: Omit<Registration, 'id' | 'registeredAt'>): Registration {
    const newRegistration: Registration = {
      ...registration,
      id: `ATT-${Date.now().toString().slice(-6)}`,
      registeredAt: new Date().toISOString()
    };

    const current = this.registrationsSubject$.value;
    const updated = [...current, newRegistration];
    this.registrationsSubject$.next(updated);
    localStorage.setItem(this.storageKey, JSON.stringify(updated));

    return newRegistration;
  }

  getRegistrationsByEventId(eventId: string): Observable<RegisteredAttendee[]> {
    return new Observable((observer) => {
      this.registrationsSubject$.subscribe((registrations) => {
        const eventRegistrations = registrations
          .filter((reg) => reg.eventId === eventId)
          .map((reg) => ({
            id: reg.id,
            name: reg.fullName,
            company: reg.companyDept,
            email: reg.emailAddress,
            eventId: reg.eventId
          }));
        observer.next(eventRegistrations);
        observer.complete();
      });
    });
  }

  isEmailRegisteredForEvent(email: string, eventId: string): boolean {
    return this.registrationsSubject$.value.some(
      (reg) => reg.emailAddress.toLowerCase() === email.toLowerCase() && reg.eventId === eventId
    );
  }
}

import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface NavbarContext {
  companyName: string | null;
  userName: string | null;
  eventName: string | null;
  currentPageKey: 'events' | 'registration' | 'agenda' | 'profile' | null;
}

@Injectable({ providedIn: 'root' })
export class NavbarContextService {
  private readonly contextSubject$ = new BehaviorSubject<NavbarContext>({
    companyName: null,
    userName: null,
    eventName: null,
    currentPageKey: null
  });

  /**
   * Update navbar context
   */
  updateContext(context: Partial<NavbarContext>): void {
    const current = this.contextSubject$.value;
    this.contextSubject$.next({ ...current, ...context });
  }

  /**
   * Get navbar context as observable
   */
  getContext(): Observable<NavbarContext> {
    return this.contextSubject$.asObservable();
  }

  /**
   * Get current context value synchronously
   */
  getCurrentContext(): NavbarContext {
    return this.contextSubject$.value;
  }

  /**
   * Set company name
   */
  setCompanyName(name: string | null): void {
    this.updateContext({ companyName: name });
  }

  /**
   * Set user name
   */
  setUserName(name: string | null): void {
    this.updateContext({ userName: name });
  }

  /**
   * Set event name (for agenda page)
   */
  setEventName(name: string | null): void {
    this.updateContext({ eventName: name });
  }

  /**
   * Set current page key to show appropriate nav item
   */
  setCurrentPage(page: 'events' | 'registration' | 'agenda' | 'profile' | null): void {
    this.updateContext({ currentPageKey: page });
  }

  /**
   * Reset context
   */
  reset(): void {
    this.contextSubject$.next({
      companyName: null,
      userName: null,
      eventName: null,
      currentPageKey: null
    });
  }
}

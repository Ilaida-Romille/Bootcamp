import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { SessionService } from '../../core/services/session.service';
import { SessionUtilityService } from '../../core/services/session-utility.service';
import { NavbarContextService, NavbarContext } from '../../core/services/navbar-context.service';
import { ProfileDrawerService } from '../../core/services/profile-drawer.service';
import { UserRole, Session } from '../../core/models/session.model';
import { ROUTE_PATHS } from '../../app.routes';

interface NavItem {
  label: string;
  route: string | null;
}

const capitalize = (str: string): string => {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1);
};


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {
  private readonly sessionService = inject(SessionService);
  private readonly sessionUtility = inject(SessionUtilityService);
  private readonly navbarContext = inject(NavbarContextService);
  private readonly profileDrawer = inject(ProfileDrawerService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  session$!: Observable<Session | null>;
  navbarContext$!: Observable<NavbarContext>;
  navItems: NavItem[] = [];
  currentRole: UserRole | null = null;
  dropdownOpen = false;
  isOnLandingPage = false;

  ngOnInit(): void {
    this.session$ = this.sessionService.getSession();
    this.navbarContext$ = this.navbarContext.getContext();

    // Subscribe to session to update navbar based on role
    this.session$.subscribe((session) => {
      this.currentRole = session?.user.role ?? null;
      this.updateNavItems();
      this.cdr.markForCheck();
    });

    // Subscribe to context changes to update nav items
    this.navbarContext$.subscribe(() => {
      this.updateNavItems();
      this.cdr.markForCheck();
    });

    // Subscribe to route changes to update navbar when navigating
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.updateNavItems();
      this.cdr.markForCheck();
    });
  }

  /**
   * Update nav items based on current role and context
   */
  private updateNavItems(): void {
    const context = this.navbarContext.getCurrentContext();
    const landingPageLinks: NavItem[] = [
      { label: 'About', route: null },
      { label: 'How it Works', route: null },
      { label: 'Contact', route: null }
    ];

    // If on landing page, always show landing page links regardless of session state
    if (this.router.url === '' || this.router.url === '/') {
      this.navItems = landingPageLinks;
      this.isOnLandingPage = true;
      return;
    }

    // Otherwise, show role-based nav items
    this.isOnLandingPage = false;
    const effectiveRole = this.currentRole ?? this.resolveRoleFromUrl(this.router.url);

    switch (effectiveRole) {
      case 'organizer':
        this.navItems = this.getOrganizerNavItems();
        break;
      case 'platformOwner':
        this.navItems = this.getPlatformOwnerNavItems();
        break;
      case 'attendee':
        this.navItems = this.getAttendeeNavItems(context);
        break;
      default:
        this.navItems = landingPageLinks;
    }
  }

  /**
   * Fallback role resolution from URL (helps during initial hydration before session emits).
   */
  private resolveRoleFromUrl(url: string): UserRole | null {
    if (url.startsWith(`/${ROUTE_PATHS.organizer}`)) {
      return 'organizer';
    }

    if (url.startsWith(`/${ROUTE_PATHS.platformOwner}`)) {
      return 'platformOwner';
    }

    if (url.startsWith(`/${ROUTE_PATHS.attendee}`)) {
      return 'attendee';
    }

    return null;
  }

  /**
   * Get nav items for organizer role
   */
  private getOrganizerNavItems(): NavItem[] {
    const companyName = this.navbarContext.getCurrentContext().companyName || 'Organization';
    return [
      { label: companyName, route: null }
    ];
  }

  /**
   * Get nav items for platform owner role
   */
  private getPlatformOwnerNavItems(): NavItem[] {
    return [
      { label: 'Platform Owner', route: null }
    ];
  }

  /**
   * Get nav items for attendee role
   */
  private getAttendeeNavItems(context: NavbarContext): NavItem[] {
    const items: NavItem[] = [];

    // Add page navigation links based on current page
    switch (context.currentPageKey) {
      case 'events':
        items.push({ label: 'Upcoming Events', route: '/dashboard/events' });
        break;
      case 'registration':
        items.push({ label: 'Event Registration', route: '/dashboard/events' });
        break;
      case 'agenda':
        if (context.eventName) {
          items.push({ label: context.eventName, route: '/dashboard/events' });
        }
        break;
    }

    return items;
  }

  /**
   * Logout user
   */
  onLogout(): void {
    this.sessionUtility.logout();
    this.dropdownOpen = false;
  }

  openProfile(): void {
    this.dropdownOpen = false;
    this.profileDrawer.open();
  }

  /**
   * Toggle dropdown menu
   */
  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  /**
   * Get current user name for display
   */
  get userName(): string {
    return this.sessionService.getCurrentSession()?.user.name ?? 'User';
  }

  /**
   * Get display text for dropdown label
   */
  get dropdownLabel(): string {
    if (this.currentRole === 'platformOwner') {
      return 'Admin';
    }
    if (this.currentRole === 'organizer') {
      return 'Jane Dela Cruz';
    }
    return capitalize(this.userName);
  }
}
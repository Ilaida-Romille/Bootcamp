import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet, ActivationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { NavbarContextService } from '../../core/services/navbar-context.service';

@Component({
  selector: 'app-attendee-layout',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './attendee-layout.component.html',
  styleUrl: './attendee-layout.component.css',
})
export class AttendeeLayoutComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly navbarContext = inject(NavbarContextService);

  ngOnInit(): void {
    // Track route changes to update navbar context
    this.router.events
      .pipe(
        filter(event => event instanceof ActivationEnd)
      )
      .subscribe((event: any) => {
        const routePath = event.snapshot.routeConfig?.path;
        
        // Update current page based on route
        if (routePath === 'events') {
          this.navbarContext.setCurrentPage('events');
          this.navbarContext.setEventName(null);
        } else if (routePath?.includes('registration')) {
          this.navbarContext.setCurrentPage('registration');
          this.navbarContext.setEventName(null);
        } else if (routePath?.includes('agenda')) {
          this.navbarContext.setCurrentPage('agenda');
          // Event name will be set by agenda component
        }
      });
  }
}
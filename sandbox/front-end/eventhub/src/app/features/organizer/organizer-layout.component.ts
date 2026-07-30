import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../../layout/sidebar/sidebar.component';
import { NavbarContextService } from '../../core/services/navbar-context.service';
import { ORGANIZER_NAV_ITEMS } from './organizer.routes';

@Component({
  selector: 'app-organizer-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent],
  templateUrl: './organizer-layout.component.html',
  styleUrls: ['./organizer-layout.component.css']
})
export class OrganizerLayoutComponent implements OnInit {
  private readonly navbarContext = inject(NavbarContextService);

  navItems = ORGANIZER_NAV_ITEMS;
  
  organizerName = 'Jane Dela Cruz';
  companyName = 'Acme Events Co.';
  roleName = 'Organizer';

  ngOnInit(): void {
    // Set navbar context for organizer
    this.navbarContext.updateContext({
      companyName: this.companyName,
      userName: this.organizerName
    });
  }
}
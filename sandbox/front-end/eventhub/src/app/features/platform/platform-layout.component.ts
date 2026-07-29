import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../../layout/sidebar/sidebar.component';
import { PLATFORM_NAV_ITEMS } from './platform.routes';
import { NavbarContextService } from '../../core/services/navbar-context.service';

@Component({
  selector: 'app-platform-layout',
  standalone: true,
  imports: [SidebarComponent, RouterOutlet],
  templateUrl: './platform-layout.component.html',
  styleUrl: './platform-layout.component.css',
})
export class PlatformLayoutComponent implements OnInit {
  private readonly navbarContext = inject(NavbarContextService);
  
  navItems = PLATFORM_NAV_ITEMS;
  adminName = 'Jane Dela Cruz';

  ngOnInit(): void {
    // Set navbar context for platform owner
    this.navbarContext.updateContext({
      userName: this.adminName
    });
  }
}
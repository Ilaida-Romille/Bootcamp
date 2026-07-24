import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../../layout/sidebar/sidebar.component';
import { PLATFORM_NAV_ITEMS } from './platform.routes';

@Component({
  selector: 'app-platform-layout',
  standalone: true,
  imports: [SidebarComponent, RouterOutlet],
  templateUrl: './platform-layout.component.html',
  styleUrl: './platform-layout.component.css',
})
export class PlatformLayoutComponent {
  navItems = PLATFORM_NAV_ITEMS;
}
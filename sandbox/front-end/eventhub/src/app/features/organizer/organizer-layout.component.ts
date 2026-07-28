import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink } from '@angular/router';
import { OrganizerSidebarComponent } from './dashboard/components/organizer-sidebar/organizer-sidebar.component';

@Component({
  selector: 'app-organizer-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, OrganizerSidebarComponent],
  templateUrl: './organizer-layout.component.html',
  styleUrls: ['./organizer-layout.component.css']
})
export class OrganizerLayoutComponent {
  organizerName = 'Jane Dela Cruz';
  roleName = 'Platform Owner';
}
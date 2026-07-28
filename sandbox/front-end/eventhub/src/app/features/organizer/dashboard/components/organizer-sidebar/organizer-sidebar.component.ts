import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-organizer-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './organizer-sidebar.component.html',
  styleUrls: ['./organizer-sidebar.component.css']
})
export class OrganizerSidebarComponent {
  navItems = [
    { label: 'Dashboard', active: true },
    { label: 'Manage Employees', active: false },
    { label: 'Manage Events', active: false },
    { label: 'Settings', active: false },
    { label: 'Tickets / Requests', active: false }
  ];

  onDummyClick(event: Event): void {
    event.preventDefault(); // Prevents URL navigation / page jump
  }
}
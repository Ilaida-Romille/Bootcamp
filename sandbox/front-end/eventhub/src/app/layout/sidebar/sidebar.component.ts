import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

export interface SidebarItem {
  label: string;
  route: string;
  exact?: boolean; // Optional flag if route matching needs exact path
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Input({ required: true }) navItems: SidebarItem[] = [];
}
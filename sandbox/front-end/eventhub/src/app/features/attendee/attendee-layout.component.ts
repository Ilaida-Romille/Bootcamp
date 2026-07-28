import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-attendee-layout',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './attendee-layout.component.html',
  styleUrl: './attendee-layout.component.css',
})
export class AttendeeLayoutComponent {}
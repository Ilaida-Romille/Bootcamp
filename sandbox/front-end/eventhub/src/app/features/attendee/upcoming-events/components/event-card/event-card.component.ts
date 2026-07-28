import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventItem } from '../../../../../core/models/event.model';
import { RouterLink } from '@angular/router';
import { ATTENDEE_ROUTE_PATHS } from '../../../attendee.routes';

@Component({
  selector: 'app-event-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.css']
})
export class EventCardComponent {
  @Input({ required: true }) event!: EventItem;

  readonly ATTENDEE_PATHS = ATTENDEE_ROUTE_PATHS;
  
  // Controls pure 3D flip card toggle via state or unique CSS ID dynamically
  isFlipped = false;

  toggleFlip(): void {
    this.isFlipped = !this.isFlipped;
  }
}
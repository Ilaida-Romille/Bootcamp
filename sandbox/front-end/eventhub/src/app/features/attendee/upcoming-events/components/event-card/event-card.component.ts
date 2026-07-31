import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ATTENDEE_ROUTE_PATHS } from '../../../attendee.routes';
import { EventItemDisplay } from '../../../models/attendee.model';

@Component({
  selector: 'app-event-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.css']
})
export class EventCardComponent {
  @Input({ required: true }) event!: EventItemDisplay;

  readonly ATTENDEE_PATHS = ATTENDEE_ROUTE_PATHS;

  isFlipped = false;

  toggleFlip(): void {
    this.isFlipped = !this.isFlipped;
  }

  get capacityDisplay(): string {
    return `${this.event.capacity.registered} / ${this.event.capacity.maximum}`;
  }

  get canRegister(): boolean {
    return this.event.status === 'registration_open' && this.event.remainingSlots > 0;
  }
}
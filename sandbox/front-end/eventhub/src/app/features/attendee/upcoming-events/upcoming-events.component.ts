import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventCardComponent } from './components/event-card/event-card.component';
import { EventItem } from '../../../core/models/event.model';

@Component({
  selector: 'app-upcoming-events',
  standalone: true,
  imports: [CommonModule, EventCardComponent],
  templateUrl: './upcoming-events.component.html',
  styleUrls: ['./upcoming-events.component.css']
})
export class UpcomingEventsComponent implements OnInit {
  currentPage = 1;
  totalPages = 3;
  pages: number[] = [1, 2, 3];

  events: EventItem[] = [];

  ngOnInit(): void {
    // Populate dummy events (or fetch from API service)
    this.events = [
      {
        id: 1,
        title: 'Tech Summit 2026',
        date: 'October 12, 2026',
        organizer: 'Innovation Labs',
        category: 'Technology',
        capacity: '450 / 500',
        status: 'Filling Fast',
        statusClass: 'status-filling'
      },
      {
        id: 2,
        title: 'Global Leadership Conference',
        date: 'November 05, 2026',
        organizer: 'Enterprise Core',
        category: 'Management',
        capacity: '200 / 200',
        status: 'Full',
        statusClass: 'status-full'
      },
      {
        id: 3,
        title: 'AI & Future of Work Forum',
        date: 'December 01, 2026',
        organizer: 'Data Dynamics',
        category: 'AI & Data',
        capacity: '120 / 300',
        status: 'Open',
        statusClass: 'status-open'
      },
      {
        id: 4,
        title: 'Tech Summit 2026',
        date: 'October 12, 2026',
        organizer: 'Innovation Labs',
        category: 'Technology',
        capacity: '450 / 500',
        status: 'Filling Fast',
        statusClass: 'status-filling'
      },
      {
        id: 5,
        title: 'AI & Future of Work Forum',
        date: 'December 01, 2026',
        organizer: 'Data Dynamics',
        category: 'AI & Data',
        capacity: '120 / 300',
        status: 'Open',
        statusClass: 'status-open'
      },
      {
        id: 6,
        title: 'Global Leadership Conference',
        date: 'November 05, 2026',
        organizer: 'Enterprise Core',
        category: 'Management',
        capacity: '200 / 200',
        status: 'Full',
        statusClass: 'status-full'
      }
    ];
  }

  setPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      // Trigger pagination data load logic here
    }
  }
}
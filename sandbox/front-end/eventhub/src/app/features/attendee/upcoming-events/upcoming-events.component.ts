import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-upcoming-events',
  standalone: true,
  template: `
    <main class="container-xl py-5">
      <h1 class="h3 fw-bold text-white">Upcoming events</h1>
      <p class="text-secondary">Coming soon — this route is wired up but not built yet.</p>
    </main>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UpcomingEventsComponent { }
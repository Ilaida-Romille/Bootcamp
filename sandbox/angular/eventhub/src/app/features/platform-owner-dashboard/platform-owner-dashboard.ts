import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-attendee-dashboard',
  standalone: true,
  template: `
    <main class="container-xl py-5">
      <h1 class="h3 fw-bold text-white">Attendee dashboard</h1>
      <p class="text-secondary">Coming soon — this route is wired up but not built yet.</p>
    </main>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendeeDashboardComponent { }
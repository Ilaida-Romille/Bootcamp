import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

import { HeroComponent } from './components/hero/hero';
import { LoginFormComponent } from './components/login-form/login-form';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [HeroComponent, LoginFormComponent],
  templateUrl: './landing.html',
  styleUrl: './landing.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LandingComponent {
  /** Drives the split-screen reveal; replaces the manual classList.add('split-active') hack. */
  protected readonly started = signal(false);

  protected onGetStarted(): void {
    this.started.set(true);
  }
}
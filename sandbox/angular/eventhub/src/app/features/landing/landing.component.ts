import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

import { HeroComponent } from './components/hero/hero.component';
import { LoginFormComponent } from './components/login-form/login-form.component';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [HeroComponent, LoginFormComponent],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LandingComponent {
  /** Drives the split-screen reveal; replaces the manual classList.add('split-active') hack. */
  protected readonly started = signal(false);

  protected onGetStarted(): void {
    this.started.set(true);
  }
}
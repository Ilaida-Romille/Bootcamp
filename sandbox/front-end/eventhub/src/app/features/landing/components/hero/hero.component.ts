import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';

@Component({
  selector: 'app-hero',
  standalone: true,
  host: { class: 'col-12 col-md-6 col-lg-5' },
  templateUrl: './hero.component.html',
  styleUrl: './hero.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HeroComponent {
  /** Whether the login column has been revealed. Drives layout + the button's visibility. */
  readonly started = input(false);

  /** Emitted when the visitor clicks "Get Started". */
  readonly getStarted = output<void>();
}
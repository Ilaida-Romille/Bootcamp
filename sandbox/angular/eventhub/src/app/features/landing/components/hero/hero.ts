import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';

@Component({
  selector: 'app-hero',
  standalone: true,
  templateUrl: './hero.html',
  styleUrl: './hero.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HeroComponent {
  /** Whether the login column has been revealed. Drives layout + the button's visibility. */
  readonly started = input(false);

  /** Emitted when the visitor clicks "Get Started". */
  readonly getStarted = output<void>();
}
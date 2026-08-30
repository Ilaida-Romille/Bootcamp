import { ChangeDetectionStrategy, Component, inject, input, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthRoutingService } from '../../../../core/services/auth-routing.service';
import { SessionService } from '../../../../core/services/session.service';
import {
  EMAIL_MAX_LENGTH,
  EMAIL_MIN_LENGTH,
  likelyValidEmailValidator,
} from '../../../../core/validators/email.validator';
import {
  PASSWORD_MIN_LENGTH,
  PasswordStrengthErrors,
  describePasswordErrors,
  passwordStrengthValidator,
} from '../../../../core/validators/password-strength.validator';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login-form',
  standalone: true,
  host: { class: 'col-12 col-md-6 col-lg-5' },
  imports: [ReactiveFormsModule],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginFormComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly authRouting = inject(AuthRoutingService);
  private readonly sessionService = inject(SessionService);

  /** Whether the login column has been revealed by the hero's "Get Started" button. */
  readonly started = input(false);

  protected readonly emailMinLength = EMAIL_MIN_LENGTH;
  protected readonly emailMaxLength = EMAIL_MAX_LENGTH;
  protected readonly passwordMinLength = PASSWORD_MIN_LENGTH;

  /** Set when a submitted email doesn't match any registered corporate domain. */
  protected readonly accessDeniedMessage = signal<string | null>(null);

  protected readonly form = this.formBuilder.nonNullable.group({
    email: [
      '',
      [
        Validators.required,
        Validators.minLength(EMAIL_MIN_LENGTH),
        Validators.maxLength(EMAIL_MAX_LENGTH),
        likelyValidEmailValidator(),
      ],
    ],
    password: ['', [Validators.required, passwordStrengthValidator()]],
  });

  protected get email() {
    return this.form.controls.email;
  }

  protected get password() {
    return this.form.controls.password;
  }

  protected get passwordErrorMessage(): string {
    const errors = this.password.errors?.['passwordStrength'] as PasswordStrengthErrors | undefined;
    return errors ? describePasswordErrors(errors) : '';
  }

  protected onSubmit(): void {
    this.accessDeniedMessage.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { email, password } = this.form.getRawValue();

    this.sessionService.login({ email, password }).subscribe({
      next: () => {
        const role = this.sessionService.getCurrentUserRole();
        if (role) {
          const destination = this.authRouting.resolveRoute(role);
          if (destination) {
            this.router.navigateByUrl(destination);
            return;
          }
        }
        this.accessDeniedMessage.set('Unable to determine landing route for account.');
      },
      error: (err: HttpErrorResponse | Error) => {
        // Handle HTTP Response Errors
        if (err instanceof HttpErrorResponse) {
          if (err.status === 0) {
            this.accessDeniedMessage.set(
              'Unable to connect to the authentication server. Please check if the backend is running.'
            );
          } else if (err.status === 401 || err.status === 400) {
            this.accessDeniedMessage.set('Invalid credentials. Please check your email and password.');
          } else {
            this.accessDeniedMessage.set(`Server error (${err.status}). Please try again later.`);
          }
        } else {
          // Handle client-side runtime errors (e.g., JWT decode failure)
          this.accessDeniedMessage.set(err.message || 'An unexpected error occurred.');
        }
      }
    });
  }
}
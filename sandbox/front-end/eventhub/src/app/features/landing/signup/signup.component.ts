import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';

import {
  EMAIL_MAX_LENGTH,
  EMAIL_MIN_LENGTH,
  likelyValidEmailValidator,
} from '../../../core/validators/email.validator';
import {
  PASSWORD_MIN_LENGTH,
  PasswordStrengthErrors,
  describePasswordErrors,
  passwordStrengthValidator,
} from '../../../core/validators/password-strength.validator';

interface PublicOrganization {
  id: number;
  companyName: string;
}

function passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
  const password = group.get('password')?.value as string;
  const confirm = group.get('confirmPassword')?.value as string;
  return confirm && password !== confirm ? { passwordMismatch: true } : null;
}

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SignupComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly http = inject(HttpClient);

  protected readonly emailMinLength = EMAIL_MIN_LENGTH;
  protected readonly emailMaxLength = EMAIL_MAX_LENGTH;
  protected readonly passwordMinLength = PASSWORD_MIN_LENGTH;

  protected readonly activeRole = signal<'attendee' | 'organizer'>('attendee');
  protected readonly isSubmitting = signal(false);
  protected readonly registeredAs = signal<'attendee' | 'organizer' | null>(null);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly organizations = signal<PublicOrganization[]>([]);
  protected readonly orgLoadError = signal(false);

  protected readonly organizerForm = this.fb.nonNullable.group(
    {
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      lastName: ['', [Validators.required, Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.minLength(EMAIL_MIN_LENGTH), Validators.maxLength(EMAIL_MAX_LENGTH), likelyValidEmailValidator()]],
      password: ['', [Validators.required, passwordStrengthValidator()]],
      confirmPassword: ['', Validators.required],
      companyName: ['', [Validators.required, Validators.maxLength(255)]],
      primaryContactEmail: ['', [Validators.required, likelyValidEmailValidator(), Validators.maxLength(255)]],
      primaryContactPhone: ['', Validators.maxLength(30)],
    },
    { validators: passwordMatchValidator }
  );

  protected readonly attendeeForm = this.fb.nonNullable.group(
    {
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      lastName: ['', [Validators.required, Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.minLength(EMAIL_MIN_LENGTH), Validators.maxLength(EMAIL_MAX_LENGTH), likelyValidEmailValidator()]],
      password: ['', [Validators.required, passwordStrengthValidator()]],
      confirmPassword: ['', Validators.required],
      organizationId: ['', Validators.required],
    },
    { validators: passwordMatchValidator }
  );

  ngOnInit(): void {
    this.http.get<PublicOrganization[]>('/api/auth/organizations').subscribe({
      next: (orgs) => this.organizations.set(orgs),
      error: () => this.orgLoadError.set(true),
    });
  }

  protected selectRole(role: 'attendee' | 'organizer'): void {
    this.activeRole.set(role);
    this.errorMessage.set(null);
  }

  protected orgPasswordErrors(role: 'attendee' | 'organizer'): string {
    const ctrl = role === 'organizer'
      ? this.organizerForm.controls.password
      : this.attendeeForm.controls.password;
    const errors = ctrl.errors?.['passwordStrength'] as PasswordStrengthErrors | undefined;
    return errors ? describePasswordErrors(errors) : '';
  }

  protected onSubmit(): void {
    this.errorMessage.set(null);
    if (this.activeRole() === 'organizer') {
      this.submitOrganizer();
    } else {
      this.submitAttendee();
    }
  }

  private submitOrganizer(): void {
    if (this.organizerForm.invalid) {
      this.organizerForm.markAllAsTouched();
      return;
    }
    const { confirmPassword: _c, ...rest } = this.organizerForm.getRawValue();
    const payload = {
      firstName: this.sanitizeText(rest.firstName),
      lastName: this.sanitizeText(rest.lastName),
      email: this.sanitizeText(rest.email),
      password: rest.password,
      companyName: this.sanitizeText(rest.companyName),
      primaryContactEmail: this.sanitizeEmail(rest.primaryContactEmail),
      primaryContactPhone: this.sanitizePhone(rest.primaryContactPhone)
    }
    this.isSubmitting.set(true);
    this.http.post('/api/auth/register', payload, { observe: 'response' }).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.registeredAs.set('organizer');
      },
      error: (err: HttpErrorResponse) => {
        this.isSubmitting.set(false);
        this.errorMessage.set(this.parseError(err));
      },
    });
  }

  private submitAttendee(): void {
    if (this.attendeeForm.invalid) {
      this.attendeeForm.markAllAsTouched();
      return;
    }

    const raw = this.attendeeForm.getRawValue();

    const { confirmPassword: _c, organizationId, ...rest } = this.attendeeForm.getRawValue();
    const payload = {
      organizationId: Number(organizationId),
      firstName: this.sanitizeText(raw.firstName),
      lastName: this.sanitizeText(raw.lastName),
      email: this.sanitizeEmail(raw.email),
      password: raw.password, // Never modify password strings
    }
    this.isSubmitting.set(true);
    this.http.post('/api/auth/register/attendee', payload, { observe: 'response' }).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.registeredAs.set('attendee');
      },
      error: (err: HttpErrorResponse) => {
        this.isSubmitting.set(false);
        this.errorMessage.set(this.parseError(err));
      },
    });
  }

  private parseError(err: HttpErrorResponse): string {
    if (err.status === 0) {
      return 'Unable to connect to the server. Please check if the backend is running.';
    }
    if (err.status === 400 || err.status === 409) {
      const msg = err.error?.message ?? err.error;
      return typeof msg === 'string' ? msg : 'Please check your input and try again.';
    }
    return `Server error (${err.status}). Please try again later.`;
  }

  // Helper functions for input sanitization
  private sanitizeText(val: string): string {
    return val ? val.trim().replace(/<[^>]*>?/gm, '') : ''; // Trims & strips HTML tags
  }

  private sanitizeEmail(val: string): string {
    return val ? val.trim().toLowerCase() : ''; // Trims & standardizes casing
  }

  private sanitizePhone(val: string): string {
    return val ? val.replace(/[^\d+]/g, '') : ''; // Keeps only numbers and leading '+'
  }
}

import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ATTENDEE_ROUTE_PATHS } from '../attendee.routes';
import { EventsDataService } from '../services/events-data.service';
import { RegistrationService } from '../services/registration.service';
import { NavbarContextService } from '../../../core/services/navbar-context.service';
import { SessionService } from '../../../core/services/session.service';
import { EventDetail } from '../models/attendee.model';

export function corporateEmailValidator(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;
  const publicDomainsRegex = /@(gmail\.com|yahoo\.com|hotmail\.com|outlook\.com|aol\.com|icloud\.com|protonmail\.com|mail\.com)$/i;
  return publicDomainsRegex.test(control.value.trim()) ? { publicEmailNotAllowed: true } : null;
}

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly eventsDataService = inject(EventsDataService);
  private readonly registrationService = inject(RegistrationService);
  private readonly navbarContext = inject(NavbarContextService);
  private readonly sessionService = inject(SessionService);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly ATTENDEE_PATHS = ATTENDEE_ROUTE_PATHS;

  eventId: string | null = null;
  event: EventDetail | null = null;
  registrationForm!: FormGroup;
  isSubmitting = false;
  submissionError = '';

  ngOnInit(): void {
    this.initForm();
    this.readRouteParams();
  }

  private initForm(): void {
    const session = this.sessionService.getCurrentSession();
    const prefillEmail = session?.user.email ?? '';
    const prefillName = session?.user.name ?? '';

    this.registrationForm = this.fb.group({
      fullName: [prefillName, [Validators.required]],
      emailAddress: [
        prefillEmail,
        [
          Validators.required,
          Validators.email,
          Validators.minLength(15),
          Validators.maxLength(100),
          corporateEmailValidator
        ]
      ],
      companyDept: ['', [Validators.required]],
      dietary: [''],
      additionalNotes: ['']
    });
  }

  private readRouteParams(): void {
    this.route.paramMap.subscribe((params) => {
      this.eventId = params.get('id');
      if (this.eventId) {
        this.navbarContext.setCurrentPage('registration');
        this.navbarContext.setEventName(null);
        this.loadEventDetails();
      }
    });
  }

  private loadEventDetails(): void {
    if (!this.eventId) return;

    this.registrationService.getEventDetails(Number(this.eventId)).subscribe({
      next: (dto) => {
        this.event = this.eventsDataService.mapToDisplayEvent(dto);
        this.cdr.detectChanges();
      },
      error: () => {
        this.event = null;
        this.submissionError = 'Unable to load event details.';
        this.cdr.detectChanges();
      }
    });
  }

  get f() {
    return this.registrationForm.controls;
  }

  get isRegistrationOpen(): boolean {
    return this.event?.status === 'registration_open';
  }

  onSubmit(): void {
    if (this.registrationForm.invalid || !this.eventId) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.submissionError = '';

    this.registrationService.register(Number(this.eventId)).subscribe({
      next: () => {
        this.router.navigate(['/dashboard', ATTENDEE_ROUTE_PATHS.agenda, this.eventId]);
      },
      error: (err) => {
        this.isSubmitting = false;
        if (err.status === 409) {
          this.submissionError = 'You are already registered for this event.';
        } else if (err.status === 400) {
          this.submissionError = err.error?.message ?? 'Invalid registration request.';
        } else if (err.status === 403) {
          this.submissionError = 'You do not have permission to register for this event.';
        } else {
          this.submissionError = 'Registration failed. Please try again.';
        }
        this.cdr.detectChanges();
      }
    });
  }
}
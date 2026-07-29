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
import { EventDetail } from '../models/attendee.model';

// Custom validator to disallow public webmail domains
export function corporateEmailValidator(control: AbstractControl): ValidationErrors | null {
  if (!control.value) {
    return null;
  }
  const publicDomainsRegex = /@(gmail\.com|yahoo\.com|hotmail\.com|outlook\.com|aol\.com|icloud\.com|protonmail\.com|mail\.com)$/i;
  const isPublicDomain = publicDomainsRegex.test(control.value.trim());
  
  return isPublicDomain ? { publicEmailNotAllowed: true } : null;
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
    this.registrationForm = this.fb.group({
      fullName: ['', [Validators.required]],
      emailAddress: [
        '', 
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
        this.loadEventDetails();
      }
    });
  }

  private loadEventDetails(): void {
    if (!this.eventId) return;

    this.eventsDataService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.event = event;
        this.cdr.detectChanges();
      },
      error: () => {
        this.event = null;
        this.submissionError = 'Unable to load event details.';
        this.cdr.detectChanges();
      }
    });
  }

  // Convenience getter for form controls
  get f() {
    return this.registrationForm.controls;
  }

  onSubmit(): void {
    if (this.registrationForm.invalid || !this.eventId) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    const email = this.registrationForm.value.emailAddress.trim().toLowerCase();

    // Check for duplicate registration
    if (this.registrationService.isEmailRegisteredForEvent(email, this.eventId)) {
      this.submissionError = 'This email is already registered for this event.';
      return;
    }

    this.isSubmitting = true;
    this.submissionError = '';

    this.registrationService.registerAttendee({
      eventId: this.eventId,
      fullName: this.registrationForm.value.fullName.trim(),
      emailAddress: email,
      companyDept: this.registrationForm.value.companyDept.trim(),
      dietary: this.registrationForm.value.dietary.trim(),
      additionalNotes: this.registrationForm.value.additionalNotes.trim()
    });

    // Navigate to agenda with event ID using absolute path
    this.router.navigate(['/dashboard', ATTENDEE_ROUTE_PATHS.agenda, this.eventId]);
  }
}
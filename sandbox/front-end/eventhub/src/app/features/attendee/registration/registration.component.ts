import { Component, OnInit, inject } from '@angular/core';
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
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  readonly ATTENDEE_PATHS = ATTENDEE_ROUTE_PATHS;

  eventId: string | null = null;
  eventTitle = 'Loading Event...';
  eventDate = '---';
  eventLocation = '---';

  registrationForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
    this.readQueryParams();
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

  private readQueryParams(): void {
    this.route.queryParamMap.subscribe((params) => {
      this.eventId = params.get('eventId') || params.get('id') || '1';
      this.eventTitle = params.get('title') || 'Tech Summit 2026';
      this.eventDate = params.get('date') || 'October 12, 2026';
      this.eventLocation = params.get('location') || 'Main Auditorium';
    });
  }

  // Convenience getter for form controls
  get f() {
    return this.registrationForm.controls;
  }

  onSubmit(): void {
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    if (!this.eventId) {
      console.error('No Event ID found.');
      return;
    }

    const formValues = this.registrationForm.value;
    const email = formValues.emailAddress.trim().toLowerCase();

    // Retrieve existing registrants array from localStorage
    const existingRegistrantsRaw = localStorage.getItem('eventHub_registrants');
    let registrants: any[] = existingRegistrantsRaw ? JSON.parse(existingRegistrantsRaw) : [];

    // Check if email already registered for this event
    const isAlreadyRegistered = registrants.some(
      (attendee) => attendee.email.toLowerCase() === email && attendee.eventId === this.eventId
    );

    if (!isAlreadyRegistered) {
      const newRegistrant = {
        id: `ATT-${Date.now().toString().slice(-4)}`,
        name: formValues.fullName.trim(),
        email: email,
        company: formValues.companyDept.trim(),
        dietaryRestrictions: formValues.dietary.trim(),
        additionalNotes: formValues.additionalNotes.trim(),
        eventId: this.eventId,
        registeredAt: new Date().toISOString()
      };

      registrants.push(newRegistrant);
      localStorage.setItem('eventHub_registrants', JSON.stringify(registrants));
    }

    // Set current active event ID
    localStorage.setItem('eventHub_currentEventId', this.eventId);

    // Navigate to Agenda page
    this.router.navigate(['../', ATTENDEE_ROUTE_PATHS.agenda], { relativeTo: this.route });
  }
}
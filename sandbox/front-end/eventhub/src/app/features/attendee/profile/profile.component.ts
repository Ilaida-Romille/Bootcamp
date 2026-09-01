import { Component, OnInit, inject, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfileService, UserProfileDto } from '../services/profile.service';
import { NavbarContextService } from '../../../core/services/navbar-context.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  private readonly profileService = inject(ProfileService);
  private readonly navbarContext = inject(NavbarContextService);
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  profile: UserProfileDto | null = null;
  profileForm!: FormGroup;
  selectedFile: File | null = null;

  isLoadingProfile = false;
  isSavingProfile = false;
  isUploadingPicture = false;

  imagePreviewUrl: string | null = null;
  saveError = '';
  saveSuccess = false;
  uploadError = '';

  ngOnInit(): void {
    this.navbarContext.setCurrentPage('profile');
    this.navbarContext.setEventName(null);
    this.initForm();
    this.loadProfile();
  }

  private initForm(): void {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      lastName: ['', [Validators.required, Validators.maxLength(100)]],
      company: ['', [Validators.maxLength(255)]],
      dietary: ['', [Validators.maxLength(255)]]
    });
  }

  private loadProfile(): void {
    this.isLoadingProfile = true;
    this.profileService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.imagePreviewUrl = profile.profileImageUrl;
        const [firstName, ...rest] = (profile.fullName ?? '').split(' ');
        this.profileForm.patchValue({
          firstName: firstName ?? '',
          lastName: rest.join(' '),
          company: profile.company ?? '',
          dietary: profile.dietary ?? ''
        });
        this.isLoadingProfile = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.isLoadingProfile = false;
        this.cdr.detectChanges();
      }
    });
  }

  get f() {
    return this.profileForm.controls;
  }

  onSave(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    this.isSavingProfile = true;
    this.saveError = '';
    this.saveSuccess = false;

    const doUpdate = () =>
      this.profileService.updateProfile(this.profileForm.value).subscribe({
        next: (updated) => {
          this.profile = updated;
          this.selectedFile = null;
          this.isSavingProfile = false;
          this.saveSuccess = true;
          setTimeout(() => { this.saveSuccess = false; this.cdr.detectChanges(); }, 3000);
          this.cdr.detectChanges();
        },
        error: () => {
          this.isSavingProfile = false;
          this.saveError = 'Failed to save profile. Please try again.';
          this.cdr.detectChanges();
        }
      });

    if (this.selectedFile) {
      this.profileService.uploadProfilePicture(this.selectedFile).subscribe({
        next: ({ profileImageUrl }) => {
          this.imagePreviewUrl = profileImageUrl;
          if (this.profile) this.profile = { ...this.profile, profileImageUrl };
          doUpdate();
        },
        error: () => {
          this.isSavingProfile = false;
          this.uploadError = 'Image upload failed. Please try again.';
          this.cdr.detectChanges();
        }
      });
    } else {
      doUpdate();
    }
  }

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file  = input.files?.[0];
    if (!file) return;

    this.uploadError = '';

    const ALLOWED = ['image/jpeg', 'image/png', 'image/webp'];
    if (!ALLOWED.includes(file.type)) {
      this.uploadError = 'Only JPEG, PNG, and WebP images are accepted.';
      return;
    }
    if (file.size > 5 * 1024 * 1024) {
      this.uploadError = 'Image must be under 5 MB.';
      return;
    }

    // Store file for upload on save; show local preview immediately
    this.selectedFile = file;
    this.imagePreviewUrl = URL.createObjectURL(file);
    this.cdr.detectChanges();

    input.value = '';
  }
}

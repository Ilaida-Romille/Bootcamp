import {
  Component, OnInit, inject, ChangeDetectorRef,
  ElementRef, ViewChild, HostListener
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfileService, UserProfileDto } from '../../features/attendee/services/profile.service';
import { ProfileDrawerService } from '../../core/services/profile-drawer.service';

@Component({
  selector: 'app-profile-drawer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile-drawer.component.html',
  styleUrls: ['./profile-drawer.component.css']
})
export class ProfileDrawerComponent implements OnInit {
  private readonly profileService  = inject(ProfileService);
  private readonly drawerService   = inject(ProfileDrawerService);
  private readonly fb              = inject(FormBuilder);
  private readonly cdr             = inject(ChangeDetectorRef);

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  isOpen = false;
  profile: UserProfileDto | null = null;
  profileForm!: FormGroup;

  isLoading        = false;
  isSaving         = false;
  isUploading      = false;
  imagePreviewUrl: string | null = null;
  saveError   = '';
  saveSuccess = false;
  uploadError = '';

  ngOnInit(): void {
    this.initForm();

    this.drawerService.isOpen$.subscribe(open => {
      this.isOpen = open;
      if (open && !this.profile) this.loadProfile();
      this.cdr.markForCheck();
    });
  }

  private initForm(): void {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      lastName:  ['', [Validators.required, Validators.maxLength(100)]],
      company:   ['', [Validators.maxLength(255)]],
      dietary:   ['', [Validators.maxLength(255)]]
    });
  }

  private loadProfile(): void {
    this.isLoading = true;
    this.profileService.getProfile().subscribe({
      next: (p) => {
        this.profile = p;
        this.imagePreviewUrl = p.profileImageUrl;
        const [firstName, ...rest] = (p.fullName ?? '').split(' ');
        this.profileForm.patchValue({
          firstName: firstName ?? '',
          lastName:  rest.join(' '),
          company:   p.company  ?? '',
          dietary:   p.dietary  ?? ''
        });
        this.isLoading = false;
        this.cdr.markForCheck();
      },
      error: () => { this.isLoading = false; this.cdr.markForCheck(); }
    });
  }

  close(): void { this.drawerService.close(); }

  get f() { return this.profileForm.controls; }

  onSave(): void {
    if (this.profileForm.invalid) { this.profileForm.markAllAsTouched(); return; }

    this.isSaving    = true;
    this.saveError   = '';
    this.saveSuccess = false;

    this.profileService.updateProfile(this.profileForm.value).subscribe({
      next: (updated) => {
        this.profile     = updated;
        this.isSaving    = false;
        this.saveSuccess = true;
        setTimeout(() => { this.saveSuccess = false; this.cdr.markForCheck(); }, 3000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.isSaving  = false;
        this.saveError = 'Failed to save. Please try again.';
        this.cdr.markForCheck();
      }
    });
  }

  triggerFileInput(): void { this.fileInput.nativeElement.click(); }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file  = input.files?.[0];
    if (!file) return;

    this.uploadError = '';

    if (!['image/jpeg', 'image/png', 'image/webp'].includes(file.type)) {
      this.uploadError = 'Only JPEG, PNG, or WebP images are accepted.';
      return;
    }
    if (file.size > 5 * 1024 * 1024) {
      this.uploadError = 'Image must be under 5 MB.';
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreviewUrl = reader.result as string;
      this.cdr.markForCheck();
    };
    reader.readAsDataURL(file);

    this.isUploading = true;
    this.profileService.uploadProfilePicture(file).subscribe({
      next: ({ profileImageUrl }) => {
        this.imagePreviewUrl = profileImageUrl;
        if (this.profile) this.profile = { ...this.profile, profileImageUrl };
        this.isUploading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.uploadError = 'Upload failed. Please try again.';
        this.isUploading = false;
        this.cdr.markForCheck();
      }
    });

    input.value = '';
  }

  // Close on Escape key
  @HostListener('document:keydown.escape')
  onEscape(): void { if (this.isOpen) this.close(); }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserProfileDto {
  id: number;
  email: string;
  fullName: string;
  company: string | null;
  dietary: string | null;
  profileImageUrl: string | null;
  organizationName: string | null;
}

export interface UserProfileUpdateDto {
  firstName?: string;
  lastName?: string;
  company?: string;
  dietary?: string;
}

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private readonly http = inject(HttpClient);

  getProfile(): Observable<UserProfileDto> {
    return this.http.get<UserProfileDto>('/api/profile/me');
  }

  updateProfile(dto: UserProfileUpdateDto): Observable<UserProfileDto> {
    return this.http.put<UserProfileDto>('/api/profile/me', dto);
  }

  uploadProfilePicture(file: File): Observable<{ profileImageUrl: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ profileImageUrl: string }>('/api/profile/me/picture', formData);
  }
}

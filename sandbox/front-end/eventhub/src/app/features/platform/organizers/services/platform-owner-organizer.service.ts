import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface OrganizerApiResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  company: string | null;
  userStatus: 'ACTIVE' | 'INACTIVE';
  organizationId: number | null;
  organizationName: string | null;
  primaryContactEmail: string | null;
  primaryContactPhone: string | null;
  organizationStatus: 'ACTIVE' | 'SUSPENDED' | 'PENDING' | null;
  totalEvents: number;
}

export interface UpdateOrganizerRequest {
  firstName: string;
  lastName: string;
  company: string;
  organizationName: string;
}

@Injectable({ providedIn: 'root' })
export class PlatformOwnerOrganizerService {
  private readonly baseUrl = '/api/platform-owner/organizers';
  private readonly http = inject(HttpClient);

  getOrganizers(): Observable<OrganizerApiResponse[]> {
    const params = new HttpParams().set('size', '500').set('page', '0');
    return this.http.get<{ content: OrganizerApiResponse[] }>(this.baseUrl, { params }).pipe(
      map(page => page.content)
    );
  }

  updateOrganizer(id: number, dto: UpdateOrganizerRequest): Observable<OrganizerApiResponse> {
    return this.http.put<OrganizerApiResponse>(`${this.baseUrl}/${id}`, dto);
  }

  toggleStatus(id: number, status: 'ACTIVE' | 'INACTIVE'): Observable<OrganizerApiResponse> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<OrganizerApiResponse>(`${this.baseUrl}/${id}/status`, null, { params });
  }

  deleteOrganizer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

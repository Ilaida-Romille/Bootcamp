import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrganizerStatus {
  id: string;
  name: string;
  status: 'Active' | 'Suspended' | 'Pending';
  eventIds: string[];
}

@Injectable({ providedIn: 'root' })
export class OrganizersDataService {
  private readonly dataUrl = '/data/organizers.json';

  constructor(private readonly http: HttpClient) {}

  getOrganizers(): Observable<OrganizerStatus[]> {
    return this.http.get<OrganizerStatus[]>(this.dataUrl);
  }
}

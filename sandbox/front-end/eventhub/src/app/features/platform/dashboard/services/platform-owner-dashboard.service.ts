import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MonthlyEventCountDto {
  label: string;
  value: number;
}

export interface PlatformOwnerDashboardDto {
  organizersCount: number;
  eventsCount: number;
  monthlyEventsData: MonthlyEventCountDto[];
}

@Injectable({ providedIn: 'root' })
export class PlatformOwnerDashboardService {
  private readonly baseUrl = '/api/platform-owner';

  private readonly http = inject(HttpClient);

  getDashboardMetrics(): Observable<PlatformOwnerDashboardDto> {
    return this.http.get<PlatformOwnerDashboardDto>(`${this.baseUrl}/dashboard`);
  }
}
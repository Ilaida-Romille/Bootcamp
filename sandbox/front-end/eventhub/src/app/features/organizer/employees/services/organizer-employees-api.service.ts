import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Employee } from '../models/employee.model';

interface EmployeeListQuery {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
  all?: boolean;
}

type EmployeesListResponse =
  | Employee[]
  | { content?: Employee[]; items?: Employee[]; data?: Employee[] };

export interface EmployeeInput {
  firstName: string;
  lastName: string;
  email: string;
  company: string;
  department: string;
  jobTitle: string;
  registeredEventIds?: string[];
}

export interface EmployeePatch {
  firstName?: string;
  lastName?: string;
  email?: string;
  company?: string;
  department?: string;
  jobTitle?: string;
  avatarUrl?: string;
  registeredEventIds?: string[];
}

@Injectable({ providedIn: 'root' })
export class OrganizerEmployeesApiService {
  private readonly baseUrl = '/api/employees';

  private readonly http = inject(HttpClient);

  getEmployees(query: EmployeeListQuery = {}): Observable<Employee[]> {
    const mergedQuery: Required<EmployeeListQuery> = {
      page: query.page ?? 0,
      size: query.size ?? 10,
      sortBy: query.sortBy ?? 'lastName',
      sortDir: query.sortDir ?? 'asc',
      all: query.all ?? true
    };

    const params = new HttpParams()
      .set('page', String(mergedQuery.page))
      .set('size', String(mergedQuery.size))
      .set('sortBy', mergedQuery.sortBy)
      .set('sortDir', mergedQuery.sortDir)
      .set('all', String(mergedQuery.all));

    return this.http.get<EmployeesListResponse>(this.baseUrl, { params }).pipe(
      map((response) => {
        if (Array.isArray(response)) {
          return response;
        }

        return response.content ?? response.items ?? response.data ?? [];
      })
    );
  }

  getEmployeeById(id: string): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}/${encodeURIComponent(id)}`);
  }

  createEmployee(employee: EmployeeInput): Observable<Employee> {
    return this.http.post<Employee>(this.baseUrl, employee);
  }

  updateEmployee(id: string, employee: EmployeeInput): Observable<Employee> {
    return this.http.put<Employee>(`${this.baseUrl}/${encodeURIComponent(id)}`, employee);
  }

  patchEmployee(id: string, employee: EmployeePatch): Observable<Employee> {
    return this.http.patch<Employee>(`${this.baseUrl}/${encodeURIComponent(id)}`, employee);
  }

  deleteEmployee(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${encodeURIComponent(id)}`);
  }
}

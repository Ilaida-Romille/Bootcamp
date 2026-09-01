import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map, tap, catchError, throwError } from 'rxjs';
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

  readonly employees = signal<Employee[]>([]);
  readonly selectedEmployee = signal<Employee | null>(null);
  readonly loading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  private handleError(err: unknown): Observable<never> {
    const message = err instanceof Error ? err.message : 'An error occurred';
    this.error.set(message);
    this.loading.set(false);
    return throwError(() => err);
  }

  getEmployees(query: EmployeeListQuery = {}): Observable<Employee[]> {
    this.loading.set(true);
    this.error.set(null);

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
      }),
      tap((result) => {
        this.employees.set(result);
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  getEmployeeById(id: string): Observable<Employee> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.get<Employee>(`${this.baseUrl}/${encodeURIComponent(id)}`).pipe(
      tap((employee) => {
        this.selectedEmployee.set(employee);
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  updateEmployee(id: string, employee: EmployeeInput): Observable<Employee> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.put<Employee>(`${this.baseUrl}/${encodeURIComponent(id)}`, employee).pipe(
      tap((updated) => {
        this.employees.update((list) => list.map((e) => (e.id === id ? updated : e)));
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  patchEmployee(id: string, employee: EmployeePatch): Observable<Employee> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.patch<Employee>(`${this.baseUrl}/${encodeURIComponent(id)}`, employee).pipe(
      tap((patched) => {
        this.employees.update((list) => list.map((e) => (e.id === id ? patched : e)));
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  deleteEmployee(id: string): Observable<void> {
    this.loading.set(true);
    this.error.set(null);
    return this.http.delete<void>(`${this.baseUrl}/${encodeURIComponent(id)}`).pipe(
      tap(() => {
        this.employees.update((list) => list.filter((e) => e.id !== id));
        this.loading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }
}

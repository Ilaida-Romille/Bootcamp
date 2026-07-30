import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { OrganizerEmployeesApiService } from './services/organizer-employees-api.service';
import { Employee } from './models/employee.model';

@Component({
  selector: 'app-organizer-employees',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './employees.component.html',
  styleUrl: './employees.component.css',
})
export class OrganizerEmployeesComponent implements OnInit {
  private readonly employeesApi = inject(OrganizerEmployeesApiService);
  private readonly cdr = inject(ChangeDetectorRef);

  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];

  searchTerm = '';
  selectedCompany = 'All';
  selectedDepartment = 'All';
  eventIdFilter = '';
  employeeIdQuery = '';

  companyOptions: string[] = ['All'];
  departmentOptions: string[] = ['All'];

  currentPage = 1;
  itemsPerPage = 8;

  isLoading = false;
  isLookupLoading = false;
  isLookupMode = false;
  dataLoadingError = '';

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.isLoading = true;
    this.dataLoadingError = '';

    this.employeesApi.getEmployees().subscribe({
      next: (employees) => {
        this.employees = [...employees].sort((a, b) => {
          const nameA = `${a.lastName} ${a.firstName}`.toLowerCase();
          const nameB = `${b.lastName} ${b.firstName}`.toLowerCase();
          return nameA.localeCompare(nameB);
        });

        this.companyOptions = ['All', ...new Set(this.employees.map((item) => item.company))];
        this.departmentOptions = ['All', ...new Set(this.employees.map((item) => item.department))];

        this.applyFilters();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.employees = [];
        this.filteredEmployees = [];
        this.isLoading = false;
        this.dataLoadingError = this.getApiErrorMessage(error, 'Unable to load employees from the API.');
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    this.isLookupMode = false;

    const normalizedSearch = this.searchTerm.trim().toLowerCase();
    const normalizedEventId = this.eventIdFilter.trim().toLowerCase();

    this.filteredEmployees = this.employees.filter((employee) => {
      const fullName = `${employee.firstName} ${employee.lastName}`.toLowerCase();
      const matchesSearch =
        normalizedSearch.length === 0 ||
        fullName.includes(normalizedSearch) ||
        employee.email.toLowerCase().includes(normalizedSearch) ||
        employee.jobTitle.toLowerCase().includes(normalizedSearch);

      const matchesCompany = this.selectedCompany === 'All' || employee.company === this.selectedCompany;
      const matchesDepartment = this.selectedDepartment === 'All' || employee.department === this.selectedDepartment;

      const matchesEventId =
        normalizedEventId.length === 0 ||
        employee.registeredEventIds.some((eventId) => eventId.toLowerCase().includes(normalizedEventId));

      return matchesSearch && matchesCompany && matchesDepartment && matchesEventId;
    });

    this.currentPage = 1;
  }

  lookupById(): void {
    const id = this.employeeIdQuery.trim();
    if (!id) {
      return;
    }

    this.isLookupLoading = true;
    this.dataLoadingError = '';

    this.employeesApi.getEmployeeById(id).subscribe({
      next: (employee) => {
        this.filteredEmployees = [employee];
        this.isLookupMode = true;
        this.currentPage = 1;
        this.isLookupLoading = false;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.filteredEmployees = [];
        this.isLookupMode = true;
        this.isLookupLoading = false;
        this.dataLoadingError = this.getApiErrorMessage(error, `No employee found for ID "${id}".`);
        this.cdr.detectChanges();
      }
    });
  }

  clearLookupAndFilters(): void {
    this.employeeIdQuery = '';
    this.searchTerm = '';
    this.selectedCompany = 'All';
    this.selectedDepartment = 'All';
    this.eventIdFilter = '';
    this.isLookupMode = false;
    this.applyFilters();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  get paginatedEmployees(): Employee[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredEmployees.slice(start, start + this.itemsPerPage);
  }

  private getApiErrorMessage(error: HttpErrorResponse, fallback: string): string {
    const message = error.error?.message;
    return typeof message === 'string' && message.trim().length > 0 ? message : fallback;
  }
}

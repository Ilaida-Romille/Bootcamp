import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { OrganizerEmployeesApiService, EmployeeInput } from './services/organizer-employees-api.service';
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

  // Data
  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];

  // Filters
  searchTerm = '';
  selectedCompany = 'All';
  selectedDepartment = 'All';
  eventIdFilter = '';
  employeeIdQuery = '';

  // Filter options
  companyOptions: string[] = ['All'];
  departmentOptions: string[] = ['All'];

  // Pagination
  currentPage = 1;
  itemsPerPage = 8;

  // Loading states
  isLoading = false;
  isLookupLoading = false;
  isFormSubmitting = false;
  isDeleting = false;
  dataLoadingError = '';
  formError = '';

  // Modal state
  isModalOpen = false;
  isEditMode = false;
  editingEmployeeId: string | null = null;

  // Confirmation dialog
  showDeleteConfirm = false;
  deleteConfirmId: string | null = null;
  deleteConfirmName: string = '';

  // Employee lookup mode
  isLookupMode = false;

  // View details modal
  isViewModalOpen = false;
  viewingEmployee: Employee | null = null;

  // Form data
  formData: EmployeeInput = this.getDefaultFormData();

  ngOnInit(): void {
    this.loadEmployees();
  }

  // ============ Loading & Filtering ============
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

  // ============ Modal & Form Management ============
  openAddEmployeeModal(): void {
    this.isEditMode = false;
    this.editingEmployeeId = null;
    this.formData = this.getDefaultFormData();
    this.formError = '';
    this.isModalOpen = true;
  }

  openEditEmployeeModal(employee: Employee): void {
    this.isEditMode = true;
    this.editingEmployeeId = employee.id;
    this.formData = { ...employee };
    this.formError = '';
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.isEditMode = false;
    this.editingEmployeeId = null;
    this.formData = this.getDefaultFormData();
    this.formError = '';
  }

  submitForm(): void {
    if (this.isFormValid()) {
      if (this.isEditMode && this.editingEmployeeId) {
        this.updateEmployee();
      } else {
        this.createEmployee();
      }
    }
  }

  private createEmployee(): void {
    this.isFormSubmitting = true;
    this.formError = '';

    this.employeesApi.createEmployee(this.formData).subscribe({
      next: (newEmployee) => {
        this.employees.push(newEmployee);
        this.employees.sort((a, b) => {
          const nameA = `${a.lastName} ${a.firstName}`.toLowerCase();
          const nameB = `${b.lastName} ${b.firstName}`.toLowerCase();
          return nameA.localeCompare(nameB);
        });
        this.applyFilters();
        this.isFormSubmitting = false;
        this.isModalOpen = false;
        this.editingEmployeeId = null;
        this.formData = this.getDefaultFormData();
        this.formError = '';
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.formError = this.getApiErrorMessage(error, 'Failed to create employee. Please try again.');
        this.isFormSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  private updateEmployee(): void {
    if (!this.editingEmployeeId) return;

    this.isFormSubmitting = true;
    this.formError = '';

    this.employeesApi.updateEmployee(this.editingEmployeeId, this.formData).subscribe({
      next: (updatedEmployee) => {
        const index = this.employees.findIndex((emp) => emp.id === this.editingEmployeeId);
        if (index !== -1) {
          this.employees[index] = updatedEmployee;
        }
        this.applyFilters();
        this.isFormSubmitting = false;
        this.isModalOpen = false;
        this.isEditMode = false;
        this.editingEmployeeId = null;
        this.formData = this.getDefaultFormData();
        this.formError = '';
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.formError = this.getApiErrorMessage(error, 'Failed to update employee. Please try again.');
        this.isFormSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ============ Delete Functionality ============
  openDeleteConfirm(employee: Employee): void {
    this.deleteConfirmId = employee.id;
    this.deleteConfirmName = `${employee.firstName} ${employee.lastName}`;
    this.showDeleteConfirm = true;
  }

  closeDeleteConfirm(): void {
    this.showDeleteConfirm = false;
    this.deleteConfirmId = null;
    this.deleteConfirmName = '';
  }

  confirmDelete(): void {
    if (!this.deleteConfirmId) return;

    this.isDeleting = true;
    const employeeId = this.deleteConfirmId;

    this.employeesApi.deleteEmployee(employeeId).subscribe({
      next: () => {
        this.employees = this.employees.filter((emp) => emp.id !== employeeId);
        this.applyFilters();
        this.isDeleting = false;
        this.showDeleteConfirm = false;
        this.deleteConfirmId = null;
        this.deleteConfirmName = '';
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        const errorMsg = this.getApiErrorMessage(error, 'Failed to delete employee. Please try again.');
        this.dataLoadingError = errorMsg;
        this.isDeleting = false;
        this.showDeleteConfirm = false;
        this.deleteConfirmId = null;
        this.deleteConfirmName = '';
        this.cdr.detectChanges();
      }
    });
  }

  // ============ View Details ============
  viewEmployeeDetails(employee: Employee): void {
    this.viewingEmployee = employee;
    this.isViewModalOpen = true;

    console.log(this.viewingEmployee.avatarUrl);
  }

  closeViewModal(): void {
    this.isViewModalOpen = false;
    this.viewingEmployee = null;
  }

  // ============ Helper Methods ============
  private getDefaultFormData(): EmployeeInput {
    return {
      firstName: '',
      lastName: '',
      email: '',
      company: '',
      department: '',
      jobTitle: '',
      avatarUrl: '',
      registeredEventIds: []
    };
  }

  private isFormValid(): boolean {
    const { firstName, lastName, email, company, department, jobTitle } = this.formData;

    if (!firstName.trim() || !lastName.trim() || !email.trim() || !company.trim() || !department.trim() || !jobTitle.trim()) {
      this.formError = 'All required fields must be filled.';
      this.cdr.detectChanges();
      return false;
    }

    if (!this.isValidEmail(email)) {
      this.formError = 'Please enter a valid email address.';
      this.cdr.detectChanges();
      return false;
    }

    return true;
  }

  private isValidEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+$/.test(email);
  }

  private getApiErrorMessage(error: HttpErrorResponse, fallback: string): string {
    const message = error.error?.message;
    return typeof message === 'string' && message.trim().length > 0 ? message : fallback;
  }

  // Handle comma-separated event IDs
  get eventIdsDisplay(): string {
    return this.formData.registeredEventIds.join(', ');
  }

  set eventIdsDisplay(value: string) {
    this.formData.registeredEventIds = value
      .split(',')
      .map((id) => id.trim())
      .filter((id) => id.length > 0);
  }
}

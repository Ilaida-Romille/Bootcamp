import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { PlatformLayoutComponent } from './platform-layout.component';
import { NavbarContextService } from '../../core/services/navbar-context.service';

describe('PlatformLayoutComponent', () => {
  let component: PlatformLayoutComponent;
  let fixture: ComponentFixture<PlatformLayoutComponent>;

  // 1. Create a mock for NavbarContextService
  const mockNavbarContextService = {
    updateContext: vi.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlatformLayoutComponent],
      providers: [
        provideRouter([]), // Provides router context for <router-outlet> and sidebar links
        { provide: NavbarContextService, useValue: mockNavbarContextService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PlatformLayoutComponent);
    component = fixture.componentInstance;
  });

  it('should create and set navbar context on init', () => {
    fixture.detectChanges(); // Safely triggers ngOnInit()

    expect(component).toBeTruthy();
    expect(mockNavbarContextService.updateContext).toHaveBeenCalledWith({
      userName: 'Jane Dela Cruz'
    });
  });
});
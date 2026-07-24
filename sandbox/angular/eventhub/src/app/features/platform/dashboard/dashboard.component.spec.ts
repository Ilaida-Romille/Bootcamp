import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlatformOwnerDashboardComponent } from './dashboard.component';

describe('PlatformOwnerDashboard', () => {
  let component: PlatformOwnerDashboardComponent;
  let fixture: ComponentFixture<PlatformOwnerDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlatformOwnerDashboardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PlatformOwnerDashboardComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

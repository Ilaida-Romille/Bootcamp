import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlatformOwnerDashboard } from './platform-owner-dashboard';

describe('PlatformOwnerDashboard', () => {
  let component: PlatformOwnerDashboard;
  let fixture: ComponentFixture<PlatformOwnerDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlatformOwnerDashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(PlatformOwnerDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

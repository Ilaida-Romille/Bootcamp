import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizerLayoutComponent } from './organizer-layout.component';

describe('OrganizerLayoutComponent', () => {
  let component: OrganizerLayoutComponent;
  let fixture: ComponentFixture<OrganizerLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganizerLayoutComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(OrganizerLayoutComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

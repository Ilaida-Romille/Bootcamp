import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendeeUpcomingEvents } from './attendee-upcoming-events';

describe('AttendeeUpcomingEvents', () => {
  let component: AttendeeUpcomingEvents;
  let fixture: ComponentFixture<AttendeeUpcomingEvents>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AttendeeUpcomingEvents],
    }).compileComponents();

    fixture = TestBed.createComponent(AttendeeUpcomingEvents);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

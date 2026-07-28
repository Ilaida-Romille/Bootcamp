import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendeeListWidgetComponent } from './attendee-list-widget.component';

describe('AttendeeListWidgetComponent', () => {
  let component: AttendeeListWidgetComponent;
  let fixture: ComponentFixture<AttendeeListWidgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AttendeeListWidgetComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AttendeeListWidgetComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

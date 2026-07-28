import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactOrganizerWidgetComponent } from './contact-organizer-widget.component';

describe('ContactOrganizerWidgetComponent', () => {
  let component: ContactOrganizerWidgetComponent;
  let fixture: ComponentFixture<ContactOrganizerWidgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContactOrganizerWidgetComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ContactOrganizerWidgetComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

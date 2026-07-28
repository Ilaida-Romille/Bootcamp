import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizerSidebarComponent } from './organizer-sidebar.component';

describe('OrganizerSidebarComponent', () => {
  let component: OrganizerSidebarComponent;
  let fixture: ComponentFixture<OrganizerSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganizerSidebarComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(OrganizerSidebarComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { ActionCardComponent } from './action-card.component';

describe('ActionCardComponent', () => {
  let component: ActionCardComponent;
  let fixture: ComponentFixture<ActionCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActionCardComponent],
      providers: [
        provideRouter([]) // 1. Provides routing context for [routerLink]
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ActionCardComponent);
    component = fixture.componentInstance;

    // 2. Assign BOTH required inputs BEFORE running change detection
    component.label = 'Manage Organizers';
    component.routerLink = '/platform/organizers';

    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
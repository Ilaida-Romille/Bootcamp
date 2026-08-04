import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { EventCardComponent } from './event-card.component';
import { EventItemDisplay } from '../../../models/attendee.model';

describe('EventCardComponent', () => {
  let component: EventCardComponent;
  let fixture: ComponentFixture<EventCardComponent>;

  // 1. Define dummy event data matching EventItemDisplay
  const mockEvent: EventItemDisplay = {
    id: 'evt-1',
    title: 'Angular Conf 2026',
    description: 'Learn latest Angular features',
    organizerName: 'Dev Guild',
    status: 'registration_open',
    startDateTime: '2026-11-01T10:00:00Z',
    endDateTime: '2026-11-01T18:00:00Z',
    venue: 'Convention Center',
    bannerImageUrl: '',
    capacity: { maximum: 100, registered: 20 },
    remainingSlots: 80,
    agenda: []
  } as unknown as EventItemDisplay;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCardComponent],
      providers: [
        provideRouter([]) // 2. Provide router context for [routerLink]
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EventCardComponent);
    component = fixture.componentInstance;

    // 3. Assign required @Input() BEFORE running change detection
    component.event = mockEvent;

    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle flip state when toggleFlip is called', () => {
    expect(component.isFlipped).toBe(false);

    component.toggleFlip();

    expect(component.isFlipped).toBe(true);
  });

  it('should compute capacityDisplay and canRegister correctly', () => {
    expect(component.capacityDisplay).toBe('20 / 100');
    expect(component.canRegister).toBe(true);
  });
});
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TicketListItemComponent } from './ticket-list-item.component';
import { Ticket } from '../../models/ticket.model';

describe('TicketListItemComponent', () => {
  let component: TicketListItemComponent;
  let fixture: ComponentFixture<TicketListItemComponent>;

  // 1. Define dummy ticket data that satisfies the Ticket model
  const mockTicket: Ticket = {
    id: '1',
    ticketNumber: 'TCK-1001',
    subject: 'Cannot login to account',
    companyName: 'Acme Corp',
    openedTimeAgo: '2 hours ago',
    status: 'Open',
    priority: 'High',
    message: 'User is unable to log in with correct credentials.'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketListItemComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TicketListItemComponent);
    component = fixture.componentInstance;

    // 2. Set the required @Input property HERE
    component.ticket = mockTicket;

    fixture.detectChanges(); // Triggers Angular lifecycle & template rendering
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit selectTicket event when onSelect is called', () => {
    const emitSpy = vi.spyOn(component.selectTicket, 'emit');

    component.onSelect();

    expect(emitSpy).toHaveBeenCalledWith(mockTicket);
  });

  it('should return correct badge class for priority levels', () => {
    expect(component.getPriorityBadgeClass('High')).toBe('badge-danger');
    expect(component.getPriorityBadgeClass('Medium')).toBe('badge-warning');
    expect(component.getPriorityBadgeClass('Low')).toBe('badge-secondary');
  });
});
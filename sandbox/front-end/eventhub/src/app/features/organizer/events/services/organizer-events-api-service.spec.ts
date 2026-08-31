import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { OrganizerEventsApiService, EventInput } from './organizer-events-api.service';
import { Event } from '../models/event.model';

describe('OrganizerEventsApiService', () => {
  let service: OrganizerEventsApiService;
  let httpMock: HttpTestingController;

  const mockEvent: Event = {
    id: 'evt-1',
    title: 'Tech Conference',
    description: 'Annual tech event',
    organizerId: 'org-1',
    organizerName: 'Tech Corp',
    status: 'registration_open',
    startTime: '2026-09-01T09:00:00Z',
    endTime: '2026-09-01T17:00:00Z',
    registrationOpensAt: '2026-08-01T00:00:00Z',
    registrationClosesAt: '2026-08-31T23:59:59Z',
    venue: 'Main Hall',
    bannerImageUrl: '',
    capacity: { maximum: 100, registered: 20 },
    agenda: []
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        OrganizerEventsApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(OrganizerEventsApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  // it('should fetch events list', () => {
  //   service.getEvents().subscribe((events) => {
  //     expect(events).toHaveLength(1);
  //     expect(events[0].title).toBe('Tech Conference');
  //   });

  //   const req = httpMock.expectOne('/api/events');
  //   expect(req.request.method).toBe('GET');
  //   req.flush([mockEvent]);
  // });

  it('should fetch a single event by ID', () => {
    service.getEventById('evt-1').subscribe((event) => {
      expect(event).toEqual(mockEvent);
    });

    const req = httpMock.expectOne('/api/events/evt-1');
    expect(req.request.method).toBe('GET');
    req.flush(mockEvent);
  });

  it('should create an event via POST', () => {
    const inputPayload: EventInput = { ...mockEvent };

    service.createEvent(inputPayload).subscribe((event) => {
      expect(event).toEqual(mockEvent);
    });

    const req = httpMock.expectOne('/api/events');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(inputPayload);
    req.flush(mockEvent);
  });

  it('should delete an event via DELETE', () => {
    service.deleteEvent('evt-1').subscribe();

    const req = httpMock.expectOne('/api/events/evt-1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
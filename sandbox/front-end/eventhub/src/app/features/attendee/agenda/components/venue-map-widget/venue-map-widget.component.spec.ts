import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueMapWidgetComponent } from './venue-map-widget.component';

describe('VenueMapWidgetComponent', () => {
  let component: VenueMapWidgetComponent;
  let fixture: ComponentFixture<VenueMapWidgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VenueMapWidgetComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VenueMapWidgetComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

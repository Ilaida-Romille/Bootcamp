import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillingFiltersComponent } from './billing-filters.component';

describe('BillingFiltersComponent', () => {
  let component: BillingFiltersComponent;
  let fixture: ComponentFixture<BillingFiltersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BillingFiltersComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BillingFiltersComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

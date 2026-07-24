import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillingTableComponent } from './billing-table.component';

describe('BillingTableComponent', () => {
  let component: BillingTableComponent;
  let fixture: ComponentFixture<BillingTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BillingTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BillingTableComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

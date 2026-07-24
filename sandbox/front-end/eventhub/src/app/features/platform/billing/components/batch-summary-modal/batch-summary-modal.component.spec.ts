import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BatchSummaryModalComponent } from './batch-summary-modal.component';

describe('BatchSummaryModalComponent', () => {
  let component: BatchSummaryModalComponent;
  let fixture: ComponentFixture<BatchSummaryModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BatchSummaryModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BatchSummaryModalComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

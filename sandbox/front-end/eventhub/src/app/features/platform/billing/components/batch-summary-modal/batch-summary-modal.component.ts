import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BatchSummary } from '../../models/billing-model/billing-model.component';

@Component({
  selector: 'app-batch-summary-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './batch-summary-modal.component.html',
  styleUrl: './batch-summary-modal.component.css'
})
export class BatchSummaryModalComponent {
  @Input() batchSummary: BatchSummary | null = null;
}
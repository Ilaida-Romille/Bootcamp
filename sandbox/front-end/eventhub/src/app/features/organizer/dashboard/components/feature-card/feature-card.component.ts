import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface FeatureCardData {
  id: string;
  iconSrc: string;
  title: string;
  description: string;
  actionText: string;
}

@Component({
  selector: 'app-feature-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feature-card.component.html',
  styleUrls: ['./feature-card.component.css']
})
export class FeatureCardComponent {
  @Input({ required: true }) data!: FeatureCardData;
  @Output() cardClick = new EventEmitter<string>();

  onAction(): void {
    this.cardClick.emit(this.data.id);
  }
}
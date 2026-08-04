import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeatureCardComponent, FeatureCardData } from './feature-card.component';

describe('FeatureCardComponent', () => {
  let component: FeatureCardComponent;
  let fixture: ComponentFixture<FeatureCardComponent>;

  const mockCardData: FeatureCardData = {
    id: 'feat-1',
    iconSrc: '/assets/icon.svg',
    title: 'Test Feature',
    description: 'Test description',
    actionText: 'Learn More'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeatureCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FeatureCardComponent);
    component = fixture.componentInstance;

    component.data = mockCardData;
    
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit cardClick event on action', () => {
    const emitSpy = vi.spyOn(component.cardClick, 'emit');

    component.onAction();

    expect(emitSpy).toHaveBeenCalledWith('feat-1');
  });
});

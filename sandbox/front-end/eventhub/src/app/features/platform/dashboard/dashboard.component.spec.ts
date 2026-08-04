import { Component, Input } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlatformOwnerDashboardComponent } from './dashboard.component';
import { MetricCardComponent } from '../../../shared/components/metric-card/metric-card.component';
import { ActionCardComponent } from '../../../shared/components/action-card/action-card.component';
import { ChartPanelComponent } from './components/chart-panel/chart-panel.component';

// 1. Create lightweight stub components to prevent required input errors
@Component({ selector: 'app-metric-card', standalone: true, template: '' })
class MockMetricCardComponent {
  @Input() title: any;
  @Input() value: any;
  @Input() icon: any;
}

@Component({ selector: 'app-action-card', standalone: true, template: '' })
class MockActionCardComponent {
  @Input() title: any;
  @Input() description: any;
}

@Component({ selector: 'app-chart-panel', standalone: true, template: '' })
class MockChartPanelComponent {
  @Input() data: any;
  @Input() title: any;
}

describe('PlatformOwnerDashboardComponent', () => {
  let component: PlatformOwnerDashboardComponent;
  let fixture: ComponentFixture<PlatformOwnerDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlatformOwnerDashboardComponent],
    })
      // 2. Override imports to use mock child components instead of real ones
      .overrideComponent(PlatformOwnerDashboardComponent, {
        remove: { imports: [MetricCardComponent, ActionCardComponent, ChartPanelComponent] },
        add: { imports: [MockMetricCardComponent, MockActionCardComponent, MockChartPanelComponent] },
      })
      .compileComponents();

    fixture = TestBed.createComponent(PlatformOwnerDashboardComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
});
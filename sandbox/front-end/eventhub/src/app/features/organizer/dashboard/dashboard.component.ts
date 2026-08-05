import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeatureCardComponent, FeatureCardData } from './components/feature-card/feature-card.component';


@Component({
  selector: 'app-organizer-dashboard',
  standalone: true,
  imports: [CommonModule, FeatureCardComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class OrganizerDashboardComponent {
  companyName = 'Acme Events Co.';

  featureNodes: FeatureCardData[] = [
    {
      id: 'manage-employees',
      iconSrc: 'assets/img/sign-in/organizers_icon_50x50.png',
      title: 'Manage Employees',
      description: 'Add, remove, and manage the employees on your account.',
      actionText: 'Open'
    },
    {
      id: 'manage-events',
      iconSrc: 'assets/img/organizer/events_icon_68x68.png',
      title: 'Manage Events',
      description: 'Create, edit, and configure your company\'s events.',
      actionText: 'Open'
    },
    {
      id: 'settings',
      iconSrc: 'assets/img/organizer/settings_icon_50x50.png',
      title: 'Settings',
      description: 'Update your company profile and account preferences.',
      actionText: 'Open'
    },
    {
      id: 'file-ticket',
      iconSrc: 'assets/img/organizer/ticket_icon_50x50.png',
      title: 'File a Ticket / Request',
      description: 'Reach out to the EventHub platform team for help.',
      actionText: 'New Ticket'
    }
  ];

  onCardAction(cardId: string): void {
    console.log(`Action triggered for card: ${cardId}`);
    // Future implementation handlers go here
  }
}
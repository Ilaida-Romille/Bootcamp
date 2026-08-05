import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-venue-map-widget',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="custom-widget-card mb-4">
      <h2 class="custom-section-title widget-title fs-6">Venue</h2>
      <div class="glass-map-box rounded-3 overflow-hidden">

        <div class="map-container">
          <!-- Pure CSS Interactive Checkbox Toggles -->
          <input type="checkbox" id="map-activate" class="map-toggle">

          <!-- Click Guard Activation Screen -->
          <label for="map-activate" class="map-overlay" aria-label="Click to interact with map">
            <svg class="map-overlay-icon" xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7z"></path>
              <circle cx="12" cy="9" r="2.5"></circle>
            </svg>
            <span class="map-overlay-text text-white">Click to Interact</span>
          </label>

          <!-- Map Pins Graphic Node System Overlays -->
          <div class="map-pin-wrapper">
            <input type="checkbox" id="pin-main-hall" class="pin-toggle">
            <label for="pin-main-hall" class="map-pin" aria-label="View Main Hall details">
              <span class="pin-pulse"></span>
            </label>
            <!-- Interactive Sibling Tooltip Node -->
            <div class="popup-card rounded-1">
              <label for="pin-main-hall" class="popup-close" aria-label="Close venue info">&#x2715;</label>
              <p class="popup-title">{{ pinTitle }}</p>
              <p class="popup-detail">{{ pinZone }}</p>
              <p class="popup-detail">{{ venueName }}</p>
            </div>
          </div>

          <!-- Vector Dark Filter Mapping Frame Canvas -->
          <iframe 
            class="map-iframe" 
            [src]="safeMapUrl" 
            [title]="venueName + ' Venue Map'" 
            loading="lazy" 
            referrerpolicy="no-referrer-when-downgrade" 
            allowfullscreen>
          </iframe>
        </div>

        <!-- Footer Descriptor Text Bar -->
        <div class="map-footer text-start">
          <span class="map-venue-name text-white small">{{ venueName }}</span>
          <span class="map-venue-address text-secondary extra-small-text">{{ venueAddress }}</span>
        </div>

      </div>
    </div>
  `,
  styleUrls: ['../../agenda.component.css']
})
export class VenueMapWidgetComponent {
  @Input() venueName = 'Makati Convention Center';
  @Input() venueAddress = 'Makati City, Metro Manila, Philippines';
  @Input() pinTitle = 'Main Hall';
  @Input() pinZone = 'Building A — Tech Zone';
  
  private readonly sanitizer = inject(DomSanitizer);
  safeMapUrl: SafeResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
    'https://maps.google.com/maps?q=Makati+Convention+Center,+Makati+City,+Philippines&hl=en&z=15&output=embed'
  );
}
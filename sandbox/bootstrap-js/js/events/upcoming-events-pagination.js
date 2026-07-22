document.addEventListener('DOMContentLoaded', () => {
    // -------------------------------------------------------------
    // Configuration & State
    // -------------------------------------------------------------
    const itemsPerPage = 6; 
    let currentPage = 1;
    let eventsData = [];

    // -------------------------------------------------------------
    // DOM Elements
    // -------------------------------------------------------------
    const eventsGrid = document.getElementById('eventsGridContainer');
    const pageNumbersContainer = document.getElementById('pageNumbersContainer');
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');

    // -------------------------------------------------------------
    // 1. Fetch JSON Data
    // -------------------------------------------------------------
    async function loadEventsData() {
        try {
            // Update this path to where your event JSON is stored
            const response = await fetch('../data/organizer/events.json'); 
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            
            eventsData = await response.json();
            renderView();
        } catch (error) {
            console.error('Error loading events JSON:', error);
            if (eventsGrid) {
                eventsGrid.innerHTML = `<div class="col-12 text-center text-danger py-4">Failed to load events.</div>`;
            }
        }
    }

    // -------------------------------------------------------------
    // 2. Render Page View
    // -------------------------------------------------------------
    function renderView() {
        const totalItems = eventsData.length;
        const totalPages = Math.ceil(totalItems / itemsPerPage) || 1;

        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        // Extract slice for current page
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const currentSlice = eventsData.slice(startIndex, endIndex);

        // Render cards and pagination
        renderEventCards(currentSlice);
        renderPagination(totalPages);
    }

    // -------------------------------------------------------------
    // Helper: Dynamic Flip Card Generator
    // -------------------------------------------------------------
    function renderEventCards(events) {
        if (!eventsGrid) return;

        if (events.length === 0) {
            eventsGrid.innerHTML = `<div class="col-12 text-center text-white-50 py-4">No events found.</div>`;
            return;
        }

        eventsGrid.innerHTML = events.map((event, index) => {
            const cardId = `flip-card-${event.id || index}`;
            const formattedDate = formatDate(event.date);
            const statusClass = getStatusClass(event.status);
            const encodedTitle = encodeURIComponent(event.title || '');
            const encodedDate = encodeURIComponent(formattedDate);
            const encodedID = encodeURIComponent(event.id);

            return `
                <div class="col-12 col-md-6 col-lg-4 card-flip-wrapper">
                    <input type="checkbox" id="${cardId}" class="card-flip-toggle">
                    <div class="card-flip-inner">
                        
                        <!-- Front Card -->
                        <div class="card-front event-card rounded-1 overflow-hidden">
                            <label for="${cardId}" class="card-flip-trigger" aria-label="View stats for ${escapeHtml(event.title)}"></label>
                            <div class="event-banner-placeholder"></div>
                            <div class="event-details flex-grow-1 d-flex flex-column justify-content-between">
                                <div>
                                    <h2 class="event-title">${escapeHtml(event.title)}</h2>
                                    <p class="event-date">${escapeHtml(formattedDate)}</p>
                                    <p class="event-organizer">by ${escapeHtml(event.organizerName || 'Unknown Organizer')}</p>
                                </div>
                                <a href="registration.html?title=${encodedTitle}&date=${encodedDate}&id=${encodedID}" class="btn btn-dark event-btn rounded-1 mt-2">View & Register</a>
                            </div>
                        </div>

                        <!-- Back Card -->
                        <div class="card-back rounded-1">
                            <span class="back-badge">Quick Stats</span>
                            <ul class="back-stats list-unstyled">
                                <li>
                                    <span class="stat-label">Current Attendees</span>
                                    <span class="stat-value">${event.currentAttendees ?? 0}</span>
                                </li>
                                <li>
                                    <span class="stat-label">Event Status</span>
                                    <span class="stat-value ${statusClass}">${escapeHtml(event.status || 'N/A')}</span>
                                </li>
                                <li>
                                    <span class="stat-label">Remaining Slots</span>
                                    <span class="stat-value">${event.remainingSlots ?? 0}</span>
                                </li>
                            </ul>
                            <label for="${cardId}" class="back-to-details-btn">&#8592; Back to Details</label>
                        </div>

                    </div>
                </div>
            `;
        }).join('');
    }

    // -------------------------------------------------------------
    // Helper: Dynamic Pagination Controls
    // -------------------------------------------------------------
    function renderPagination(totalPages) {
        if (!pageNumbersContainer) return;
        pageNumbersContainer.innerHTML = '';

        // Handle Prev button
        if (currentPage <= 1) {
            prevPageBtn.classList.add('page-disabled');
            prevPageBtn.setAttribute('tabindex', '-1');
            prevPageBtn.setAttribute('aria-disabled', 'true');
        } else {
            prevPageBtn.classList.remove('page-disabled');
            prevPageBtn.removeAttribute('tabindex');
            prevPageBtn.removeAttribute('aria-disabled');
        }

        // Handle Next button
        if (currentPage >= totalPages) {
            nextPageBtn.classList.add('page-disabled');
            nextPageBtn.setAttribute('tabindex', '-1');
            nextPageBtn.setAttribute('aria-disabled', 'true');
        } else {
            nextPageBtn.classList.remove('page-disabled');
            nextPageBtn.removeAttribute('tabindex');
            nextPageBtn.removeAttribute('aria-disabled');
        }

        // Generate number buttons matching custom-page-link styling
        for (let i = 1; i <= totalPages; i++) {
            const a = document.createElement('a');
            a.href = '#';
            a.className = `custom-page-link ${i === currentPage ? 'page-active' : ''}`;
            if (i === currentPage) a.setAttribute('aria-current', 'page');
            a.textContent = i;

            a.addEventListener('click', (e) => {
                e.preventDefault();
                if (currentPage !== i) {
                    currentPage = i;
                    renderView();
                }
            });

            pageNumbersContainer.appendChild(a);
        }
    }

    // -------------------------------------------------------------
    // Helper Functions
    // -------------------------------------------------------------
    function getStatusClass(status) {
        const s = (status || '').toLowerCase();
        if (s.includes('open')) return 'status-open';
        if (s.includes('filling') || s.includes('fast')) return 'status-filling';
        if (s.includes('almost') || s.includes('full')) return 'status-full';
        return '';
    }

    function formatDate(dateString) {
        if (!dateString) return '';
        const options = { month: 'short', day: 'numeric', year: 'numeric' };
        const dateObj = new Date(dateString);
        return isNaN(dateObj) ? dateString : dateObj.toLocaleDateString('en-US', options);
    }

    function escapeHtml(str) {
        return (str || '').replace(/[&<>"']/g, match => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[match]));
    }

    // -------------------------------------------------------------
    // Event Listeners for Prev/Next Controls
    // -------------------------------------------------------------
    prevPageBtn.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentPage > 1) {
            currentPage--;
            renderView();
        }
    });

    nextPageBtn.addEventListener('click', (e) => {
        e.preventDefault();
        const totalPages = Math.ceil(eventsData.length / itemsPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            renderView();
        }
    });

    // -------------------------------------------------------------
    // Kickoff
    // -------------------------------------------------------------
    loadEventsData();
});
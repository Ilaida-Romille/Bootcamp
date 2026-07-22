document.addEventListener('DOMContentLoaded', () => {
    // -------------------------------------------------------------
    // Configuration & State
    // -------------------------------------------------------------
    const itemsPerPage = 5; 
    let currentPage = 1;
    let rawData = [];      // Holds all JSON data
    let filteredData = []; // Holds filtered data after search/status checks

    // -------------------------------------------------------------
    // DOM Elements
    // -------------------------------------------------------------
    const tableBody = document.getElementById('companyTableBody');
    const searchInput = document.getElementById('searchCompany');
    const statusDropdownBtn = document.getElementById('statusFilterBtn');
    const statusDropdownItems = document.querySelectorAll('#statusFilterDropdown .dropdown-item');
    let selectedStatusValue = 'All'; // Track current status selection
    
    const filterForm = document.getElementById('companyFilterForm');
    
    // Updated Pagination Elements
    const pageNumbersContainer = document.getElementById('pageNumbersContainer');
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const paginationInfo = document.getElementById('pagination-info');

    // Add event listeners for each Bootstrap dropdown option
    statusDropdownItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();

            // 1. Get selected value
            selectedStatusValue = item.getAttribute('data-value');

            // 2. Update Button Label Text
            const labelSpan = statusDropdownBtn.querySelector('span');
            if (labelSpan) labelSpan.textContent = item.textContent;

            // 3. Toggle Active state styling across menu items
            statusDropdownItems.forEach(el => el.classList.remove('active'));
            item.classList.add('active');

            // NOTE: Auto-filter removed. Requires Search button click to execute filter.
        });
    });

    // -------------------------------------------------------------
    // 1. Fetch JSON Data
    // -------------------------------------------------------------
    async function loadCompanyData() {
        try {
            const response = await fetch('../data/organizer/organizers.json'); 
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            
            rawData = await response.json();
            
            // Once data is loaded, display initial empty state
            applyFilters();
        } catch (error) {
            console.error('Error loading company JSON data:', error);
            if (tableBody) {
                renderTableMessage('Failed to load data.', 'text-danger');
            }
        }
    }

    // -------------------------------------------------------------
    // 2. Filter Logic (Triggered ONLY via Search Button Click)
    // -------------------------------------------------------------
    function applyFilters() {
        const searchTerm = (searchInput ? searchInput.value : '').trim().toLowerCase();
        const selectedStatus = selectedStatusValue.toLowerCase();

        // If search term is 1 or 2 chars, exit immediately and DO NOT modify data or UI
        if (searchTerm.length > 0 && searchTerm.length < 3) {
            return;
        }

        filteredData = rawData.filter(item => {
            const matchesName = (item.name || '').toLowerCase().includes(searchTerm);
            const matchesStatus = selectedStatus === 'all' || (item.status || '').toLowerCase() === selectedStatus;

            return matchesName && matchesStatus;
        });

        currentPage = 1; // Reset to first page when filtering
        renderView();
    }

    // -------------------------------------------------------------
    // 3. Render View (Table Rows + Pagination Controls)
    // -------------------------------------------------------------
    function renderView() {
        const totalItems = filteredData.length;
        const totalPages = Math.ceil(totalItems / itemsPerPage) || 1;

        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        // Extract slice of data for current page
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const currentSlice = filteredData.slice(startIndex, endIndex);

        // Render Table Body
        renderTableRows(currentSlice);

        // Render Info Text
        if (paginationInfo) {
            if (totalItems === 0) {
                paginationInfo.textContent = 'Showing 0 to 0 of 0 entries';
            } else {
                const showingStart = startIndex + 1;
                const showingEnd = Math.min(endIndex, totalItems);
                paginationInfo.textContent = `Showing ${showingStart} to ${showingEnd} of ${totalItems} entries`;
            }
        }

        // Render Custom Pagination Controls
        renderPagination(totalPages);
    }

    // -------------------------------------------------------------
    // Helper: Render Single Message Row safely
    // -------------------------------------------------------------
    function renderTableMessage(messageText, textClass = 'text-white-50') {
        tableBody.replaceChildren(); // Safely clears existing table rows
        
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        
        td.colSpan = 4;
        td.className = `text-center ${textClass} py-4`;
        td.textContent = messageText;
        
        tr.appendChild(td);
        tableBody.appendChild(tr);
    }

    // -------------------------------------------------------------
    // Helper: Dynamic Table Row Generator
    // -------------------------------------------------------------
    function renderTableRows(items) {
        if (!tableBody) return;

        const searchTerm = (searchInput ? searchInput.value : '').trim();

        if (searchTerm.length > 0 && searchTerm.length < 3) {
            renderTableMessage('Please enter at least 3 characters to search.');
            return;
        }

        if (items.length === 0) {
            renderTableMessage('No company record/s found.');
            return;
        }

        // Clear existing table contents safely
        tableBody.replaceChildren();

        const fragment = document.createDocumentFragment();

        items.forEach(item => {
            const eventCount = item.eventIds ? item.eventIds.length : 0;
            const badgeClass = getBadgeClass(item.status);

            const tr = document.createElement('tr');

            // 1. Name Cell
            const tdName = document.createElement('td');
            tdName.className = 'fw-medium text-white';
            tdName.textContent = item.name || '';

            // 2. Events Count Cell
            const tdEvents = document.createElement('td');
            tdEvents.textContent = eventCount;

            // 3. Status Badge Cell
            const tdStatus = document.createElement('td');
            const spanBadge = document.createElement('span');
            spanBadge.className = `badge ${badgeClass} border px-2 py-1`;
            spanBadge.textContent = item.status || '';
            tdStatus.appendChild(spanBadge);

            // 4. Actions Cell
            const tdActions = document.createElement('td');
            tdActions.className = 'actions-cell';

            const viewLink = document.createElement('a');
            viewLink.href = '#';
            viewLink.dataset.id = item.id;
            viewLink.textContent = 'View';

            const divider = document.createElement('span');
            divider.className = 'action-divider';
            divider.textContent = '|';

            const statusLink = document.createElement('a');
            statusLink.href = '#';
            statusLink.dataset.id = item.id;
            statusLink.textContent = 'Change Status';

            tdActions.append(viewLink, divider, statusLink);

            // Append all cells to table row
            tr.append(tdName, tdEvents, tdStatus, tdActions);
            
            // Append row to fragment
            fragment.appendChild(tr);
        });

        tableBody.appendChild(fragment);
    }

    // Helper for Status Badge styling
    function getBadgeClass(status) {
        switch (status?.toLowerCase()) {
            case 'active':
                return 'bg-success-subtle text-success border-success-subtle';
            case 'suspended':
                return 'bg-danger-subtle text-danger border-danger-subtle';
            case 'pending':
                return 'bg-warning-subtle text-warning border-warning-subtle';
            default:
                return 'bg-secondary-subtle text-secondary border-secondary-subtle';
        }
    }

    // -------------------------------------------------------------
    // Helper: Dynamic Pagination Controls
    // -------------------------------------------------------------
    function renderPagination(totalPages) {
        if (!pageNumbersContainer) return;
        pageNumbersContainer.replaceChildren(); // Safe clear

        if (filteredData.length === 0) {
            if (prevPageBtn) prevPageBtn.classList.add('page-disabled');
            if (nextPageBtn) nextPageBtn.classList.add('page-disabled');
            return;
        }

        // Handle Prev button state
        if (currentPage <= 1) {
            prevPageBtn.classList.add('page-disabled');
            prevPageBtn.setAttribute('tabindex', '-1');
            prevPageBtn.setAttribute('aria-disabled', 'true');
        } else {
            prevPageBtn.classList.remove('page-disabled');
            prevPageBtn.removeAttribute('tabindex');
            prevPageBtn.removeAttribute('aria-disabled');
        }

        // Handle Next button state
        if (currentPage >= totalPages) {
            nextPageBtn.classList.add('page-disabled');
            nextPageBtn.setAttribute('tabindex', '-1');
            nextPageBtn.setAttribute('aria-disabled', 'true');
        } else {
            nextPageBtn.classList.remove('page-disabled');
            nextPageBtn.removeAttribute('tabindex');
            nextPageBtn.removeAttribute('aria-disabled');
        }

        // Generate number buttons using .custom-page-link and .page-active
        const fragment = document.createDocumentFragment();

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

            fragment.appendChild(a);
        }

        pageNumbersContainer.appendChild(fragment);
    }

    // -------------------------------------------------------------
    // Event Listeners (Triggers ONLY when Form/Search button is clicked)
    // -------------------------------------------------------------
    if (filterForm) {
        filterForm.addEventListener('submit', (e) => {
            e.preventDefault();
            applyFilters();
        });
    }

    if (prevPageBtn) {
        prevPageBtn.addEventListener('click', (e) => {
            e.preventDefault();
            if (currentPage > 1) {
                currentPage--;
                renderView();
            }
        });
    }

    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const totalPages = Math.ceil(filteredData.length / itemsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                renderView();
            }
        });
    }

    // -------------------------------------------------------------
    // Kickoff
    // -------------------------------------------------------------
    loadCompanyData();
});
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

            // 4. Trigger filter update
            applyFilters();
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
            
            // Once data is loaded, apply initial filters and render
            applyFilters();
        } catch (error) {
            console.error('Error loading company JSON data:', error);
            if (tableBody) {
                tableBody.innerHTML = `<tr><td colspan="4" class="text-center text-danger py-4">Failed to load data.</td></tr>`;
            }
        }
    }

    // -------------------------------------------------------------
    // 2. Filter Logic (Search & Dropdown)
    // -------------------------------------------------------------
    function applyFilters() {
        const searchTerm = (searchInput ? searchInput.value : '').trim().toLowerCase();
        const selectedStatus = selectedStatusValue.toLowerCase();

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
    // Helper: Dynamic Table Row Generator
    // -------------------------------------------------------------
    function renderTableRows(items) {
        if (!tableBody) return;

        if (items.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="4" class="text-center text-white-50 py-4">No companies found matching your query.</td></tr>`;
            return;
        }

        tableBody.innerHTML = items.map(item => {
            const eventCount = item.eventIds ? item.eventIds.length : 0;
            const badgeClass = getBadgeClass(item.status);

            return `
                <tr>
                    <td class="fw-medium text-white">${escapeHtml(item.name)}</td>
                    <td>${eventCount}</td>
                    <td><span class="badge ${badgeClass} border px-2 py-1">${escapeHtml(item.status)}</span></td>
                    <td class="actions-cell">
                        <a href="#" data-id="${item.id}">View</a><span class="action-divider">|</span><a href="#" data-id="${item.id}">Change Status</a>
                    </td>
                </tr>
            `;
        }).join('');
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

    // Helper to prevent HTML injection XSS
    function escapeHtml(str) {
        return (str || '').replace(/[&<>"']/g, match => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[match]));
    }

    // -------------------------------------------------------------
    // Helper: Dynamic Pagination Controls (Copied Design)
    // -------------------------------------------------------------
    function renderPagination(totalPages) {
        if (!pageNumbersContainer) return;
        pageNumbersContainer.innerHTML = '';

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
    // Event Listeners
    // -------------------------------------------------------------
    if (searchInput) searchInput.addEventListener('input', applyFilters);
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
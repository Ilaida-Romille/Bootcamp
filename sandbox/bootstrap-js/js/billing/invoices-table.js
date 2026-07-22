// Helper: Converts billing period strings like "May 2026" or "5/2026" to "2026-05"
function parsePeriodToYearMonth(periodStr) {
    if (!periodStr) return '';
    
    // Parse "Month Year" format (e.g. "May 2026")
    const parts = periodStr.trim().split(' ');
    if (parts.length >= 2) {
        const date = new Date(`${parts[0]} 1, ${parts[1]}`);
        if (!isNaN(date.getTime())) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            return `${year}-${month}`;
        }
    }
    return '';
}

document.addEventListener('DOMContentLoaded', () => {
    const ITEMS_PER_PAGE = 5;
    let currentPage = 1;
    let rawInvoicesData = [];     // Master JSON data
    let filteredInvoicesData = []; // Filtered data after clicking 'Go'

    const JSON_URL = '../data/platform/invoices.json';

    // DOM Elements
    const billingFilterForm = document.getElementById('billingFilterForm');
    const filterOrganizerInput = document.getElementById('filterOrganizer');
    const filterFromInput = document.getElementById('filterFrom');
    const filterToInput = document.getElementById('filterTo');

    const pageNumbersContainer = document.getElementById('pageNumbersContainer');
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const paginationInfo = document.getElementById('pagination-info');

    // Fetch and initialize table data
    fetchData();

    async function fetchData() {
        try {
            const response = await fetch(JSON_URL);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            
            // Store master invoice list
            rawInvoicesData = data.invoices || [];
            
            // Initial render
            applyFilters();
        } catch (error) {
            console.error('Error fetching invoice data:', error);
            const tableBody = document.getElementById('invoice-table-body');
            if (tableBody) {
                renderTableMessage(tableBody, 'Failed to load invoice records.', 'text-danger');
            }
        }
    }

    // -------------------------------------------------------------
    // Filtering Logic (Runs ONLY on 'Go' form submission)
    // -------------------------------------------------------------
    function applyFilters() {
        const query = (filterOrganizerInput ? filterOrganizerInput.value : '').trim().toLowerCase();

        // If the query is between 1 and 2 characters, exit and DO NOT touch the table
        if (query.length > 0 && query.length < 3) {
            return;
        }

        // Month input values return "YYYY-MM" (e.g., "2026-05")
        const fromMonthVal = filterFromInput ? filterFromInput.value : '';
        const toMonthVal = filterToInput ? filterToInput.value : '';

        filteredInvoicesData = rawInvoicesData.filter(item => {
            // 1. Search Query Match (Organizer Name OR Invoice #)
            const matchesOrg = (item.organizerName || '').toLowerCase().includes(query);
            const matchesInvNum = (item.invoiceNumber || '').toLowerCase().includes(query);
            const matchesSearch = query === '' || matchesOrg || matchesInvNum;

            // 2. Month-Year Range Match
            let matchesDate = true;
            
            // Extract "YYYY-MM" from item.billingPeriod (or fallback to item.issueDate)
            const itemYearMonth = parsePeriodToYearMonth(item.billingPeriod || item.issueDate);

            if (itemYearMonth) {
                // Lexicographical string comparison works directly for "YYYY-MM"
                if (fromMonthVal && itemYearMonth < fromMonthVal) {
                    matchesDate = false;
                }
                if (toMonthVal && itemYearMonth > toMonthVal) {
                    matchesDate = false;
                }
            }

            return matchesSearch && matchesDate;
        });

        currentPage = 1; // Reset to page 1 on successful filter execution
        updateView();
    }

    // -------------------------------------------------------------
    // Helper Functions
    // -------------------------------------------------------------
    function renderStatusBadge(status) {
        let badgeClass = 'bg-secondary-subtle text-secondary border-secondary-subtle';
        
        switch (status?.toLowerCase()) {
            case 'paid':
                badgeClass = 'bg-success-subtle text-success border-success-subtle';
                break;
            case 'overdue':
                badgeClass = 'bg-danger-subtle text-danger border-danger-subtle';
                break;
            case 'pending':
                badgeClass = 'bg-warning-subtle text-warning border-warning-subtle';
                break;
        }

        const span = document.createElement('span');
        span.className = `badge ${badgeClass} border px-2 py-1`;
        span.textContent = status || '';
        return span;
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('en-PH', {
            style: 'currency',
            currency: 'PHP',
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }).format(amount).replace('PHP', '₱');
    }

    function renderTableMessage(tableBody, messageText, textClass = 'text-white-50') {
        tableBody.replaceChildren(); // Safe clear

        const tr = document.createElement('tr');
        const td = document.createElement('td');

        td.colSpan = 5;
        td.className = `text-center ${textClass} py-4`;
        td.textContent = messageText;

        tr.appendChild(td);
        tableBody.appendChild(tr);
    }

    // -------------------------------------------------------------
    // Render Functions
    // -------------------------------------------------------------
    function renderTable() {
        const tableBody = document.getElementById('invoice-table-body');
        if (!tableBody) return;

        if (filteredInvoicesData.length === 0) {
            renderTableMessage(tableBody, 'No invoice record/s found.');
            
            if (paginationInfo) {
                paginationInfo.textContent = 'Showing 0 to 0 of 0 entries';
            }
            return;
        }

        tableBody.replaceChildren(); // Safely clear old rows

        const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        const endIndex = Math.min(startIndex + ITEMS_PER_PAGE, filteredInvoicesData.length);
        const currentItems = filteredInvoicesData.slice(startIndex, endIndex);

        const fragment = document.createDocumentFragment();

        currentItems.forEach(item => {
            const calculatedAmount = (item.attendeeCount || 0) * (item.ratePerAttendee || 0);

            const row = document.createElement('tr');

            // 1. Invoice Number
            const tdInvoice = document.createElement('td');
            tdInvoice.className = 'fw-medium text-white';
            tdInvoice.textContent = item.invoiceNumber || 'N/A';

            // 2. Organizer Name
            const tdOrganizer = document.createElement('td');
            tdOrganizer.textContent = item.organizerName || 'N/A';

            // 3. Billing Period
            const tdPeriod = document.createElement('td');
            tdPeriod.textContent = item.billingPeriod || 'N/A';

            // 4. Amount
            const tdAmount = document.createElement('td');
            tdAmount.textContent = formatCurrency(calculatedAmount);

            // 5. Status Badge
            const tdStatus = document.createElement('td');
            tdStatus.appendChild(renderStatusBadge(item.status));

            row.append(tdInvoice, tdOrganizer, tdPeriod, tdAmount, tdStatus);
            fragment.appendChild(row);
        });

        tableBody.appendChild(fragment);

        if (paginationInfo) {
            paginationInfo.textContent = `Showing ${startIndex + 1} to ${endIndex} of ${filteredInvoicesData.length} entries`;
        }
    }

    function renderPagination() {
        if (!pageNumbersContainer) return;

        pageNumbersContainer.replaceChildren(); // Safely clear existing buttons
        
        const totalPages = Math.ceil(filteredInvoicesData.length / ITEMS_PER_PAGE) || 1;

        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        if (prevPageBtn) {
            if (currentPage <= 1 || filteredInvoicesData.length === 0) {
                prevPageBtn.classList.add('page-disabled');
                prevPageBtn.setAttribute('tabindex', '-1');
                prevPageBtn.setAttribute('aria-disabled', 'true');
            } else {
                prevPageBtn.classList.remove('page-disabled');
                prevPageBtn.removeAttribute('tabindex');
                prevPageBtn.removeAttribute('aria-disabled');
            }
        }

        if (nextPageBtn) {
            if (currentPage >= totalPages || filteredInvoicesData.length === 0) {
                nextPageBtn.classList.add('page-disabled');
                nextPageBtn.setAttribute('tabindex', '-1');
                nextPageBtn.setAttribute('aria-disabled', 'true');
            } else {
                nextPageBtn.classList.remove('page-disabled');
                nextPageBtn.removeAttribute('tabindex');
                nextPageBtn.removeAttribute('aria-disabled');
            }
        }

        if (filteredInvoicesData.length === 0) return;

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
                    updateView();
                }
            });

            fragment.appendChild(a);
        }

        pageNumbersContainer.appendChild(fragment);
    }

    // -------------------------------------------------------------
    // Event Listeners
    // -------------------------------------------------------------
    if (billingFilterForm) {
        billingFilterForm.addEventListener('submit', (e) => {
            e.preventDefault();
            applyFilters();
        });
    }

    if (prevPageBtn) {
        prevPageBtn.addEventListener('click', (e) => {
            e.preventDefault();
            if (currentPage > 1) {
                currentPage--;
                updateView();
            }
        });
    }

    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const totalPages = Math.ceil(filteredInvoicesData.length / ITEMS_PER_PAGE);
            if (currentPage < totalPages) {
                currentPage++;
                updateView();
            }
        });
    }

    function updateView() {
        renderTable();
        renderPagination();
    }
});
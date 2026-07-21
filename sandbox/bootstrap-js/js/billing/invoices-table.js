document.addEventListener('DOMContentLoaded', () => {
    const ITEMS_PER_PAGE = 5;
    let currentPage = 1;
    let invoicesData = [];

    // Replace with the path to your JSON file or API endpoint
    const JSON_URL = '../data/platform/invoices.json';

    // DOM Elements
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
            
            // Assign invoices from JSON source
            invoicesData = data.invoices || [];
            
            updateView();
        } catch (error) {
            console.error('Error fetching invoice data:', error);
            const tableBody = document.getElementById('invoice-table-body');
            if (tableBody) {
                tableBody.innerHTML = `
                    <tr>
                        <td colspan="5" class="text-center text-danger py-4">
                            Failed to load invoice records.
                        </td>
                    </tr>`;
            }
        }
    }

    // Helper: Formats status string to appropriate Bootstrap badge classes
    function getStatusBadge(status) {
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

        return `<span class="badge ${badgeClass} border px-2 py-1">${status}</span>`;
    }

    // Helper: Formats numbers into currency format (PHP)
    function formatCurrency(amount) {
        return new Intl.NumberFormat('en-PH', {
            style: 'currency',
            currency: 'PHP',
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }).format(amount).replace('PHP', '₱');
    }

    // Render items for current active page
    function renderTable() {
        const tableBody = document.getElementById('invoice-table-body');
        if (!tableBody) return;

        tableBody.innerHTML = '';

        if (invoicesData.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center text-white-50 py-4">
                        No invoice records found.
                    </td>
                </tr>`;
            
            if (paginationInfo) {
                paginationInfo.textContent = 'Showing 0 to 0 of 0 entries';
            }
            return;
        }

        const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        const endIndex = Math.min(startIndex + ITEMS_PER_PAGE, invoicesData.length);
        const currentItems = invoicesData.slice(startIndex, endIndex);

        currentItems.forEach(item => {
            // Calculate Amount dynamically: attendeeCount * ratePerAttendee
            const calculatedAmount = (item.attendeeCount || 0) * (item.ratePerAttendee || 0);

            const row = document.createElement('tr');
            row.innerHTML = `
                <td class="fw-medium text-white">${item.invoiceNumber || 'N/A'}</td>
                <td>${item.organizerName || 'N/A'}</td>
                <td>${item.billingPeriod || 'N/A'}</td>
                <td>${formatCurrency(calculatedAmount)}</td>
                <td>${getStatusBadge(item.status)}</td>
            `;
            tableBody.appendChild(row);
        });

        // Update footer info (e.g., "Showing 1 to 5 of 22 entries")
        if (paginationInfo) {
            paginationInfo.textContent = `Showing ${startIndex + 1} to ${endIndex} of ${invoicesData.length} entries`;
        }
    }

    // Render pagination buttons dynamically matching custom design
    function renderPagination() {
        if (!pageNumbersContainer) return;

        pageNumbersContainer.innerHTML = '';
        const totalPages = Math.ceil(invoicesData.length / ITEMS_PER_PAGE) || 1;

        // Ensure currentPage stays within valid boundaries
        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        // Update Prev button status
        if (prevPageBtn) {
            if (currentPage <= 1 || invoicesData.length === 0) {
                prevPageBtn.classList.add('page-disabled');
                prevPageBtn.setAttribute('tabindex', '-1');
                prevPageBtn.setAttribute('aria-disabled', 'true');
            } else {
                prevPageBtn.classList.remove('page-disabled');
                prevPageBtn.removeAttribute('tabindex');
                prevPageBtn.removeAttribute('aria-disabled');
            }
        }

        // Update Next button status
        if (nextPageBtn) {
            if (currentPage >= totalPages || invoicesData.length === 0) {
                nextPageBtn.classList.add('page-disabled');
                nextPageBtn.setAttribute('tabindex', '-1');
                nextPageBtn.setAttribute('aria-disabled', 'true');
            } else {
                nextPageBtn.classList.remove('page-disabled');
                nextPageBtn.removeAttribute('tabindex');
                nextPageBtn.removeAttribute('aria-disabled');
            }
        }

        if (invoicesData.length === 0) return;

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
                    updateView();
                }
            });

            pageNumbersContainer.appendChild(a);
        }
    }

    // Prev / Next button listeners
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
            const totalPages = Math.ceil(invoicesData.length / ITEMS_PER_PAGE);
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
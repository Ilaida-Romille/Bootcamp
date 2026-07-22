// --- Pure Utility Math & Formatting Functions ---

let allInvoices = [];
let filteredInvoices = [];
let currentPage = 1;
const rowsPerPage = 3;

/**
 * Pure function to calculate total invoice amount
 */
function calculateInvoiceTotal(attendeeCount = 0, safeRate = 0) {
    return attendeeCount * safeRate;
}

/**
 * Pure function to format amounts into PHP currency strings
 */
function formatCurrencyPHP(amount = 0) {
    const safeAmount = amount;
    const formatted = new Intl.NumberFormat('en-PH', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(safeAmount);

    return `₱ ${formatted}`;
}

/**
 * Helper function to trigger the error toast notification
 */
function showErrorToast(message) {
    const toastEl = document.getElementById('errorToast');
    const toastBody = document.getElementById('errorToastBody');
    
    if (toastEl && toastBody) {
        toastBody.textContent = message;
        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    } else {
        alert(message);
    }
}

/**
 * Fetches JSON, calculates necessary invoice figures, populates modal, and triggers display.
 */
async function processAndShowInvoice(organizerQuery, monthQuery) {
    try {
        const response = await fetch('../data/platform/invoices.json');
        if (!response.ok) throw new Error('Failed to load JSON data');
        
        const data = await response.json();

        // Perform case-insensitive match on organizer name, organizer ID, or invoice number
        const matchedInvoice = data.invoices.find(inv => {
            const orgNameMatch = inv.organizerName?.toLowerCase().includes(organizerQuery.toLowerCase());
            const orgIdMatch = inv.organizerId?.toLowerCase().includes(organizerQuery.toLowerCase());
            const invoiceNumMatch = inv.invoiceNumber?.toLowerCase().includes(organizerQuery.toLowerCase());
            const monthMatch = monthQuery ? inv.billingPeriod === monthQuery : true;
            
            return (orgNameMatch || orgIdMatch || invoiceNumMatch) && monthMatch;
        });

        if (!matchedInvoice) {
            showErrorToast(`No billing record found for "${organizerQuery}".`);
            return;
        }

        populateAndShowModal(matchedInvoice, data.organizers);

    } catch (error) {
        console.error('Error generating invoice preview:', error);
        showErrorToast('An error occurred while attempting to generate the invoice.');
    }
}

/**
 * Processes batch invoices and renders ALL matching invoices into the batch modal table safely
 */
async function processBatchInvoices(targetMonth) {
    try {
        const response = await fetch('../data/platform/invoices.json');
        if (!response.ok) throw new Error('Failed to load JSON data');

        const data = await response.json();

        const batchInvoices = data.invoices.filter(inv => inv.billingPeriod === targetMonth);

        if (!batchInvoices.length) {
            showErrorToast(`No records found for batch processing for ${targetMonth}.`);
            return;
        }

        let totalAmount = 0;
        let totalAttendees = 0;

        batchInvoices.forEach(inv => {
            totalAmount += calculateInvoiceTotal(inv.attendeeCount, inv.ratePerAttendee);
            totalAttendees += inv.attendeeCount ?? 0;
        });

        document.getElementById('batchPeriodBadge').textContent = targetMonth;
        document.getElementById('batchTotalCount').textContent = batchInvoices.length;
        document.getElementById('batchTotalAttendees').textContent = totalAttendees.toLocaleString();
        document.getElementById('batchTotalAmount').textContent = formatCurrencyPHP(totalAmount);

        const tbody = document.getElementById('batchInvoicesTableBody');
        tbody.replaceChildren();

        const fragment = document.createDocumentFragment();

        batchInvoices.forEach(inv => {
            const rowTotal = calculateInvoiceTotal(inv.attendeeCount, inv.ratePerAttendee);
            const statusClass = inv.status === 'Paid' 
                ? 'bg-success-subtle text-success border-success-subtle' 
                : inv.status === 'Overdue' 
                ? 'bg-danger-subtle text-danger border-danger-subtle' 
                : 'bg-warning-subtle text-warning border-warning-subtle';

            const tr = document.createElement('tr');

            const tdInvoice = document.createElement('td');
            tdInvoice.className = 'fw-semibold text-white';
            tdInvoice.textContent = inv.invoiceNumber ?? '';

            const tdOrganizer = document.createElement('td');
            tdOrganizer.textContent = inv.organizerName ?? '';

            const tdAttendees = document.createElement('td');
            tdAttendees.className = 'text-center';
            tdAttendees.textContent = (inv.attendeeCount ?? 0).toLocaleString();

            const tdAmount = document.createElement('td');
            tdAmount.className = 'text-end fw-bold text-white';
            tdAmount.textContent = formatCurrencyPHP(rowTotal);

            const tdStatus = document.createElement('td');
            tdStatus.className = 'text-center';
            
            const badgeSpan = document.createElement('span');
            badgeSpan.className = `badge ${statusClass} border px-2 py-1`;
            badgeSpan.textContent = inv.status ?? 'Pending';

            tdStatus.appendChild(badgeSpan);

            tr.appendChild(tdInvoice);
            tr.appendChild(tdOrganizer);
            tr.appendChild(tdAttendees);
            tr.appendChild(tdAmount);
            tr.appendChild(tdStatus);

            fragment.appendChild(tr);
        });

        tbody.appendChild(fragment);

        const batchModalEl = document.getElementById('batchSummaryModal');
        const bsModal = new bootstrap.Modal(batchModalEl);
        bsModal.show();

    } catch (error) {
        console.error('Error processing batch invoices:', error);
        showErrorToast('An error occurred while generating batch invoices.');
    }
}

/**
 * Helper to populate the Invoice Modal DOM elements and trigger display
 */
function populateAndShowModal(invoice, organizersList = []) {
    const totalAmount = calculateInvoiceTotal(invoice.attendeeCount, invoice.ratePerAttendee);
    const formattedTotal = formatCurrencyPHP(totalAmount);
    const formattedRate = formatCurrencyPHP(invoice.ratePerAttendee);

    document.getElementById('modalInvoiceNum').textContent = invoice.invoiceNumber;
    document.getElementById('modalBillingPeriod').textContent = invoice.billingPeriod;
    document.getElementById('modalOrganizerName').textContent = invoice.organizerName;
    
    const orgInfo = organizersList.find(o => o.id === invoice.organizerId);
    document.getElementById('modalOrganizerEmail').textContent = orgInfo?.email ?? 'billing@organizer.com';

    document.getElementById('modalAttendeeCount').textContent = (invoice.attendeeCount ?? 0).toLocaleString();
    document.getElementById('modalRatePerAttendee').textContent = formattedRate;
    document.getElementById('modalLineTotal').textContent = formattedTotal;
    document.getElementById('modalSubtotal').textContent = formattedTotal;
    document.getElementById('modalTotalAmount').textContent = formattedTotal;

    const badge = document.getElementById('modalStatusBadge');
    const status = invoice.status ?? 'Pending';
    badge.textContent = status;
    
    if (status === 'Paid') {
        badge.className = 'badge bg-success-subtle text-success border border-success-subtle fs-6 ms-2';
    } else if (status === 'Overdue') {
        badge.className = 'badge bg-danger-subtle text-danger border border-danger-subtle fs-6 ms-2';
    } else {
        badge.className = 'badge bg-warning-subtle text-warning border border-warning-subtle fs-6 ms-2';
    }

    const invoiceModalEl = document.getElementById('invoiceModal');
    const bsModal = new bootstrap.Modal(invoiceModalEl);
    bsModal.show();
}

/**
 * Loads Invoices from invoices.json into the Dropup menu showing Invoice ID
 */
async function loadOrganizersDropup() {
    const genOrgMenuList = document.getElementById('genOrgMenuList');
    const genOrgBtn = document.getElementById('genOrgBtn');
    const filterOrganizerInput = document.getElementById('filterOrganizer');

    if (!genOrgMenuList || !genOrgBtn) return;

    try {
        const response = await fetch('../data/platform/invoices.json');
        if (!response.ok) throw new Error('Failed to load JSON data');
        
        const data = await response.json();
        const invoices = data.invoices || [];

        genOrgMenuList.replaceChildren();
        const fragment = document.createDocumentFragment();

        invoices.forEach(inv => {
            const li = document.createElement('li');
            const a = document.createElement('a');
            
            // Format to show Organizer Name and Invoice ID
            const displayText = `${inv.organizerName} - ${inv.invoiceNumber}`;
            
            a.className = 'dropdown-item text-truncate';
            a.title = displayText;
            a.href = '#';
            a.setAttribute('data-value', inv.organizerName);
            a.setAttribute('data-invoice-num', inv.invoiceNumber);
            a.setAttribute('data-id', inv.organizerId || '');
            a.textContent = displayText;

            a.addEventListener('click', (e) => {
                e.preventDefault();

                // 1. Update Dropup Button text and data attribute
                const btnSpan = genOrgBtn.querySelector('span');
                if (btnSpan) {
                    btnSpan.textContent = displayText;
                } else {
                    genOrgBtn.textContent = displayText;
                }
                genOrgBtn.setAttribute('data-value', inv.invoiceNumber);

                // 2. Sync to search filter section input
                if (filterOrganizerInput) {
                    filterOrganizerInput.value = inv.invoiceNumber;
                }

                // 3. Update active state in menu
                genOrgMenuList.querySelectorAll('.dropdown-item').forEach(i => i.classList.remove('active'));
                a.classList.add('active');

                // 4. Close the dropup menu cleanly after selection
                const bsDropdown = bootstrap.Dropdown.getInstance(genOrgBtn) || new bootstrap.Dropdown(genOrgBtn);
                if (bsDropdown) {
                    bsDropdown.hide();
                }
            });

            li.appendChild(a);
            fragment.appendChild(li);
        });

        genOrgMenuList.appendChild(fragment);

    } catch (error) {
        console.error('Error loading invoices into dropup:', error);
    }
}

// --- Main App Logic ---

document.addEventListener('DOMContentLoaded', () => {
    const genOrgBtn = document.getElementById('genOrgBtn');
    const genMonthBtn = document.getElementById('genMonthBtn');
    const genMonthDropdown = document.getElementById('genMonthDropdown');
    const specificForm = genOrgBtn?.closest('form');

    // Load Invoices Dropup
    loadOrganizersDropup();

    // 1. HANDLE MONTH DROPDOWN SELECTION
    if (genMonthDropdown && genMonthBtn) {
        const dropdownItems = genMonthDropdown.querySelectorAll('.dropdown-item');
        
        dropdownItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                
                const selectedValue = item.getAttribute('data-value');
                const selectedText = item.textContent.trim();

                const btnSpan = genMonthBtn.querySelector('span');
                if (btnSpan) {
                    btnSpan.textContent = selectedText;
                } else {
                    genMonthBtn.textContent = selectedText;
                }
                
                genMonthBtn.setAttribute('data-value', selectedValue);

                dropdownItems.forEach(i => i.classList.remove('active'));
                item.classList.add('active');
            });
        });
    }

    // 2. BATCH BUTTON
    const batchBtn = document.getElementById('btnGenerateBatch') || 
                     document.getElementById('btnBatchGenerate') || 
                     document.querySelector('.glass-generation-card button.custom-action-btn');

    // 3. SPECIFIC ORGANIZER FORM SUBMISSION
    if (specificForm) {
        specificForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const selectedQuery = genOrgBtn ? genOrgBtn.getAttribute('data-value') : '';
            const selectedMonth = genMonthBtn ? genMonthBtn.getAttribute('data-value') : '';

            if (!selectedQuery) {
                showErrorToast('Please select an Invoice or Organizer.');
                return;
            }

            await processAndShowInvoice(selectedQuery, selectedMonth);
        });
    }

    // 4. BATCH GENERATION BUTTON LISTENER
    if (batchBtn) {
        batchBtn.addEventListener('click', async () => {
            const targetMonth = "June 2026";
            await processBatchInvoices(targetMonth);
        });
    }
});
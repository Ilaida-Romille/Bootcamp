// --- Pure Utility Math & Formatting Functions ---

/**
 * Pure function to calculate total invoice amount
 */

let allInvoices = [];
let filteredInvoices = [];
let currentPage = 1;
const rowsPerPage = 3; // Enforce max 3 rows per page

function calculateInvoiceTotal(attendeeCount = 0, safeRate = 0) {
    return attendeeCount * safeRate;
}

/**
 * Pure function to format amounts into PHP currency strings (e.g., "₱ 84,200.00")
 */
function formatCurrencyPHP(amount = 0) {
    const safeAmount = amount;
    const formatted = new Intl.NumberFormat('en-PH', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(safeAmount);

    return `₱ ${formatted}`;
}

// --- Main App Logic ---

document.addEventListener('DOMContentLoaded', () => {
    // Select input controls and form
    const genOrgInput = document.getElementById('genOrganizer');
    const genMonthSelect = document.getElementById('genMonth');
    const specificForm = genOrgInput?.closest('form');

    // 1. SELECT THE BATCH BUTTON
    // (Ensure your HTML button has id="btnBatchGenerate" or class "custom-action-btn")
    const batchBtn = document.getElementById('btnBatchGenerate') || 
                     document.querySelector('.glass-generation-card button.custom-action-btn');

    // Attach event listener to Specific Organizer form submission
    if (specificForm) {
        specificForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const selectedOrgName = genOrgInput.value.trim();
            const selectedMonth = genMonthSelect.value;

            if (!selectedOrgName || !selectedMonth) {
                alert('Please provide both an Organizer Name and select a Month.');
                return;
            }

            await processAndShowInvoice(selectedOrgName, selectedMonth);
        });
    }

    // 2. ATTACH LISTENER FOR BATCH GENERATION BUTTON
    if (batchBtn) {
        batchBtn.addEventListener('click', async () => {
            // Target month for batch processing
            const targetMonth = "June 2026";
            await processBatchInvoices(targetMonth);
        });
    }
});

/**
 * Fetches JSON, calculates necessary invoice figures, populates modal, and triggers display.
 */
async function processAndShowInvoice(organizerQuery, monthQuery) {
    try {
        // Fetch JSON located in the same folder
        const response = await fetch('../js/billing/invoices.json');
        if (!response.ok) throw new Error('Failed to load JSON data');
        
        const data = await response.json();

        // Perform case-insensitive match on organizer name and exact match on month
        const matchedInvoice = data.invoices.find(inv => {
            const orgMatch = inv.organizerName.toLowerCase().includes(organizerQuery.toLowerCase());
            const monthMatch = inv.billingPeriod === monthQuery;
            return orgMatch && monthMatch;
        });

        if (!matchedInvoice) {
            alert(`No invoice usage records found for "${organizerQuery}" in ${monthQuery}.`);
            return;
        }

        // 3. REFACTORED: Use the modular modal helper function instead of repeating DOM code
        populateAndShowModal(matchedInvoice, data.organizers);

    } catch (error) {
        console.error('Error generating invoice preview:', error);
        alert('An error occurred while attempting to generate the invoice.');
    }
}

/**
 * Handles batch invoice processing and directly triggers the Modal
 */
/**
 * Processes batch invoices and renders ALL matching invoices into the batch modal table
 */
async function processBatchInvoices(targetMonth) {
    try {
        const response = await fetch('../js/billing/invoices.json');
        if (!response.ok) throw new Error('Failed to load JSON data');

        const data = await response.json();

        // 1. Get ALL 5 invoices for June 2026
        const batchInvoices = data.invoices.filter(inv => inv.billingPeriod === targetMonth);

        if (!batchInvoices.length) {
            alert(`No records found for batch processing for ${targetMonth}.`);
            return;
        }

        // 2. Calculate aggregate statistics
        let totalAmount = 0;
        let totalAttendees = 0;

        batchInvoices.forEach(inv => {
            totalAmount += calculateInvoiceTotal(inv.attendeeCount, inv.ratePerAttendee);
            totalAttendees += inv.attendeeCount ?? 0;
        });

        // 3. Update Modal Header Stats
        document.getElementById('batchPeriodBadge').textContent = targetMonth;
        document.getElementById('batchTotalCount').textContent = batchInvoices.length; // Shows 5
        document.getElementById('batchTotalAttendees').textContent = totalAttendees.toLocaleString();
        document.getElementById('batchTotalAmount').textContent = formatCurrencyPHP(totalAmount);

        // 4. Inject ALL 5 Invoices into the Table Body
        const tbody = document.getElementById('batchInvoicesTableBody');
        tbody.innerHTML = batchInvoices.map(inv => {
            const rowTotal = calculateInvoiceTotal(inv.attendeeCount, inv.ratePerAttendee);
            const statusClass = inv.status === 'Paid' 
                ? 'bg-success-subtle text-success border-success-subtle' 
                : inv.status === 'Overdue' 
                ? 'bg-danger-subtle text-danger border-danger-subtle' 
                : 'bg-warning-subtle text-warning border-warning-subtle';

            return `
                <tr>
                    <td class="fw-semibold text-white">${inv.invoiceNumber}</td>
                    <td>${inv.organizerName}</td>
                    <td class="text-center">${(inv.attendeeCount ?? 0).toLocaleString()}</td>
                    <td class="text-end fw-bold text-white">${formatCurrencyPHP(rowTotal)}</td>
                    <td class="text-center">
                        <span class="badge ${statusClass} border px-2 py-1">${inv.status ?? 'Pending'}</span>
                    </td>
                </tr>
            `;
        }).join('');

        // 5. Open the Batch Modal showing all 5 records
        const batchModalEl = document.getElementById('batchSummaryModal');
        const bsModal = new bootstrap.Modal(batchModalEl);
        bsModal.show();

    } catch (error) {
        console.error('Error processing batch invoices:', error);
        alert('An error occurred while generating batch invoices.');
    }
}

/**
 * Helper to populate the Invoice Modal DOM elements and trigger display
 */
function populateAndShowModal(invoice, organizersList = []) {
    const totalAmount = calculateInvoiceTotal(invoice.attendeeCount, invoice.ratePerAttendee);
    const formattedTotal = formatCurrencyPHP(totalAmount);
    const formattedRate = formatCurrencyPHP(invoice.ratePerAttendee);

    // Populate Modal Fields
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

    // Configure Status Badge dynamically
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

    // Trigger Bootstrap Modal Display
    const invoiceModalEl = document.getElementById('invoiceModal');
    const bsModal = new bootstrap.Modal(invoiceModalEl);
    bsModal.show();
}
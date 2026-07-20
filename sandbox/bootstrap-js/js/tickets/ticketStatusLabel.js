let loadedTickets = [];

/**
 * Status Badge mapping helper function using if/else if/else
 */
function ticketStatusLabel(status) {
    let label = "";
    let badgeClass = "";

    if (status === "Open") {
        label = "Open";
        badgeClass = "badge-danger";
    } else if (status === "In Progress") {
        label = "In Progress";
        badgeClass = "badge-warning";
    } else if (status === "Resolved") {
        label = "Resolved";
        badgeClass = "badge-success";
    } else {
        label = status || "Unknown";
        badgeClass = "badge-secondary";
    }

    return {
        label: label,
        badgeClass: badgeClass
    };
}

/**
 * Render left list stack
 */
function renderTicketsList(tickets) {
    const container = document.getElementById("ticketsListContainer");
    container.innerHTML = "";

    tickets.forEach((ticket, index) => {
        const statusInfo = ticketStatusLabel(ticket.status);
        const itemDiv = document.createElement("div");
        itemDiv.className = `ticket-item p-3 mb-2 rounded border border-dark ${index === 0 ? 'active' : ''}`;
        itemDiv.setAttribute("data-index", index);
        
        itemDiv.onclick = function() {
            document.querySelectorAll('.ticket-item').forEach(el => el.classList.remove('active'));
            this.classList.add('active');
            renderTicketDetail(tickets[index]);
        };

        itemDiv.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-1">
                <strong class="text-white">#${ticket.ticketId}</strong>
                <span class="badge ${statusInfo.badgeClass} px-2 py-1">${statusInfo.label}</span>
            </div>
            <div class="text-light small fw-semibold">${ticket.organizer ? ticket.organizer.name : 'Unknown'}</div>
            <div class="text-secondary small">${ticket.subject}</div>
        `;
        
        container.appendChild(itemDiv);
    });
}

/**
 * Render right-hand detail panel
 */
function renderTicketDetail(ticket) {
    const statusInfo = ticketStatusLabel(ticket.status);
    const detailPanel = document.getElementById("ticketDetailPanel");
    
    // Format opened timestamp
    const openedDate = new Date(ticket.openedAt).toLocaleDateString("en-US", {
        month: 'short', day: 'numeric', year: 'numeric'
    });

    detailPanel.innerHTML = `
        <h2 class="h5 fw-bold text-white mb-2">#${ticket.ticketId} — ${ticket.subject}</h2>
        
        <div class="small text-secondary mb-4">
            From: <span class="fw-medium text-white">${ticket.organizer ? ticket.organizer.name : 'N/A'}</span> 
            <span class="mx-1">•</span> Opened: ${openedDate} 
            <span class="mx-1">•</span> Status: <span class="badge ${statusInfo.badgeClass}">${statusInfo.label}</span>
            ${ticket.processArea ? `<span class="mx-1">•</span> Category: <span class="text-info">${ticket.processArea}</span>` : ''}
        </div>

        <div class="p-3 bg-dark border border-secondary rounded-2 mb-4">
            <p class="mb-0 text-light fst-italic">“${ticket.message}”</p>
        </div>

        <form onsubmit="event.preventDefault();">
            <div class="mb-3">
                <label for="userResponse" class="form-label small text-secondary mb-2">Your Response</label>
                <textarea id="userResponse" class="form-control bg-dark text-light border-secondary" rows="4"></textarea>
            </div>
            
            <div class="d-flex gap-3">
                <button type="submit" class="btn btn-primary px-4">Send Response</button>
                <button type="button" class="btn btn-outline-light px-4">Mark Resolved</button>
            </div>
        </form>
    `;
}

/**
 * Fetch JSON file asynchronously on load
 */
document.addEventListener("DOMContentLoaded", () => {
    fetch("../js/tickets/tickets.json")
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            loadedTickets = data;
            renderTicketsList(loadedTickets);
            if (loadedTickets.length > 0) {
                renderTicketDetail(loadedTickets[0]);
            }
        })
        .catch(error => {
            console.error("Error loading tickets.json:", error);
            document.getElementById("ticketsListContainer").innerHTML = 
                `<div class="text-danger p-3">Failed to load tickets. Ensure you are serving the page via a local server (e.g. Live Server).</div>`;
        });
});
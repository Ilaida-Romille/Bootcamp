document.addEventListener('DOMContentLoaded', () => {
    const attendeeListElement = document.querySelector('.custom-attendee-list');
    if (!attendeeListElement) return;

    // 1. Identify active Event ID (Check URI params first, then fallback to localStorage)
    const urlParams = new URLSearchParams(window.location.search);
    const currentEventId = urlParams.get('id') || localStorage.getItem('eventHub_currentEventId') || 'EVT-001';

    // 2. Mock initial attendees (simulating initial JSON/database content)
    const mockAttendees = [
        { id: "ATT-001", name: "Maria Santos", company: "Acme Events Co.", eventId: "EVT-001" },
        { id: "ATT-002", name: "Liam Cruz", company: "Northwind Corp.", eventId: "EVT-001" },
        { id: "ATT-003", name: "Ana Reyes", company: "Initech PH", eventId: "EVT-001" }
    ];

    // 3. Read registrants array saved in localStorage from registration.html
    const localRegistrants = JSON.parse(localStorage.getItem('eventHub_registrants')) || [];

    // Debugging console log to verify reading from storage
    console.log('--- localStorage Debugger ---');
    console.log('Active Event ID:', currentEventId);
    console.log('Raw localStorage registrants:', localRegistrants);

    // 4. Filter stored registrants matching this specific event ID
    const eventRegistrants = localRegistrants.filter(att => att.eventId === currentEventId);
    console.log(`Filtered registrants for ${currentEventId}:`, eventRegistrants);

    // 5. Combine mock data with local data avoiding duplicate email/names
    const combinedAttendees = [...mockAttendees.filter(att => att.eventId === currentEventId)];

    eventRegistrants.forEach(newAtt => {
        const exists = combinedAttendees.some(
            att => (att.email && att.email.toLowerCase() === newAtt.email.toLowerCase()) || 
                    att.name.toLowerCase() === newAtt.name.toLowerCase()
        );
        if (!exists) {
            combinedAttendees.push(newAtt);
        }
    });

    // 6. Clear static placeholder markup inside `.custom-attendee-list`
    attendeeListElement.innerHTML = '';

    // 7. Render dynamic attendee list items
    combinedAttendees.forEach(attendee => {
        const li = document.createElement('li');
        li.className = 'd-flex align-items-center gap-3 custom-attendee-item';
        li.innerHTML = `
            <div class="avatar-placeholder">
                <img src="../img/attendee/attendee_50x50.png" alt="Attendee Icon" class="attendee-avatar-img">
            </div>
            <span class="small text-white attendee-info">
                ${escapeHTML(attendee.name)} — <span class="text-secondary">${escapeHTML(attendee.company || 'N/A')}</span>
            </span>
        `;
        attendeeListElement.appendChild(li);
    });

    // 8. Append attendee total count metadata item at the bottom
    const metaLi = document.createElement('li');
    metaLi.className = 'd-flex align-items-center gap-3 custom-attendee-item meta-item';
    metaLi.innerHTML = `
        <div class="avatar-placeholder">
            <img src="../img/attendee/attendee_50x50.png" alt="Attendee Icon" class="attendee-avatar-img">
        </div>
        <span class="small text-secondary attendee-info fst-italic">+ ${Math.max(0, 120 - combinedAttendees.length)} more attendees</span>
    `;
    attendeeListElement.appendChild(metaLi);
});

// Helper function to sanitize user text rendering
function escapeHTML(str) {
    return String(str).replace(/[&<>"']/g, match => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    }[match]));
}
document.addEventListener('DOMContentLoaded', function() {
    // Update attendance graphs
    updateAttendanceGraphs();
    
    // Handle RSVP status changes
    const statusSelects = document.querySelectorAll('.status-select');
    if (statusSelects) {
        statusSelects.forEach(select => {
            select.addEventListener('change', function() {
                const rsvpId = this.dataset.rsvpId;
                const eventId = this.dataset.eventId;
                const newStatus = this.value;
                
                // Make an AJAX request to update the RSVP status
                updateRSVPStatus(rsvpId, eventId, newStatus);
            });
        });
    }
    
    // Handle RSVP button
    const rsvpButton = document.getElementById('rsvpButton');
    if (rsvpButton) {
        rsvpButton.addEventListener('click', function(event) {
            const eventId = this.dataset.eventId;
            const capacity = parseInt(this.dataset.capacity);
            const attendees = parseInt(this.dataset.attendees);
            
            if (attendees >= capacity) {
                alert('This event is at full capacity. You cannot RSVP at this time.');
                event.preventDefault();
                return;
            }
        });
    }
});

function updateAttendanceGraphs() {
    const progressBars = document.querySelectorAll('.progress-bar');
    progressBars.forEach(bar => {
        const attendees = parseInt(bar.dataset.attendees);
        const capacity = parseInt(bar.dataset.capacity);
        const percentage = (attendees / capacity) * 100;
        
        bar.style.width = `${percentage}%`;
        
        // Change color based on attendance
        if (percentage >= 90) {
            bar.style.backgroundColor = '#dc3545'; // Almost full (red)
        } else if (percentage >= 70) {
            bar.style.backgroundColor = '#ffc107'; // Getting full (yellow)
        } else {
            bar.style.backgroundColor = '#28a745'; // Plenty of space (green)
        }
    });
}

function updateRSVPStatus(rsvpId, eventId, status) {
    // This is a placeholder for a real AJAX call
    // In a real application, you would make an API request to update the RSVP
    
    fetch(`/rsvp/updateStatus?id=${rsvpId}&eventId=${eventId}&status=${status}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to update RSVP status');
        }
        return response.json();
    })
    .then(data => {
        // Success message
        const statusCell = document.querySelector(`#rsvp-${rsvpId} .status-cell`);
        if (statusCell) {
            // Update the status badge
            statusCell.innerHTML = `<span class="status-badge ${status}">${status}</span>`;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to update RSVP status. Please try again.');
    });
}
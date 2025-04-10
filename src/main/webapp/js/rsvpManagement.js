document.addEventListener('DOMContentLoaded', function() {
    // Get the context path from a hidden input or meta tag
    const contextPath = document.querySelector('meta[name="contextPath"]')?.content || '';
    
    // Update attendance graphs
    updateAttendanceGraphs();
    
    // Handle RSVP status changes (if you add this feature later)
    const statusSelects = document.querySelectorAll('.status-select');
    if (statusSelects && statusSelects.length > 0) {
        statusSelects.forEach(select => {
            select.addEventListener('change', function() {
                const rsvpId = this.dataset.rsvpId;
                const eventId = this.dataset.eventId;
                const newStatus = this.value;
                
                // Make an AJAX request to update the RSVP status
                updateRSVPStatus(rsvpId, eventId, newStatus, contextPath);
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
    
    // Handle RSVP removal confirmation
    const removeButtons = document.querySelectorAll('.btn-danger[onclick^="return confirm"]');
    if (removeButtons && removeButtons.length > 0) {
        removeButtons.forEach(button => {
            // Remove the inline onclick and add a proper event listener
            const confirmMessage = button.getAttribute('onclick').replace('return confirm(\'', '').replace('\')', '');
            button.removeAttribute('onclick');
            
            button.addEventListener('click', function(event) {
                if (!confirm(confirmMessage)) {
                    event.preventDefault();
                }
            });
        });
    }
});

function updateAttendanceGraphs() {
    // This function no longer needs to set the width and color as it's handled in the JSP
    // But we can add dynamic features if needed
    
    const progressBars = document.querySelectorAll('.progress-bar');
    if (progressBars && progressBars.length > 0) {
        progressBars.forEach(bar => {
            // You can add animations or other dynamic behaviors here if desired
            // For example, animate the progress bar filling up
            bar.style.transition = 'width 1s ease-in-out';
        });
    }
}

function updateRSVPStatus(rsvpId, eventId, status, contextPath) {
    // Updated fetch with context path
    fetch(`${contextPath}/rsvp/updateStatus?id=${rsvpId}&eventId=${eventId}&status=${status}`, {
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
        const statusCell = document.querySelector(`tr[data-rsvp-id="${rsvpId}"] .status-cell`);
        if (statusCell) {
            // Update the status badge
            const badgeClass = status === 'Confirmed' ? 'bg-success' : 'bg-warning';
            statusCell.innerHTML = `<span class="badge ${badgeClass}">${status}</span>`;
        }
        
        // Show success message
        showMessage('RSVP status updated successfully', 'success');
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('Failed to update RSVP status. Please try again.', 'danger');
    });
}

function showMessage(message, type) {
    // Create a toast-like message that fades after a few seconds
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    alertDiv.style.top = '20px';
    alertDiv.style.right = '20px';
    alertDiv.style.zIndex = '9999';
    
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    // Auto-dismiss after 3 seconds
    setTimeout(() => {
        alertDiv.classList.remove('show');
        setTimeout(() => alertDiv.remove(), 150);
    }, 3000);
}
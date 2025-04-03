document.addEventListener('DOMContentLoaded', function() {
    const eventForm = document.getElementById('eventForm');
    
    if (eventForm) {
        eventForm.addEventListener('submit', function(event) {
            // Validate name
            const nameInput = document.getElementById('name');
            if (nameInput.value.trim() === '') {
                alert('Event name cannot be empty');
                nameInput.focus();
                event.preventDefault();
                return;
            }
            
            // Validate type
            const typeInput = document.getElementById('type');
            if (typeInput.value === '') {
                alert('Please select an event type');
                typeInput.focus();
                event.preventDefault();
                return;
            }
            
            // Validate date
            const dateInput = document.getElementById('date');
            if (dateInput.value === '') {
                alert('Please select an event date');
                dateInput.focus();
                event.preventDefault();
                return;
            }
            
            // Check if date is in the past
            const selectedDate = new Date(dateInput.value);
            const today = new Date();
            today.setHours(0, 0, 0, 0);
            
            if (selectedDate < today) {
                if (!confirm('The selected date is in the past. Are you sure you want to continue?')) {
                    event.preventDefault();
                    return;
                }
            }
            
            // Validate location
            const locationInput = document.getElementById('location');
            if (locationInput.value.trim() === '') {
                alert('Event location cannot be empty');
                locationInput.focus();
                event.preventDefault();
                return;
            }
            
            // Validate capacity
            const capacityInput = document.getElementById('capacity');
            if (capacityInput.value <= 0) {
                alert('Capacity must be greater than 0');
                capacityInput.focus();
                event.preventDefault();
                return;
            }
            
            // Validate description
            const descriptionInput = document.getElementById('description');
            if (descriptionInput.value.trim() === '') {
                alert('Event description cannot be empty');
                descriptionInput.focus();
                event.preventDefault();
                return;
            }
            
            if (descriptionInput.value.trim().length < 10) {
                alert('Event description should be at least 10 characters');
                descriptionInput.focus();
                event.preventDefault();
                return;
            }
        });
    }
});
/**
 * Event Management Platform - Event Validation Functions
 * 
 * This file contains validation functions for event forms
 */

document.addEventListener('DOMContentLoaded', function() {
    // Get the event form if it exists on the page
    const eventForm = document.querySelector('.event-form');
    
    if (eventForm) {
        eventForm.addEventListener('submit', validateEventForm);
    }
    
    // Set up date picker minimum date to today
    const dateInputs = document.querySelectorAll('input[type="date"]');
    if (dateInputs.length > 0) {
        const today = new Date().toISOString().split('T')[0];
        dateInputs.forEach(input => {
            input.setAttribute('min', today);
        });
    }
});

/**
 * Validates the event form before submission
 * @param {Event} e - The form submission event
 */
function validateEventForm(e) {
    let isValid = true;
    
    // Get form fields
    const nameInput = document.getElementById('name');
    const typeInput = document.getElementById('eventType');
    const dateInput = document.getElementById('date');
    const locationInput = document.getElementById('location');
    const capacityInput = document.getElementById('capacity');
    
    // Reset previous error messages
    clearErrorMessages();
    
    // Validate name (required, min length)
    if (!nameInput.value.trim()) {
        displayError(nameInput, 'Event name is required');
        isValid = false;
    } else if (nameInput.value.trim().length < 3) {
        displayError(nameInput, 'Event name must be at least 3 characters');
        isValid = false;
    }
    
    // Validate event type (required)
    if (!typeInput.value) {
        displayError(typeInput, 'Please select an event type');
        isValid = false;
    }
    
    // Validate date (required, future date)
    if (!dateInput.value) {
        displayError(dateInput, 'Event date is required');
        isValid = false;
    } else {
        const selectedDate = new Date(dateInput.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        
        if (selectedDate < today) {
            displayError(dateInput, 'Event date cannot be in the past');
            isValid = false;
        }
    }
    
    // Validate location (required)
    if (!locationInput.value.trim()) {
        displayError(locationInput, 'Event location is required');
        isValid = false;
    }
    
    // Validate capacity (positive number)
    if (capacityInput && (isNaN(capacityInput.value) || parseInt(capacityInput.value) <= 0)) {
        displayError(capacityInput, 'Capacity must be a positive number');
        isValid = false;
    }
    
    // If form is not valid, prevent submission
    if (!isValid) {
        e.preventDefault();
    }
}

/**
 * Displays an error message for an input field
 * @param {HTMLElement} inputElement - The input element with error
 * @param {string} message - The error message to display
 */
function displayError(inputElement, message) {
    // Create error message element
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    
    // Insert error message after input
    inputElement.parentNode.appendChild(errorDiv);
    
    // Add error class to input
    inputElement.classList.add('input-error');
}

/**
 * Clears all error messages from the form
 */
function clearErrorMessages() {
    // Remove all error message divs
    const errorMessages = document.querySelectorAll('.error-message');
    errorMessages.forEach(div => div.remove());
    
    // Remove error class from inputs
    const errorInputs = document.querySelectorAll('.input-error');
    errorInputs.forEach(input => input.classList.remove('input-error'));
}
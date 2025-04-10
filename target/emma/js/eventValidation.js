document.addEventListener('DOMContentLoaded', function() {
    const eventForm = document.getElementById('eventForm');
    
    if (eventForm) {
        // Set up input validations
        setupInputValidations();
        
        eventForm.addEventListener('submit', function(event) {
            // Remove any existing validation messages
            clearValidationMessages();
            
            // Track if form is valid
            let isValid = true;
            
            // Validate all required fields
            isValid = validateField('name', 'Event name cannot be empty') && isValid;
            isValid = validateField('type', 'Please select an event type') && isValid;
            isValid = validateField('location', 'Event location cannot be empty') && isValid;
            isValid = validateField('description', 'Event description cannot be empty') && isValid;
            
            // Validate capacity is a positive number
            const capacityInput = document.getElementById('capacity');
            if (capacityInput.value <= 0) {
                showValidationError(capacityInput, 'Capacity must be greater than 0');
                isValid = false;
            }
            
            // Validate description length
            const descriptionInput = document.getElementById('description');
            if (descriptionInput.value.trim().length < 10) {
                showValidationError(descriptionInput, 'Event description should be at least 10 characters');
                isValid = false;
            }
            
            // Validate date
            const dateInput = document.getElementById('date');
            if (dateInput.value === '') {
                showValidationError(dateInput, 'Please select an event date');
                isValid = false;
            } else {
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
            }
            
            // If validation failed, prevent form submission
            if (!isValid) {
                event.preventDefault();
                
                // Scroll to the first error
                const firstErrorField = document.querySelector('.is-invalid');
                if (firstErrorField) {
                    firstErrorField.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    firstErrorField.focus();
                }
            }
        });
    }
    
    // Setup live validation on inputs
    function setupInputValidations() {
        // Name validation
        const nameInput = document.getElementById('name');
        if (nameInput) {
            nameInput.addEventListener('blur', function() {
                validateField('name', 'Event name cannot be empty');
            });
        }
        
        // Type validation
        const typeInput = document.getElementById('type');
        if (typeInput) {
            typeInput.addEventListener('change', function() {
                validateField('type', 'Please select an event type');
            });
        }
        
        // Date validation
        const dateInput = document.getElementById('date');
        if (dateInput) {
            // Set min date to today to prevent past dates
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');
            dateInput.min = `${yyyy}-${mm}-${dd}`;
            
            dateInput.addEventListener('blur', function() {
                validateField('date', 'Please select an event date');
            });
        }
        
        // Location validation
        const locationInput = document.getElementById('location');
        if (locationInput) {
            locationInput.addEventListener('blur', function() {
                validateField('location', 'Event location cannot be empty');
            });
        }
        
        // Capacity validation
        const capacityInput = document.getElementById('capacity');
        if (capacityInput) {
            capacityInput.addEventListener('blur', function() {
                if (capacityInput.value <= 0) {
                    showValidationError(capacityInput, 'Capacity must be greater than 0');
                } else {
                    clearValidationError(capacityInput);
                }
            });
        }
        
        // Description validation
        const descriptionInput = document.getElementById('description');
        if (descriptionInput) {
            descriptionInput.addEventListener('blur', function() {
                if (descriptionInput.value.trim() === '') {
                    showValidationError(descriptionInput, 'Event description cannot be empty');
                } else if (descriptionInput.value.trim().length < 10) {
                    showValidationError(descriptionInput, 'Event description should be at least 10 characters');
                } else {
                    clearValidationError(descriptionInput);
                }
            });
        }
    }
    
    // Validate a field by its ID
    function validateField(fieldId, errorMessage) {
        const field = document.getElementById(fieldId);
        if (!field) return true;
        
        if (field.value.trim() === '') {
            showValidationError(field, errorMessage);
            return false;
        } else {
            clearValidationError(field);
            return true;
        }
    }
    
    // Show validation error for a field
    function showValidationError(field, message) {
        field.classList.add('is-invalid');
        
        // Check if error message already exists
        let errorDiv = field.nextElementSibling;
        if (!errorDiv || !errorDiv.classList.contains('invalid-feedback')) {
            errorDiv = document.createElement('div');
            errorDiv.className = 'invalid-feedback';
            field.parentNode.insertBefore(errorDiv, field.nextSibling);
        }
        
        errorDiv.textContent = message;
    }
    
    // Clear validation error for a field
    function clearValidationError(field) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
        
        const errorDiv = field.nextElementSibling;
        if (errorDiv && errorDiv.classList.contains('invalid-feedback')) {
            errorDiv.textContent = '';
        }
    }
    
    // Clear all validation messages
    function clearValidationMessages() {
        const invalidFields = document.querySelectorAll('.is-invalid');
        invalidFields.forEach(field => {
            field.classList.remove('is-invalid');
        });
        
        const validFields = document.querySelectorAll('.is-valid');
        validFields.forEach(field => {
            field.classList.remove('is-valid');
        });
        
        const errorMessages = document.querySelectorAll('.invalid-feedback');
        errorMessages.forEach(div => {
            div.textContent = '';
        });
    }
});
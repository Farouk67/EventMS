document.addEventListener('DOMContentLoaded', function() {
    // Handle date picker maximum range
    const dateInput = document.getElementById('date');
    if (dateInput) {
        // Set max date to 2 years from now
        const twoYearsFromNow = new Date();
        twoYearsFromNow.setFullYear(twoYearsFromNow.getFullYear() + 2);
        dateInput.max = twoYearsFromNow.toISOString().split('T')[0];
    }
    
    // Handle search form submission
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            // Check if at least one search field is filled
            const keyword = document.getElementById('q').value.trim();
            const type = document.getElementById('type').value;
            const location = document.getElementById('location').value;
            const date = document.getElementById('date').value;
            
            if (keyword === '' && type === '' && location === '' && date === '') {
                alert('Please enter at least one search criteria.');
                event.preventDefault();
                return;
            }
        });
    }
    
    // Handle filters for event list
    const filterLinks = document.querySelectorAll('.event-types a');
    filterLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            // Remove active class from all links
            filterLinks.forEach(l => l.classList.remove('active'));
            // Add active class to clicked link
            this.classList.add('active');
        });
    });
    
    // Handle search input for real-time filtering
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const eventCards = document.querySelectorAll('.event-card');
            
            eventCards.forEach(card => {
                const eventName = card.querySelector('h3').textContent.toLowerCase();
                const eventDescription = card.querySelector('.event-description').textContent.toLowerCase();
                const eventLocation = card.querySelector('.event-location').textContent.toLowerCase();
                
                if (eventName.includes(searchTerm) || 
                    eventDescription.includes(searchTerm) || 
                    eventLocation.includes(searchTerm)) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    }
});
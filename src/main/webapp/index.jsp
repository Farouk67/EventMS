<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />


<div class="hero-section bg-primary text-white text-center py-5">
    <div class="container">
        <div class="row">
            <div class="col-lg-8 mx-auto">
                <h1 class="display-4">Welcome to Emma's Event Management</h1>
                <p class="lead mb-4">Discover exciting events near you or create your own!</p>
                <div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
                    <a href="${pageContext.request.contextPath}/events/list" class="btn btn-light btn-lg px-4 gap-3">Browse Events</a>
                    <c:if test="${not empty sessionScope.userId}">
                        <a href="${pageContext.request.contextPath}/events/new" class="btn btn-outline-light btn-lg px-4">Create Event</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container my-5">
    <div class="row text-center mb-5">
        <div class="col">
            <h2>Discover What We Offer</h2>
            <p class="lead text-muted">Everything you need to manage events successfully</p>
        </div>
    </div>
    
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center">
                    <div class="mb-3">
                        <i class="bi bi-search text-primary" style="font-size: 2.5rem;"></i>
                    </div>
                    <h3 class="card-title">Find Events</h3>
                    <p class="card-text">Discover events by category, location, or date that match your interests.</p>
                </div>
            </div>
        </div>
        
        <div class="col-md-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center">
                    <div class="mb-3">
                        <i class="bi bi-calendar-event text-primary" style="font-size: 2.5rem;"></i>
                    </div>
                    <h3 class="card-title">Create Events</h3>
                    <p class="card-text">Host your own events and manage registrations easily.</p>
                </div>
            </div>
        </div>
        
        <div class="col-md-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center">
                    <div class="mb-3">
                        <i class="bi bi-people text-primary" style="font-size: 2.5rem;"></i>
                    </div>
                    <h3 class="card-title">RSVP</h3>
                    <p class="card-text">Quickly respond to event invitations and keep track of your schedule.</p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="row mt-5">
        <div class="col-12 text-center">
            <h2>Upcoming Events</h2>
            <p class="lead text-muted mb-4">Check out what's happening soon</p>
            <div class="event-carousel" id="upcomingEvents">
                <div class="text-center py-4">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <p class="mt-2">Loading upcoming events...</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript to load upcoming events -->
<!-- JavaScript to load upcoming events -->
<script>
   document.addEventListener('DOMContentLoaded', function() {
    console.log("Loading upcoming events...");
    const contextPath = '${pageContext.request.contextPath}'; // Evaluate JSP expression once
    
    fetch(contextPath + '/api/events/upcoming')
        .then(response => {
            // Rest of your fetch code unchanged
        })
        .then(events => {
            console.log("Events received:", events);
            const eventCarousel = document.getElementById('upcomingEvents');
            
            if (events && events.length > 0) {
                // Create container for events
                let rowHTML = '<div class="row row-cols-1 row-cols-md-3 g-4">';
                
                // Loop through each event
                events.forEach(event => {
                    console.log("Processing event:", event.id, event.name);
                    
                    // Format the date code unchanged
                    
                    // Add event card HTML
                    rowHTML += `
                        <div class="col">
                            <div class="card h-100 shadow-sm">
                                <div class="card-header">
                                    <h5 class="card-title">${event.name || 'Unnamed Event'}</h5>
                                    <span class="badge bg-primary">${event.type || 'Event'}</span>
                                </div>
                                <div class="card-body">
                                    <p class="card-text"><i class="bi bi-geo-alt me-2"></i>${event.location || 'TBD'}</p>
                                    <p class="card-text"><i class="bi bi-calendar me-2"></i>${formattedDate}</p>
                                    <p class="card-text">${shortDesc}</p>
                                </div>
                                <div class="card-footer bg-white">
                                    <a href="${contextPath}/events/details?id=${event.id}" class="btn btn-primary w-100">View Details</a>
                                </div>
                            </div>
                        </div>
                    `;
                });
                
                rowHTML += '</div>';
                eventCarousel.innerHTML = rowHTML;
            } else {
                eventCarousel.innerHTML = '<p class="text-center">No upcoming events found.</p>';
            }
        })
        .catch(error => {
            console.error('Error fetching upcoming events:', error);
            document.getElementById('upcomingEvents').innerHTML = 
                '<div class="alert alert-danger">Failed to load upcoming events. Please try again later.</div>';
        });
});
</script>

<jsp:include page="WEB-INF/jsp/common/footer.jsp" />
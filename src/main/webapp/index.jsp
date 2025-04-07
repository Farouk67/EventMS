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
    fetch('${pageContext.request.contextPath}/events/api/upcoming')
        .then(response => {
            console.log("API response status:", response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(events => {
            console.log("Events received:", events);
            const eventCarousel = document.getElementById('upcomingEvents');
            if (events && events.length > 0) {
                let html = '<div class="row row-cols-1 row-cols-md-3 g-4">';
                events.forEach(event => {
                    console.log("Processing event:", event.id, event.name);
                    
                    // Safely handle date formatting
                    let formattedDate = 'TBD';
                    try {
                        if (event.eventDate) {
                            const eventDate = new Date(event.eventDate);
                            if (!isNaN(eventDate.getTime())) {
                                formattedDate = eventDate.toLocaleDateString('en-US', {
                                    year: 'numeric', 
                                    month: 'short', 
                                    day: 'numeric'
                                });
                            }
                        }
                    } catch (dateError) {
                        console.error('Error parsing date:', dateError);
                    }

                    // Safely handle other fields
                    const eventName = event.name || 'Unnamed Event';
                    const eventType = event.type || 'Unspecified';
                    const eventLocation = event.location || 'Location TBD';
                    const description = event.description || '';
                    const shortDesc = description.length > 100 
                        ? description.substring(0, 100) + '...' 
                        : description;
                    
                    // Make sure the ID exists before creating a link
                    const detailsLink = event.id 
                        ? `${pageContext.request.contextPath}/events/details?id=${event.id}` 
                        : '#';
                    
                    if (!event.id) {
                        console.warn("Event missing ID:", event);
                    }

                    html += `
                        <div class="col">
                            <div class="card h-100 shadow-sm">
                                <div class="card-header">
                                    <h5 class="card-title">${eventName}</h5>
                                    <span class="badge bg-primary">${eventType}</span>
                                </div>
                                <div class="card-body">
                                    <p class="card-text"><i class="bi bi-geo-alt me-2"></i>${eventLocation}</p>
                                    <p class="card-text"><i class="bi bi-calendar me-2"></i>${formattedDate}</p>
                                    <p class="card-text">${shortDesc}</p>
                                </div>
                                <div class="card-footer bg-white">
                                    <a href="${detailsLink}" class="btn btn-primary w-100">View Details</a>
                                </div>
                            </div>
                        </div>
                    `;
                });
                html += '</div>';
                eventCarousel.innerHTML = html;
            } else {
                eventCarousel.innerHTML = '<p class="text-center">No upcoming events found.</p>';
            }
        })
        .catch(error => {
            console.error('Error fetching upcoming events:', error);
            const eventCarousel = document.getElementById('upcomingEvents');
            eventCarousel.innerHTML = '<div class="alert alert-danger">Failed to load upcoming events. Please try again later.</div>';
        });
});
</script>

<jsp:include page="WEB-INF/jsp/common/footer.jsp" />
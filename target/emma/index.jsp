<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="WEB-INF/jsp/common/header.jsp" />
<jsp:include page="WEB-INF/jsp/common/navigation.jsp" />

<div class="hero-section">
    <div class="container">
        <div class="hero-content">
            <h1>Welcome to Emma's Event Management</h1>
            <p>Discover exciting events near you or create your own!</p>
            <div class="hero-buttons">
                <a href="${pageContext.request.contextPath}/events/list" class="btn btn-primary">Browse Events</a>
                <c:if test="${not empty sessionScope.userId}">
                    <a href="${pageContext.request.contextPath}/events/new" class="btn btn-secondary">Create Event</a>
                </c:if>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div class="features-section">
        <h2>Discover What We Offer</h2>
        <div class="features-grid">
            <div class="feature-card">
                <i class="icon-search"></i>
                <h3>Find Events</h3>
                <p>Discover events by category, location, or date that match your interests.</p>
            </div>
            <div class="feature-card">
                <i class="icon-calendar"></i>
                <h3>Create Events</h3>
                <p>Host your own events and manage registrations easily.</p>
            </div>
            <div class="feature-card">
                <i class="icon-users"></i>
                <h3>RSVP</h3>
                <p>Quickly respond to event invitations and keep track of your schedule.</p>
            </div>
        </div>
    </div>
    
    <div class="upcoming-events">
        <h2>Upcoming Events</h2>
        <div class="event-carousel">
            
            <p class="text-center">Loading upcoming events...</p>
        </div>
    </div>
</div>

<!-- Additional script to load upcoming events -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        fetch('${pageContext.request.contextPath}/events/api/upcoming')
            .then(response => response.json())
            .then(events => {
                const eventCarousel = document.querySelector('.event-carousel');
                if (events.length > 0) {
                    eventCarousel.innerHTML = '';
                    events.forEach(event => {
                        eventCarousel.innerHTML += `
                            <div class="event-card">
                                <div class="event-card-header">
                                    <h3>${event.name}</h3>
                                    <span class="event-type">${event.type}</span>
                                </div>
                                <div class="event-card-body">
                                    <p class="event-location"><i class="icon-location"></i> ${event.location}</p>
                                    <p class="event-date"><i class="icon-calendar"></i> ${new Date(event.date).toLocaleDateString()}</p>
                                    <p class="event-description">${event.description.substring(0, 100)}...</p>
                                </div>
                                <div class="event-card-footer">
                                    <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-primary">View Details</a>
                                </div>
                            </div>
                        `;
                    });
                } else {
                    eventCarousel.innerHTML = '<p class="text-center">No upcoming events found.</p>';
                }
            })
            .catch(error => {
                console.error('Error fetching upcoming events:', error);
                const eventCarousel = document.querySelector('.event-carousel');
                eventCarousel.innerHTML = '<p class="text-center">Failed to load upcoming events. Please try again later.</p>';
            });
    });
</script>

<jsp:include page="WEB-INF/jsp/common/footer.jsp" />
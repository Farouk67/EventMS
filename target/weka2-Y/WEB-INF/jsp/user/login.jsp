<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<div class="jumbotron bg-light p-5 rounded">
    <h1 class="display-4">Welcome to Emma's Event Management</h1>
    <p class="lead">Discover, create, and manage events all in one place.</p>
    <hr class="my-4">
    <p>Looking for an event? Browse our listings. Want to host? Create your own event in minutes!</p>
    <div class="d-flex gap-2">
        <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/events" role="button">Browse Events</a>
        <a class="btn btn-success btn-lg" href="${pageContext.request.contextPath}/events/create" role="button">Create Event</a>
    </div>
</div>

<div class="row mt-5">
    <div class="col-12">
        <h2 class="text-center mb-4">Upcoming Events</h2>
    </div>
</div>

<div class="row">
    <c:forEach var="event" items="${featuredEvents}" varStatus="status">
        <div class="col-md-4 mb-4">
            <div class="card h-100">
                <div class="card-header bg-${status.index % 4 == 0 ? 'primary' : status.index % 4 == 1 ? 'success' : status.index % 4 == 2 ? 'info' : 'warning'} text-white">
                    ${event.eventType.name}
                </div>
                <div class="card-body">
                    <h5 class="card-title">${event.name}</h5>
                    <h6 class="card-subtitle mb-2 text-muted">${event.formattedDate} - ${event.location}</h6>
                    <p class="card-text">${event.description}</p>
                </div>
                <div class="card-footer">
                    <a href="${pageContext.request.contextPath}/events/view?id=${event.id}" class="btn btn-sm btn-outline-primary">View Details</a>
                </div>
            </div>
        </div>
    </c:forEach>
    
    <c:if test="${empty featuredEvents}">
        <div class="col-12 text-center">
            <p>No upcoming events found. Be the first to <a href="${pageContext.request.contextPath}/events/create">create an event</a>!</p>
        </div>
    </c:if>
</div>

<div class="row mt-5">
    <div class="col-md-4">
        <div class="card text-center h-100">
            <div class="card-body">
                <i class="fas fa-calendar-plus fa-3x mb-3 text-primary"></i>
                <h5 class="card-title">Create Events</h5>
                <p class="card-text">Host your own events and manage attendees all in one place.</p>
                <a href="${pageContext.request.contextPath}/events/create" class="btn btn-primary">Create Now</a>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-center h-100">
            <div class="card-body">
                <i class="fas fa-search fa-3x mb-3 text-success"></i>
                <h5 class="card-title">Discover Events</h5>
                <p class="card-text">Find events based on your interests, location, and schedule.</p>
                <a href="${pageContext.request.contextPath}/events" class="btn btn-success">Browse Events</a>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-center h-100">
            <div class="card-body">
                <i class="fas fa-users fa-3x mb-3 text-info"></i>
                <h5 class="card-title">Manage RSVPs</h5>
                <p class="card-text">Keep track of events you're attending and manage your RSVPs.</p>
                <a href="${pageContext.request.contextPath}/rsvp/list" class="btn btn-info">My RSVPs</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
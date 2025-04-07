<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
            <h1>${event.name}</h1>
            <span class="badge bg-primary">${event.type}</span>
        </div>
    </div>
    
    <div class="row">
        <div class="col-md-8">
            <div class="card mb-4">
                <div class="card-body">
                    <div class="d-flex mb-3">
                        <div class="me-4">
                            <i class="bi bi-calendar-event fs-4 text-primary"></i>
                        </div>
                        <div>
                            <h5 class="mb-1">Date</h5>
                            <p class="mb-0"><fmt:formatDate pattern="EEEE, MMMM d, yyyy" value="${event.date}" /></p>
                        </div>
                    </div>
                    
                    <div class="d-flex mb-3">
                        <div class="me-4">
                            <i class="bi bi-geo-alt fs-4 text-primary"></i>
                        </div>
                        <div>
                            <h5 class="mb-1">Location</h5>
                            <p class="mb-0">${event.location}</p>
                        </div>
                    </div>
                    
                    <div class="d-flex mb-3">
                        <div class="me-4">
                            <i class="bi bi-people fs-4 text-primary"></i>
                        </div>
                        <div>
                            <h5 class="mb-1">Attendees</h5>
                            <p class="mb-0">${event.attendeeCount} / ${event.capacity} Registered</p>
                            
                            <c:if test="${event.capacity > 0}">
                                <c:set var="attendeePercentage" value="${(event.attendeeCount * 100) / event.capacity}" />
                                <c:set var="limitedPercentage" value="${attendeePercentage > 100 ? 100 : attendeePercentage}" />
                                
                                <div class="progress mt-2" style="height: 10px;">
                                    <div class="progress-bar 
                                        <c:choose>
                                            <c:when test="${attendeePercentage >= 90}">bg-danger</c:when>
                                            <c:when test="${attendeePercentage >= 70}">bg-warning</c:when>
                                            <c:otherwise>bg-success</c:otherwise>
                                        </c:choose>"
                                         style="width: ${limitedPercentage}%">
                                    </div>
                                </div>
                                
                                <small class="text-muted">
                                    <c:choose>
                                        <c:when test="${attendeePercentage >= 90}">Almost Full</c:when>
                                        <c:when test="${attendeePercentage >= 70}">Filling Up</c:when>
                                        <c:otherwise>Spaces Available</c:otherwise>
                                    </c:choose>
                                </small>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">About this event</h5>
                </div>
                <div class="card-body">
                    <p>${event.description}</p>
                </div>
            </div>
        </div>
        
        <div class="col-md-4">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Actions</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${sessionScope.userId eq event.organizerId}">
                            <p>You are the organizer of this event.</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/events/edit?id=${event.id}" class="btn btn-primary">
                                    <i class="bi bi-pencil me-2"></i>Edit Event
                                </a>
                                <a href="${pageContext.request.contextPath}/events/delete?id=${event.id}" 
                                   class="btn btn-danger" 
                                   onclick="return confirm('Are you sure you want to delete this event?')">
                                    <i class="bi bi-trash me-2"></i>Delete Event
                                </a>
                            </div>
                        </c:when>
                        <c:when test="${not empty sessionScope.userId}">
                            <c:choose>
                                <c:when test="${hasRSVPed}">
                                    <div class="alert alert-success">
                                        <i class="bi bi-check-circle me-2"></i>You have RSVPed to this event.
                                    </div>
                                    <div class="d-grid">
                                        <a href="${pageContext.request.contextPath}/rsvp/cancel?eventId=${event.id}" 
                                           class="btn btn-outline-danger"
                                           onclick="return confirm('Are you sure you want to cancel your RSVP?')">
                                            <i class="bi bi-calendar-x me-2"></i>Cancel RSVP
                                        </a>
                                    </div>
                                </c:when>
                                <c:when test="${event.attendeeCount < event.capacity}">
                                    <div class="d-grid">
                                        <a href="${pageContext.request.contextPath}/rsvp/add?eventId=${event.id}" 
                                           class="btn btn-primary">
                                            <i class="bi bi-calendar-check me-2"></i>RSVP to Event
                                        </a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-warning">
                                        <i class="bi bi-exclamation-triangle me-2"></i>This event is at full capacity.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle me-2"></i>Please 
                                <a href="${pageContext.request.contextPath}/users/login">login</a> 
                                to RSVP for this event.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
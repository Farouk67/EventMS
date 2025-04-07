<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container">
    <div class="event-details">
        <div class="event-header">
            <h1>${event.name}</h1>
            <span class="event-type">${event.type}</span>
        </div>
        
        <div class="event-info">
            <div class="event-info-item">
                <i class="icon-calendar"></i>
                <div class="info-content">
                    <h3>Date</h3>
                    <p><fmt:formatDate pattern="EEEE, MMMM d, yyyy" value="${event.date}" /></p>
                </div>
            </div>
            
            <div class="event-info-item">
                <i class="icon-location"></i>
                <div class="info-content">
                    <h3>Location</h3>
                    <p>${event.location}</p>
                </div>
            </div>
            
            <div class="event-info-item">
                <i class="icon-users"></i>
                <div class="info-content">
                    <h3>Attendees</h3>
                    <div class="attendee-details">
                        <span class="attendee-count">
                            ${event.attendeeCount} / ${event.capacity} Registered
                        </span>
                        <c:choose>
                            <c:when test="${event.capacity > 0}">
                                <c:set var="attendeePercentage" 
                                       value="${Math.min(Math.max((event.attendeeCount / event.capacity) * 100, 0), 100)}" />
                                <div class="progress mt-2">
                                    <div class="progress-bar 
                                        ${attendeePercentage >= 90 ? 'bg-danger' : 
                                          attendeePercentage >= 70 ? 'bg-warning' : 'bg-success'}" 
                                         style="width: '${attendeePercentage}%'">
                                    </div>
                                </div>
                                <small class="text-muted">
                                    ${attendeePercentage >= 90 ? 'Almost Full' : 
                                      attendeePercentage >= 70 ? 'Filling Up' : 'Spaces Available'}
                                </small>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-warning mt-2">
                                    Capacity not set
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="event-description">
            <h2>About this event</h2>
            <p>${event.description}</p>
        </div>
        
        <div class="event-actions">
            <c:choose>
                <c:when test="${sessionScope.userId eq event.organizerId}">
                    <p>You are the organizer of this event.</p>
                    <a href="${pageContext.request.contextPath}/events/edit?id=${event.id}" class="btn btn-primary">Edit Event</a>
                    <a href="${pageContext.request.contextPath}/events/delete?id=${event.id}" class="btn btn-danger" 
                       onclick="return confirm('Are you sure you want to delete this event?')">Delete Event</a>
                </c:when>
                <c:when test="${not empty sessionScope.userId}">
                    <c:choose>
                        <c:when test="${hasRSVPed}">
                            <p>You have already RSVP'd to this event.</p>
                            <a href="${pageContext.request.contextPath}/rsvp/cancel?eventId=${event.id}" class="btn btn-danger">Cancel RSVP</a>
                        </c:when>
                        <c:when test="${event.attendeeCount < event.capacity}">
                            <a href="${pageContext.request.contextPath}/rsvp/add?eventId=${event.id}" class="btn btn-primary">RSVP to Event</a>
                        </c:when>
                        <c:otherwise>
                            <p class="event-full">This event is at full capacity.</p>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <p>Please <a href="${pageContext.request.contextPath}/users/login">login</a> to RSVP for this event.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
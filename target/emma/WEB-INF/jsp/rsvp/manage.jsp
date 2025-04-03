<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>Manage RSVPs for ${event.name}</h1>
    </div>
    
    <div class="event-summary">
        <div class="event-info">
            <p><strong>Date:</strong> <fmt:formatDate pattern="MMMM d, yyyy" value="${event.date}" /></p>
            <p><strong>Location:</strong> ${event.location}</p>
            <p><strong>Attendees:</strong> ${event.attendeeCount} / ${event.capacity}</p>
        </div>
        
        <div class="attendance-graph">
            <div class="progress">
                <div class="progress-bar" style="width: ${(event.attendeeCount / event.capacity) * 100}%"></div>
            </div>
            <p class="attendance-text">${event.attendeeCount} of ${event.capacity} spots filled</p>
        </div>
    </div>
    
    <div class="rsvp-list">
        <h2>Attendees</h2>
        
        <c:choose>
            <c:when test="${not empty rsvps}">
                <table class="rsvp-table">
                    <thead>
                        <tr>
                            <th>User</th>
                            <th>RSVP Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="rsvp" items="${rsvps}">
                            <tr>
                                <td>${rsvp.user.username}</td>
                                <td><fmt:formatDate pattern="MMM d, yyyy" value="${rsvp.respondedAt}" /></td>
                                <td><span class="status-badge ${rsvp.status}">${rsvp.status}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/rsvp/remove?id=${rsvp.id}&eventId=${event.id}" 
                                       class="btn btn-sm btn-danger"
                                       onclick="return confirm('Are you sure you want to remove this RSVP?')">Remove</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>No RSVPs yet for this event.</p>
            </c:otherwise>
        </c:choose>
    </div>
    
    <div class="actions">
        <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-secondary">Back to Event</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/rsvpManagement.js"></script>

<jsp:include page="../common/footer.jsp" />
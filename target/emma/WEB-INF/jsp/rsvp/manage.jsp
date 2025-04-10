<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="page-header mb-4">
        <h1>Manage RSVPs for ${event.name}</h1>
    </div>
    
    <div class="card mb-4">
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <p><strong>Date:</strong> <fmt:formatDate pattern="MMMM d, yyyy" value="${event.eventDate}" /></p>
                    <p><strong>Location:</strong> ${event.location}</p>
                    <p><strong>Attendees:</strong> ${event.attendeeCount} / ${event.capacity}</p>
                </div>
                <div class="col-md-6">
                    <% 
                        int attendeeCount = ((com.emma.model.Event)request.getAttribute("event")).getAttendeeCount();
                        int capacity = ((com.emma.model.Event)request.getAttribute("event")).getCapacity();
                        int percentage = (capacity > 0) ? (attendeeCount * 100 / capacity) : 0;
                        
                        String progressColor = "bg-success";
                        if (percentage < 50) {
                            progressColor = "bg-warning";
                        } else if (percentage < 25) {
                            progressColor = "bg-danger";
                        }
                    %>
                    <h5>Attendance</h5>
                    <div class="progress mb-2">
                        <div class="progress-bar <%= progressColor %>" 
                             role="progressbar" 
                             style="width: <%= percentage %>%" 
                             aria-valuenow="<%= percentage %>" 
                             aria-valuemin="0" 
                             aria-valuemax="100"><%= percentage %>%</div>
                    </div>
                    <p class="text-muted">${event.attendeeCount} of ${event.capacity} spots filled</p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="card mb-4">
        <div class="card-header">
            <h2 class="mb-0 h5">Attendees</h2>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty rsvps}">
                    <div class="table-responsive">
                        <table class="table table-striped">
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
                                        <td>${usernames[rsvp.userId]}</td>
                                        <td><fmt:formatDate pattern="MMM d, yyyy" value="${rsvp.respondedAt}" /></td>
                                        <td><span class="badge ${rsvp.status == 'Confirmed' ? 'bg-success' : 'bg-warning'}">${rsvp.status}</span></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/rsvp/remove?id=${rsvp.id}&eventId=${event.id}" 
                                               class="btn btn-sm btn-danger"
                                               onclick="return confirm('Are you sure you want to remove this RSVP?')">Remove</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-center">No RSVPs yet for this event.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <div class="actions mb-4">
        <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-secondary">Back to Event</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/rsvpManagement.js"></script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
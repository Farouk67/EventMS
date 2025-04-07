<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
            <h1>My RSVP'd Events</h1>
            <p class="lead">Events you have registered to attend</p>
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty events}">
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <c:forEach var="event" items="${events}">
                    <div class="col">
                        <div class="card h-100 shadow-sm">
                            <div class="card-header">
                                <h5 class="card-title">${event.name}</h5>
                                <span class="badge bg-primary">${event.type}</span>
                            </div>
                            <div class="card-body">
                                <p class="card-text"><i class="bi bi-geo-alt me-2"></i>${event.location}</p>
                                <p class="card-text"><i class="bi bi-calendar me-2"></i><fmt:formatDate pattern="MMMM d, yyyy" value="${event.date}" /></p>
                                <p class="card-text"><i class="bi bi-people me-2"></i>${event.attendeeCount} / ${event.capacity} attendees</p>
                                <p class="card-text">${event.description.length() > 100 ? event.description.substring(0, 100).concat('...') : event.description}</p>
                            </div>
                            <div class="card-footer bg-white">
                                <div class="d-flex justify-content-between">
                                    <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-primary">View Details</a>
                                    <a href="${pageContext.request.contextPath}/rsvp/cancel?eventId=${event.id}" 
                                       class="btn btn-outline-danger"
                                       onclick="return confirm('Are you sure you want to cancel your RSVP?')">
                                        Cancel RSVP
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center p-5">
                <p class="h4 mb-3">You haven't RSVP'd to any events yet.</p>
                <a href="${pageContext.request.contextPath}/events/list" class="btn btn-primary">Browse Events</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
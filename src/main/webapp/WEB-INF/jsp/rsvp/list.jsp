<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>My RSVPs</h1>
    </div>
    
    <div class="rsvp-list">
        <c:choose>
            <c:when test="${not empty rsvps}">
                <div class="event-grid">
                    <c:forEach var="rsvp" items="${rsvps}">
                        <div class="event-card">
                            <div class="event-card-header">
                                <h3>${rsvp.event.name}</h3>
                                <span class="event-type">${rsvp.event.type}</span>
                            </div>
                            <div class="event-card-body">
                                <p class="event-location"><i class="icon-location"></i> ${rsvp.event.location}</p>
                                <p class="event-date"><i class="icon-calendar"></i> <fmt:formatDate pattern="MMMM d, yyyy" value="${rsvp.event.date}" /></p>
                                <p class="rsvp-date">RSVP'd on <fmt:formatDate pattern="MMM d, yyyy" value="${rsvp.respondedAt}" /></p>
                                <p class="rsvp-status">Status: <span class="status-badge ${rsvp.status}">${rsvp.status}</span></p>
                            </div>
                            <div class="event-card-footer">
                                <a href="${pageContext.request.contextPath}/events/details?id=${rsvp.event.id}" class="btn btn-primary">View Event</a>
                                <a href="${pageContext.request.contextPath}/rsvp/cancel?eventId=${rsvp.event.id}" class="btn btn-danger"
                                   onclick="return confirm('Are you sure you want to cancel your RSVP?')">Cancel RSVP</a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-rsvps">
                    <p>You haven't RSVP'd to any events yet.</p>
                    <a href="${pageContext.request.contextPath}/events/list" class="btn btn-primary">Browse Events</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
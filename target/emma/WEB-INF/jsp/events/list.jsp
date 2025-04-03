<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>
            <c:choose>
                <c:when test="${not empty currentType}">
                    ${currentType} Events
                </c:when>
                <c:otherwise>
                    All Events
                </c:otherwise>
            </c:choose>
        </h1>
    </div>
    
    <div class="event-filters">
        <div class="event-types">
            <h3>Event Types</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/events/list" class="${empty currentType ? 'active' : ''}">All</a></li>
                <c:forEach var="type" items="${eventTypes}">
                    <li><a href="${pageContext.request.contextPath}/events/byType?type=${type}" class="${currentType eq type ? 'active' : ''}">${type}</a></li>
                </c:forEach>
            </ul>
        </div>
    </div>
    
    <div class="event-grid">
        <c:choose>
            <c:when test="${not empty events}">
                <c:forEach var="event" items="${events}">
                    <div class="event-card">
                        <div class="event-card-header">
                            <h3>${event.name}</h3>
                            <span class="event-type">${event.type}</span>
                        </div>
                        <div class="event-card-body">
                            <p class="event-location"><i class="icon-location"></i> ${event.location}</p>
                            <p class="event-date"><i class="icon-calendar"></i> <fmt:formatDate pattern="MMMM d, yyyy" value="${event.date}" /></p>
                            <p class="event-attendees"><i class="icon-users"></i> ${event.attendeeCount} / ${event.capacity} attendees</p>
                            <p class="event-description">${event.description.length() > 100 ? event.description.substring(0, 100).concat('...') : event.description}</p>
                        </div>
                        <div class="event-card-footer">
                            <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-primary">View Details</a>
                            <c:if test="${sessionScope.userId eq event.organizerId}">
                                <a href="${pageContext.request.contextPath}/events/edit?id=${event.id}" class="btn btn-secondary">Edit</a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-events">
                    <p>No events available in this category.</p>
                    <c:if test="${not empty sessionScope.userId}">
                        <a href="${pageContext.request.contextPath}/events/new" class="btn btn-primary">Create New Event</a>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <c:if test="${not empty sessionScope.userId}">
        <div class="create-event">
            <a href="${pageContext.request.contextPath}/events/new" class="btn btn-primary">Create New Event</a>
        </div>
    </c:if>
</div>

<jsp:include page="../common/footer.jsp" />
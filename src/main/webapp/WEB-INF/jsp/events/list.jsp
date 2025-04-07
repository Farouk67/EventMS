<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
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
    </div>
    
    <div class="row">
        <div class="col-md-3 mb-4">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">Event Types</h5>
                </div>
                <div class="list-group list-group-flush">
                    <a href="${pageContext.request.contextPath}/events/list" 
                       class="list-group-item list-group-item-action ${empty currentType ? 'active' : ''}">
                        All Events
                    </a>
                    <c:forEach var="type" items="${eventTypes}">
                        <a href="${pageContext.request.contextPath}/events/byType?type=${type}" 
                           class="list-group-item list-group-item-action ${currentType eq type ? 'active' : ''}">
                            ${type}
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
        
        <div class="col-md-9">
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
                                            <c:if test="${sessionScope.userId eq event.organizerId}">
                                                <a href="${pageContext.request.contextPath}/events/edit?id=${event.id}" class="btn btn-outline-secondary">Edit</a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info text-center p-5">
                        <p class="h4 mb-3">No events available in this category.</p>
                        <c:if test="${not empty sessionScope.userId}">
                            <a href="${pageContext.request.contextPath}/events/new" class="btn btn-primary">Create New Event</a>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <c:if test="${not empty sessionScope.userId}">
        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/events/new" class="btn btn-lg btn-success">
                <i class="bi bi-plus-circle me-2"></i>Create New Event
            </a>
        </div>
    </c:if>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
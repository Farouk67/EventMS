<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<div class="row mb-4">
    <div class="col-md-8">
        <h2>Browse Events</h2>
    </div>
    <div class="col-md-4 text-end">
        <a href="${pageContext.request.contextPath}/events/create" class="btn btn-primary">
            <i class="fas fa-plus"></i> Create Event
        </a>
    </div>
</div>

<div class="row mb-4">
    <div class="col-md-3">
        <!-- Filters -->
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h5 class="card-title mb-0">Filter Events</h5>
            </div>
            <div class="card-body">
                <form id="filterForm" action="${pageContext.request.contextPath}/events" method="GET">
                    <div class="mb-3">
                        <label for="eventType" class="form-label">Event Type</label>
                        <select class="form-select" id="eventType" name="type">
                            <option value="">All Types</option>
                            <c:forEach var="type" items="${eventTypes}">
                                <option value="${type.id}" ${param.type eq type.id ? 'selected' : ''}>${type.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="location" class="form-label">Location</label>
                        <input type="text" class="form-control" id="location" name="location" value="${param.location}" placeholder="e.g., London">
                    </div>
                    
                    <div class="mb-3">
                        <label for="fromDate" class="form-label">From Date</label>
                        <input type="date" class="form-control" id="fromDate" name="fromDate" value="${param.fromDate}">
                    </div>
                    
                    <div class="mb-3">
                        <label for="toDate" class="form-label">To Date</label>
                        <input type="date" class="form-control" id="toDate" name="toDate" value="${param.toDate}">
                    </div>
                    
                    <button type="submit" class="btn btn-primary w-100">Apply Filters</button>
                    <button type="button" id="clearFilters" class="btn btn-outline-secondary w-100 mt-2">Clear Filters</button>
                </form>
            </div>
        </div>
    </div>
    
    <div class="col-md-9">
        <!-- Event Type Tabs -->
        <ul class="nav nav-tabs mb-4">
            <li class="nav-item">
                <a class="nav-link ${empty param.type ? 'active' : ''}" href="${pageContext.request.contextPath}/events">All</a>
            </li>
            <c:forEach var="type" items="${eventTypes}">
                <li class="nav-item">
                    <a class="nav-link ${param.type eq type.id ? 'active' : ''}" href="${pageContext.request.contextPath}/events?type=${type.id}">${type.name}</a>
                </li>
            </c:forEach>
        </ul>
        
        <!-- Events List -->
        <div class="row" id="eventsList">
            <c:choose>
                <c:when test="${not empty events}">
                    <c:forEach var="event" items="${events}">
                        <div class="col-md-6 mb-4">
                            <div class="card h-100">
                                <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                                    <span>${event.eventType.name}</span>
                                    <fmt:parseDate value="${event.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                    <span><fmt:formatDate value="${parsedDate}" type="date" pattern="MMM dd, yyyy" /></span>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title">${event.name}</h5>
                                    <h6 class="card-subtitle mb-2 text-muted"><i class="fas fa-map-marker-alt"></i> ${event.location}</h6>
                                    <p class="card-text text-truncate">${event.description}</p>
                                    
                                    <div class="d-flex justify-content-between align-items-center mt-3">
                                        <span class="badge bg-info">${event.attendeeCount} attendees</span>
                                        <a href="${pageContext.request.contextPath}/events/view?id=${event.id}" class="btn btn-outline-primary">View Details</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="alert alert-info">
                            No events found. Please try different filters or <a href="${pageContext.request.contextPath}/events/create">create a new event</a>.
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/events?page=${currentPage - 1}${not empty param.type ? '&type='.concat(param.type) : ''}${not empty param.location ? '&location='.concat(param.location) : ''}${not empty param.fromDate ? '&fromDate='.concat(param.fromDate) : ''}${not empty param.toDate ? '&toDate='.concat(param.toDate) : ''}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/events?page=${i}${not empty param.type ? '&type='.concat(param.type) : ''}${not empty param.location ? '&location='.concat(param.location) : ''}${not empty param.fromDate ? '&fromDate='.concat(param.fromDate) : ''}${not empty param.toDate ? '&toDate='.concat(param.toDate) : ''}">${i}</a>
                        </li>
                    </c:forEach>
                    
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/events?page=${currentPage + 1}${not empty param.type ? '&type='.concat(param.type) : ''}${not empty param.location ? '&location='.concat(param.location) : ''}${not empty param.fromDate ? '&fromDate='.concat(param.fromDate) : ''}${not empty param.toDate ? '&toDate='.concat(param.toDate) : ''}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/searchFilter.js"></script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
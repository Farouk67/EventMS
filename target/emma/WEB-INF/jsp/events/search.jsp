<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
            <h1>Search Events</h1>
        </div>
    </div>
    
    <div class="row">
        <div class="col-lg-4 mb-4">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">Search Filters</h5>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/search" method="get" id="searchForm">
                        <div class="mb-3">
                            <label for="q" class="form-label">Keywords</label>
                            <input type="text" id="q" name="q" class="form-control" value="${keyword}">
                        </div>
                        
                        <div class="mb-3">
                            <label for="type" class="form-label">Event Type</label>
                            <select id="type" name="type" class="form-select">
                                <option value="">All Types</option>
                                <c:forEach var="type" items="${eventTypes}">
                                    <option value="${type}" ${selectedType eq type ? 'selected' : ''}>${type}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="location" class="form-label">Location</label>
                            <select id="location" name="location" class="form-select">
                                <option value="">All Locations</option>
                                <c:forEach var="loc" items="${locations}">
                                    <option value="${loc}" ${selectedLocation eq loc ? 'selected' : ''}>${loc}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="date" class="form-label">Event Date</label>
                            <input type="date" id="date" name="date" class="form-control" value="${selectedDate}">
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <div class="col-lg-8">
            <c:choose>
                <c:when test="${empty searchResults and (not empty keyword or not empty selectedType or not empty selectedLocation or not empty selectedDate)}">
                    <div class="alert alert-info text-center p-5">
                        <p class="h4 mb-3">No events found matching your search criteria.</p>
                        <p>Try adjusting your search filters or <a href="${pageContext.request.contextPath}/events/list">browse all events</a>.</p>
                    </div>
                </c:when>
                <c:when test="${empty searchResults}">
                    <div class="alert alert-info text-center p-5">
                        <p class="h4 mb-3">Use the search filters to find events.</p>
                        <p>Search by keywords, event type, location, or date.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="mb-3">
                        <p class="lead">Found ${searchResults.size()} event(s) matching your criteria</p>
                    </div>
                    <div class="row row-cols-1 row-cols-md-2 g-4">
                        <c:forEach var="event" items="${searchResults}">
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
                                        <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-primary w-100">View Details</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/searchFilter.js"></script>

<jsp:include page="../common/footer.jsp" />
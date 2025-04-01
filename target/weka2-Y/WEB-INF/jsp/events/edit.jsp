<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<div class="row mb-4">
    <div class="col-12">
        <h2>Edit Event</h2>
        <p class="lead">Update the details of your event below.</p>
    </div>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<c:if test="${not empty successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="row">
    <div class="col-md-8">
        <div class="card">
            <div class="card-body">
                <form id="editEventForm" action="${pageContext.request.contextPath}/events/edit" method="POST" novalidate>
                    <input type="hidden" name="eventId" value="${event.id}">
                    
                    <div class="mb-3">
                        <label for="eventName" class="form-label">Event Name <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="eventName" name="eventName" required value="${event.name}">
                        <div class="invalid-feedback">
                            Please provide an event name.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="eventType" class="form-label">Event Type <span class="text-danger">*</span></label>
                        <select class="form-select" id="eventType" name="eventType" required>
                            <option value="">Select an event type</option>
                            <c:forEach var="type" items="${eventTypes}">
                                <option value="${type.id}" ${event.eventType.id eq type.id ? 'selected' : ''}>${type.name}</option>
                            </c:forEach>
                        </select>
                        <div class="invalid-feedback">
                            Please select an event type.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="eventDate" class="form-label">Event Date <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" id="eventDate" name="eventDate" required value="${event.date}">
                        <div class="invalid-feedback">
                            Please provide a valid event
                            </div>
                    </div>
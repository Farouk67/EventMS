<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<div class="row mb-4">
    <div class="col-12">
        <h2>Create New Event</h2>
        <p class="lead">Fill in the details below to create your event.</p>
    </div>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="row">
    <div class="col-md-8">
        <div class="card">
            <div class="card-body">
                <form id="createEventForm" action="${pageContext.request.contextPath}/events/create" method="POST" novalidate>
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
                        <div id="predictedType" class="form-text text-info d-none">
                            Based on your description, we predict this event might be a <span id="predictedTypeName"></span>.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="eventDate" class="form-label">Event Date <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" id="eventDate" name="eventDate" required value="${event.date}">
                        <div class="invalid-feedback">
                            Please provide a valid event date.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="eventTime" class="form-label">Event Time <span class="text-danger">*</span></label>
                        <input type="time" class="form-control" id="eventTime" name="eventTime" required value="${event.time}">
                        <div class="invalid-feedback">
                            Please provide a valid event time.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="eventLocation" class="form-label">Location <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="eventLocation" name="eventLocation" required value="${event.location}">
                        <div class="invalid-feedback">
                            Please provide an event location.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="eventDescription" class="form-label">Description <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="eventDescription" name="eventDescription" rows="5" required>${event.description}</textarea>
                        <div class="invalid-feedback">
                            Please provide an event description.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="maxAttendees" class="form-label">Maximum Attendees</label>
                        <input type="number" class="form-control" id="maxAttendees" name="maxAttendees" min="1" value="${empty event.maxAttendees ? '100' : event.maxAttendees}">
                        <div class="form-text">Leave empty for unlimited attendees.</div>
                    </div>
                    
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="isPublic" name="isPublic" ${empty event.isPublic or event.isPublic ? 'checked' : ''}>
                        <label class="form-check-label" for="isPublic">Make this event public</label>
                        <div class="form-text">Public events are visible to everyone. Private events are only visible to invited users.</div>
                    </div>
                    
                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                        <button type="button" class="btn btn-outline-secondary" onclick="window.location.href='${pageContext.request.contextPath}/events'">Cancel</button>
                        <button type="submit" class="btn btn-primary">Create Event</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <div class="col-md-4">
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h5 class="card-title mb-0">Tips for Creating Events</h5>
            </div>
            <div class="card-body">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">Be descriptive with your event name and description.</li>
                    <li class="list-group-item">Include all relevant details: date, time, and location.</li>
                    <li class="list-group-item">Specify if there are any requirements for attendees.</li>
                    <li class="list-group-item">Consider setting a maximum number of attendees if space is limited.</li>
                </ul>
            </div>
        </div>
        
        <div class="card">
            <div class="card-header bg-info text-white">
                <h5 class="card-title mb-0">AI-Powered Event Type Prediction</h5>
            </div>
            <div class="card-body">
                <p>Our system can predict the most suitable event type based on your event details. Just fill in the event name, description, and location to see a prediction.</p>
                <p class="mb-0"><small>This feature uses machine learning to analyze your event details and suggest the most appropriate category.</small></p>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/eventValidation.js"></script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
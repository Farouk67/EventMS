<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
            <h1>Create New Event</h1>
        </div>
    </div>
    
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <div class="card shadow">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/events/insert" method="post" id="eventForm">
                        <div class="mb-3">
                            <label for="name" class="form-label">Event Name <span class="text-danger">*</span></label>
                            <input type="text" id="name" name="name" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="type" class="form-label">Event Type <span class="text-danger">*</span></label>
                            <select id="type" name="type" class="form-select" required>
                                <option value="">Select Event Type</option>
                                <c:choose>
                                    <c:when test="${not empty eventTypes}">
                                        <c:forEach var="type" items="${eventTypes}">
                                            <option value="${type}">${type}</option>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Fallback options if event types aren't loaded -->
                                        <option value="Conference">Conference</option>
                                        <option value="Workshop">Workshop</option>
                                        <option value="Seminar">Seminar</option>
                                        <option value="Party">Party</option>
                                        <option value="Concert">Concert</option>
                                        <option value="Exhibition">Exhibition</option>
                                        <option value="Sports">Sports</option>
                                        <option value="Social">Social</option>
                                        <option value="Other">Other</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="date" class="form-label">Event Date <span class="text-danger">*</span></label>
                            <input type="date" id="date" name="date" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="location" class="form-label">Location <span class="text-danger">*</span></label>
                            <input type="text" id="location" name="location" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="capacity" class="form-label">Capacity <span class="text-danger">*</span></label>
                            <input type="number" id="capacity" name="capacity" class="form-control" min="1" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Description <span class="text-danger">*</span></label>
                            <textarea id="description" name="description" class="form-control" rows="5" required></textarea>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <button type="submit" class="btn btn-primary">Create Event</button>
                            <a href="${pageContext.request.contextPath}/events/list" class="btn btn-outline-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const eventForm = document.getElementById('eventForm');
    
    eventForm.addEventListener('submit', function(event) {
        // Basic form validation
        const name = document.getElementById('name').value.trim();
        const type = document.getElementById('type').value;
        const date = document.getElementById('date').value;
        const location = document.getElementById('location').value.trim();
        const capacity = document.getElementById('capacity').value;
        const description = document.getElementById('description').value.trim();
        
        let isValid = true;
        
        // Check if fields are empty
        if (!name) {
            alert('Event name is required');
            isValid = false;
        } else if (!type) {
            alert('Event type is required');
            isValid = false;
        } else if (!date) {
            alert('Event date is required');
            isValid = false;
        } else if (!location) {
            alert('Event location is required');
            isValid = false;
        } else if (!capacity || capacity < 1) {
            alert('Event capacity must be at least 1');
            isValid = false;
        } else if (!description) {
            alert('Event description is required');
            isValid = false;
        }
        
        // Check if date is in the future
        const eventDate = new Date(date);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        
        if (eventDate < today) {
            alert('Event date must be in the future');
            isValid = false;
        }
        
        if (!isValid) {
            event.preventDefault();
        }
    });
});
</script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
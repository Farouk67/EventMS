<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
            <h1>Edit Event</h1>
        </div>
    </div>
    
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <div class="card shadow">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/events/update" method="post" id="eventForm">
                        <input type="hidden" name="id" value="${event.id}">
                        
                        <div class="mb-3">
                            <label for="name" class="form-label">Event Name <span class="text-danger">*</span></label>
                            <input type="text" id="name" name="name" class="form-control" value="${event.name}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="type" class="form-label">Event Type <span class="text-danger">*</span></label>
                            <select id="type" name="type" class="form-select" required>
                                <c:forEach var="type" items="${eventTypes}">
                                    <option value="${type}" ${event.type eq type ? 'selected' : ''}>${type}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="date" class="form-label">Event Date <span class="text-danger">*</span></label>
                            <input type="date" id="date" name="date" class="form-control" 
                                   value="<fmt:formatDate pattern="yyyy-MM-dd" value="${event.date}" />" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="location" class="form-label">Location <span class="text-danger">*</span></label>
                            <input type="text" id="location" name="location" class="form-control" value="${event.location}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="capacity" class="form-label">Capacity <span class="text-danger">*</span></label>
                            <input type="number" id="capacity" name="capacity" class="form-control" min="1" value="${event.capacity}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Description <span class="text-danger">*</span></label>
                            <textarea id="description" name="description" class="form-control" rows="5" required>${event.description}</textarea>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <div>
                                <button type="submit" class="btn btn-primary">Update Event</button>
                                <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-outline-secondary">Cancel</a>
                            </div>
                            <a href="${pageContext.request.contextPath}/events/delete?id=${event.id}" class="btn btn-danger" 
                               onclick="return confirm('Are you sure you want to delete this event?')">Delete Event</a>
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
        
        if (!isValid) {
            event.preventDefault();
        }
    });
});
</script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
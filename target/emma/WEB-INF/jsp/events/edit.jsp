<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>Edit Event</h1>
    </div>
    
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/events/update" method="post" id="eventForm">
            <input type="hidden" name="id" value="${event.id}">
            
            <div class="form-group">
                <label for="name">Event Name <span class="required">*</span></label>
                <input type="text" id="name" name="name" class="form-control" value="${event.name}" required>
            </div>
            
            <div class="form-group">
                <label for="type">Event Type <span class="required">*</span></label>
                <select id="type" name="type" class="form-control" required>
                    <c:forEach var="type" items="${eventTypes}">
                        <option value="${type}" ${event.type eq type ? 'selected' : ''}>${type}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="date">Event Date <span class="required">*</span></label>
                <input type="date" id="date" name="date" class="form-control" 
                       value="<fmt:formatDate pattern="yyyy-MM-dd" value="${event.date}" />" required>
            </div>
            
            <div class="form-group">
                <label for="location">Location <span class="required">*</span></label>
                <input type="text" id="location" name="location" class="form-control" value="${event.location}" required>
            </div>
            
            <div class="form-group">
                <label for="capacity">Capacity <span class="required">*</span></label>
                <input type="number" id="capacity" name="capacity" class="form-control" min="1" value="${event.capacity}" required>
            </div>
            
            <div class="form-group">
                <label for="description">Description <span class="required">*</span></label>
                <textarea id="description" name="description" class="form-control" rows="5" required>${event.description}</textarea>
            </div>
            
            <div class="form-group form-buttons">
                <button type="submit" class="btn btn-primary">Update Event</button>
                <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" class="btn btn-secondary">Cancel</a>
                <a href="${pageContext.request.contextPath}/events/delete?id=${event.id}" class="btn btn-danger" 
                   onclick="return confirm('Are you sure you want to delete this event?')">Delete Event</a>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/eventValidation.js"></script>

<jsp:include page="../common/footer.jsp" />
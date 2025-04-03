<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>Create New Event</h1>
    </div>
    
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/events/insert" method="post" id="eventForm">
            <div class="form-group">
                <label for="name">Event Name <span class="required">*</span></label>
                <input type="text" id="name" name="name" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="type">Event Type <span class="required">*</span></label>
                <select id="type" name="type" class="form-control" required>
                    <option value="">Select Event Type</option>
                    <c:forEach var="type" items="${eventTypes}">
                        <option value="${type}">${type}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="date">Event Date <span class="required">*</span></label>
                <input type="date" id="date" name="date" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="location">Location <span class="required">*</span></label>
                <input type="text" id="location" name="location" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="capacity">Capacity <span class="required">*</span></label>
                <input type="number" id="capacity" name="capacity" class="form-control" min="1" required>
            </div>
            
            <div class="form-group">
                <label for="description">Description <span class="required">*</span></label>
                <textarea id="description" name="description" class="form-control" rows="5" required></textarea>
            </div>
            
            <div class="form-group form-buttons">
                <button type="submit" class="btn btn-primary">Create Event</button>
                <a href="${pageContext.request.contextPath}/events/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/eventValidation.js"></script>

<jsp:include page="../common/footer.jsp" />
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container mt-4">
    <div class="row">
        <div class="col-md-12">
            <div class="card shadow">
                <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <h4 class="mb-0">My Events</h4>
                    <a href="${pageContext.request.contextPath}/events/new" class="btn btn-light">
                        <i class="bi bi-plus-circle"></i> Create New Event
                    </a>
                </div>
                <div class="card-body">
                    <c:if test="${empty events}">
                        <div class="alert alert-info">
                            <p>You haven't created any events yet.</p>
                            <a href="${pageContext.request.contextPath}/events/new" class="btn btn-primary">
                                Create Your First Event
                            </a>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty events}">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th>Name</th>
                                        <th>Date</th>
                                        <th>Location</th>
                                        <th>Type</th>
                                        <th>Attendees</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="event" items="${events}">
                                        <tr>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/events/details?id=${event.id}">
                                                    ${event.name}
                                                </a>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy hh:mm a" />
                                            </td>
                                            <td>${event.location}</td>
                                            <td>${event.type}</td>
                                            <td>
                                                ${event.attendeeCount} / ${event.capacity}
                                                <div class="progress" style="height: 5px;">
                                                    <div class="progress-bar" role="progressbar" 
                                                         style="width: <c:out value="${event.capacity > 0 ? (event.attendeeCount * 100 / event.capacity) : 0}"/>%;" 
                                                         aria-valuenow="${event.attendeeCount}" 
                                                         aria-valuemin="0" 
                                                         aria-valuemax="${event.capacity}">
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <a href="${pageContext.request.contextPath}/events/edit?id=${event.id}" 
                                                       class="btn btn-outline-primary">
                                                        <i class="bi bi-pencil"></i> Edit
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/events/details?id=${event.id}" 
                                                       class="btn btn-outline-info">
                                                        <i class="bi bi-eye"></i> View
                                                    </a>
                                                    <button type="button" class="btn btn-outline-danger"
                                                            onclick="confirmDelete('${event.id}', '${event.name}')">
                                                        <i class="bi bi-trash"></i> Delete
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </div>
            </div>
            
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/users/myRSVPs" class="btn btn-outline-primary">
                    View My RSVPs
                </a>
                <a href="${pageContext.request.contextPath}/events/list" class="btn btn-outline-secondary">
                    View All Events
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete the event: <span id="eventName" class="fw-bold"></span>?
                <p class="text-danger mt-2">This action cannot be undone.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <a href="#" id="confirmDeleteButton" class="btn btn-danger">Delete Event</a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmDelete(eventId, eventName) {
        document.getElementById('eventName').textContent = eventName;
        var baseUrl = '${pageContext.request.contextPath}';
        document.getElementById('confirmDeleteButton').href = baseUrl + '/events/delete?id=' + eventId;
        var deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
        deleteModal.show();
    }
</script>

<jsp:include page="../common/footer.jsp" />
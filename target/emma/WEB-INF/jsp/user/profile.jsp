<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container mt-5">
    <div class="row">
        <div class="col-md-8 mx-auto">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">My Profile</h4>
                </div>
                <div class="card-body">
                    <c:if test="${empty user}">
                        <div class="alert alert-warning">
                            User information not available. Please <a href="${pageContext.request.contextPath}/users/login">login</a> to view your profile.
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty user}">
                        <div class="row mb-4">
                            <div class="col-md-12">
                                <h5 class="border-bottom pb-2">User Information</h5>
                                <div class="row mt-3">
                                    <div class="col-md-4 text-muted">Username:</div>
                                    <div class="col-md-8">${user.username}</div>
                                </div>
                                <div class="row mt-2">
                                    <div class="col-md-4 text-muted">Email:</div>
                                    <div class="col-md-8">${user.email}</div>
                                </div>
                                <div class="row mt-2">
                                    <div class="col-md-4 text-muted">Member Since:</div>
                                    <div class="col-md-8">
                                        <c:choose>
                                            <c:when test="${not empty user.registeredDate}">
                                                <fmt:formatDate pattern="MMMM d, yyyy" value="${user.registeredDate}" />
                                            </c:when>
                                            <c:otherwise>
                                                Not available
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mt-2">
                                    <div class="col-md-4 text-muted">Role:</div>
                                    <div class="col-md-8">${user.role}</div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-12">
                                <h5 class="border-bottom pb-2">My Activity</h5>
                                <div class="row mt-3">
                                    <div class="col-md-4">
                                        <a href="${pageContext.request.contextPath}/users/myEvents" class="btn btn-outline-primary w-100">
                                            <i class="bi bi-calendar-event"></i> My Events
                                        </a>
                                    </div>
                                    <div class="col-md-4">
                                        <a href="${pageContext.request.contextPath}/users/myRSVPs" class="btn btn-outline-primary w-100">
                                            <i class="bi bi-check-circle"></i> My RSVPs
                                        </a>
                                    </div>
                                    <div class="col-md-4">
                                        <a href="${pageContext.request.contextPath}/events/new" class="btn btn-outline-primary w-100">
                                            <i class="bi bi-plus-circle"></i> Create Event
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-12">
                                <h5 class="border-bottom pb-2">Account Settings</h5>
                                <div class="row mt-3">
                                    <div class="col-md-6">
                                        <a href="#" class="btn btn-outline-secondary w-100" disabled>
                                            <i class="bi bi-key"></i> Change Password
                                        </a>
                                    </div>
                                    <div class="col-md-6">
                                        <a href="#" class="btn btn-outline-secondary w-100" disabled>
                                            <i class="bi bi-pencil"></i> Edit Profile
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
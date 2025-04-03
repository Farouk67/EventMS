<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>My Profile</h1>
    </div>
    
    <div class="profile-container">
        <div class="profile-section">
            <h2>User Information</h2>
            <div class="profile-info">
                <div class="info-item">
                    <span class="label">Username:</span>
                    <span class="value">${user.username}</span>
                </div>
                <div class="info-item">
                    <span class="label">Email:</span>
                    <span class="value">${user.email}</span>
                </div>
                <div class="info-item">
                    <span class="label">Member Since:</span>
                    <span class="value"><fmt:formatDate pattern="MMMM d, yyyy" value="${user.registeredDate}" /></span>
                </div>
            </div>
        </div>
        
        <div class="profile-section">
            <h2>My Activity</h2>
            <div class="profile-links">
                <a href="${pageContext.request.contextPath}/users/myEvents" class="profile-link">
                    <i class="icon-calendar"></i>
                    <span>My Events</span>
                </a>
                <a href="${pageContext.request.contextPath}/users/myRSVPs" class="profile-link">
                    <i class="icon-check"></i>
                    <span>My RSVPs</span>
                </a>
                <a href="${pageContext.request.contextPath}/events/new" class="profile-link">
                    <i class="icon-plus"></i>
                    <span>Create New Event</span>
                </a>
            </div>
        </div>
        
        <div class="profile-section">
            <h2>Account Settings</h2>
            <div class="profile-actions">
                <a href="#" class="btn btn-secondary">Change Password</a>
                <a href="#" class="btn btn-secondary">Edit Profile</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
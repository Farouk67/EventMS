<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - Emma's Event Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/navigation.jsp" />
    
    <main class="container profile-container">
        <div class="profile-header">
            <div class="profile-image">
                <c:choose>
                    <c:when test="${not empty user.profileImageUrl}">
                        <img src="${user.profileImageUrl}" alt="${user.username}'s profile picture">
                    </c:when>
                    <c:otherwise>
                        <div class="profile-placeholder">
                            ${user.firstName.charAt(0)}${user.lastName.charAt(0)}
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="profile-info">
                <h1>${user.fullName}</h1>
                <p class="username">@${user.username}</p>
                <p class="member-since">Member since <fmt:formatDate value="${user.registeredDate}" pattern="MMMM yyyy" /></p>
                
                <c:if test="${not empty user.bio}">
                    <div class="bio">
                        <p>${user.bio}</p>
                    </div>
                </c:if>
            </div>
        </div>
        
        <div class="profile-tabs">
            <ul class="tabs">
                <li class="tab-item active" data-tab="my-events">My Events</li>
                <li class="tab-item" data-tab="my-rsvps">My RSVPs</li>
                <li class="tab-item" data-tab="account-settings">Account Settings</li>
            </ul>
            
            <div class="tab-content">
                <div id="my-events" class="tab-pane active">
                    <div class="section-header">
                        <h2>Events I'm Organizing</h2>
                        <a href="${pageContext.request.contextPath}/events/create" class="btn btn-outline-primary">Create New Event</a>
                    </div>
                    
                    <c:choose>
                        <c:when test="${not empty user.createdEvents}">
                            <div class="events-grid">
                                <c:forEach var="event" items="${user.createdEvents}">
                                    <div class="event-card">
                                        <div class="event-card-header">
                                            <span class="event-type">${event.type}</span>
                                            <h3 class="event-name">${event.name}</h3>
                                        </div>
                                        
                                        <div class="event-card-body">
                                            <p class="event-date">
                                                <i class="icon-calendar"></i>
                                                <fmt:formatDate value="${event.date}" pattern="MMM d, yyyy" />
                                            </p>
                                            <p class="event-location">
                                                <i class="icon-location"></i>
                                                ${event.location}
                                            </p>
                                            <p class="event-attendees">
                                                <i class="icon-user"></i>
                                                ${event.attendeeCount} / ${event.capacity}
                                            </p>
                                        </div>
                                        
                                        <div class="event-card-footer">
                                            <a href="${pageContext.request.contextPath}/events/view?id=${event.id}" class="btn btn-text">View</a>
                                            <a href="${pageContext.request.contextPath}/events/edit?id=${event.id}" class="btn btn-text">Edit</a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <p>You haven't created any events yet.</p>
                                <a href="${pageContext.request.contextPath}/events/create" class="btn btn-primary">Create your first event</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div id="my-rsvps" class="tab-pane">
                    <h2>Events I'm Attending</h2>
                    
                    <c:choose>
                        <c:when test="${not empty user.rsvps}">
                            <div class="rsvp-list">
                                <c:forEach var="rsvp" items="${user.rsvps}">
                                    <div class="rsvp-item ${rsvp.status}">
                                        <div class="rsvp-event-info">
                                            <h3>${rsvp.event.name}</h3>
                                            <p class="event-date">
                                                <fmt:formatDate value="${rsvp.event.date}" pattern="EEEE, MMMM d, yyyy" />
                                            </p>
                                            <p class="event-location">${rsvp.event.location}</p>
                                        </div>
                                        
                                        <div class="rsvp-status">
                                            <span class="status-badge ${rsvp.status}">${rsvp.status}</span>
                                            <p class="responded-at">
                                                Responded <fmt:formatDate value="${rsvp.respondedAt}" pattern="MMM d, yyyy" />
                                            </p>
                                        </div>
                                        
                                        <div class="rsvp-actions">
                                            <a href="${pageContext.request.contextPath}/events/view?id=${rsvp.event.id}" class="btn btn-text">View Event</a>
                                            <a href="${pageContext.request.contextPath}/rsvp/edit?id=${rsvp.id}" class="btn btn-text">Update RSVP</a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <p>You haven't RSVP'd to any events yet.</p>
                                <a href="${pageContext.request.contextPath}/events" class="btn btn-primary">Browse events</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div id="account-settings" class="tab-pane">
                    <h2>Account Settings</h2>
                    
                    <form action="${pageContext.request.contextPath}/user/update" method="post" class="profile-form">
                        <div class="form-section">
                            <h3>Personal Information</h3>
                            
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" id="email" name="email" value="${user.email}" required>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="firstName">First Name</label>
                                    <input type="text" id="firstName" name="firstName" value="${user.firstName}" required>
                                </div>
                                
                                <div class="form-group col-md-6">
                                    <label for="lastName">Last Name</label>
                                    <input type="text" id="lastName" name="lastName" value="${user.lastName}" required>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="bio">Bio</label>
                                <textarea id="bio" name="bio" rows="4">${user.bio}</textarea>
                            </div>
                        </div>
                        
                        <div class="form-section">
                            <h3>Password</h3>
                            
                            <div class="form-group">
                                <label for="currentPassword">Current Password</label>
                                <input type="password" id="currentPassword" name="currentPassword">
                            </div>
                            
                            <div class="form-group">
                                <label for="newPassword">New Password</label>
                                <input type="password" id="newPassword" name="newPassword">
                            </div>
                            
                            <div class="form-group">
                                <label for="confirmPassword">Confirm New Password</label>
                                <input type="password" id="confirmPassword" name="confirmPassword">
                            </div>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="../common/footer.jsp" />
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const tabs = document.querySelectorAll('.tab-item');
            const tabPanes = document.querySelectorAll('.tab-pane');
            
            tabs.forEach(tab => {
                tab.addEventListener('click', function() {
                    const tabId = this.getAttribute('data-tab');
                    
                    // Remove active class from all tabs and panes
                    tabs.forEach(t => t.classList.remove('active'));
                    tabPanes.forEach(p => p.classList.remove('active'));
                    
                    // Add active class to current tab and pane
                    this.classList.add('active');
                    document.getElementById(tabId).classList.add('active');
                });
            });
            
            const profileForm = document.querySelector('.profile-form');
            if (profileForm) {
                profileForm.addEventListener('submit', function(e) {
                    const newPassword = document.getElementById('newPassword').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;
                    
                    if (newPassword && newPassword !== confirmPassword) {
                        e.preventDefault();
                        alert('New passwords do not match!');
                    }
                });
            }
        });
    </script>
</body>
</html>
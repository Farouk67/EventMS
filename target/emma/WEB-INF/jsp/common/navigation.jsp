<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="main-nav">
    <div class="container">
        <ul class="nav-list">
            <li><a href="${pageContext.request.contextPath}/events/list">All Events</a></li>
            <c:if test="${not empty eventTypes}">
                <li class="dropdown">
                    <a href="#" class="dropbtn">Event Types</a>
                    <div class="dropdown-content">
                        <c:forEach var="type" items="${eventTypes}">
                            <a href="${pageContext.request.contextPath}/events/byType?type=${type}">${type}</a>
                        </c:forEach>
                    </div>
                </li>
            </c:if>
            <li><a href="${pageContext.request.contextPath}/search">Search Events</a></li>
            <c:if test="${not empty sessionScope.userId}">
                <li><a href="${pageContext.request.contextPath}/events/new">Create Event</a></li>
                <li><a href="${pageContext.request.contextPath}/users/myEvents">My Events</a></li>
                <li><a href="${pageContext.request.contextPath}/users/myRSVPs">My RSVPs</a></li>
            </c:if>
        </ul>
    </div>
</nav>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarMain">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/events/list">All Events</a>
                </li>
                <c:if test="${not empty eventTypes}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            Event Types
                        </a>
                        <ul class="dropdown-menu">
                            <c:forEach var="type" items="${eventTypes}">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/events/byType?type=${type}">${type}</a></li>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/search">Search Events</a>
                </li>
                <c:if test="${not empty sessionScope.userId}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/events/new">Create Event</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/users/myEvents">My Events</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/users/myRSVPs">My RSVPs</a>
                    </li>
                    <c:if test="${sessionScope.userRole eq 'admin'}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/ml-dashboard">ML Dashboard</a>
                    </li>
                    </c:if>
                </c:if>
            </ul>
        </div>
    </div>
</nav>
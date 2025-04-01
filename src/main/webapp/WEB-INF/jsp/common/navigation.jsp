<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath eq '/index.jsp' ? 'active' : ''}" href="${pageContext.request.contextPath}/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/events/list') ? 'active' : ''}" href="${pageContext.request.contextPath}/events">Browse Events</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/events/create') ? 'active' : ''}" href="${pageContext.request.contextPath}/events/create">Create Event</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath.contains('/rsvp/list') ? 'active' : ''}" href="${pageContext.request.contextPath}/rsvp/list">My RSVPs</a>
                </li>
            </ul>

            <div class="d-flex">
                <form class="d-flex me-2" action="${pageContext.request.contextPath}/events/search" method="GET">
                    <input class="form-control me-2" type="search" name="query" placeholder="Search events..." aria-label="Search">
                    <button class="btn btn-outline-light" type="submit">Search</button>
                </form>
                
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/user/login" class="btn btn-outline-light me-2">Login</a>
                        <a href="${pageContext.request.contextPath}/user/register" class="btn btn-primary">Register</a>
                    </c:when>
                    <c:otherwise>
                        <div class="dropdown">
                            <button class="btn btn-outline-light dropdown-toggle" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                ${sessionScope.user.username}
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/profile">My Profile</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/events/myevents">My Events</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/logout">Logout</a></li>
                            </ul>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="header.jsp" />
<jsp:include page="navigation.jsp" />

<div class="container">
    <div class="error-page">
        <div class="error-code">404</div>
        <h1>Page Not Found</h1>
        <div class="error-description">
            <p>We couldn't find the page you were looking for.</p>
            <p>The page may have been moved, deleted, or the URL might have been typed incorrectly.</p>
        </div>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Home Page</a>
            <a href="${pageContext.request.contextPath}/events/list" class="btn btn-secondary">View Events</a>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />
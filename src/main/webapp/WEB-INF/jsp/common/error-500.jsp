<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="header.jsp" />
<jsp:include page="navigation.jsp" />

<div class="container">
    <div class="error-page">
        <div class="error-code">500</div>
        <h1>Internal Server Error</h1>
        <div class="error-description">
            <p>Oops! Something went wrong on our end.</p>
            <p>Our technical team has been notified and is working to fix the issue.</p>
            <p>Please try again later or contact support if the problem persists.</p>
        </div>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Home Page</a>
            <a href="${pageContext.request.contextPath}/events/list" class="btn btn-secondary">View Events</a>
        </div>
        
        <c:if test="${pageContext.request.isUserInRole('admin')}">
            <div class="error-details">
                <h3>Error Details (Admin Only)</h3>
                <div class="error-trace">
                    <p>Error Type: ${pageContext.exception.getClass().getName()}</p>
                    <p>Error Message: ${pageContext.exception.message}</p>
                    <pre>
                        <c:forEach var="stackTraceElement" items="${pageContext.exception.stackTrace}">
                            ${stackTraceElement}
                        </c:forEach>
                    </pre>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="footer.jsp" />
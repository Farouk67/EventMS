<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="header.jsp" />
<jsp:include page="navigation.jsp" />

<div class="container">
    <div class="error-page">
        <div class="error-icon">
            <i class="fa fa-exclamation-triangle"></i>
        </div>
        <h1>Oops! Something Went Wrong</h1>
        <div class="error-description">
            <p>We encountered an unexpected error while processing your request.</p>
            <p>Please try again or contact our support team if the issue persists.</p>
        </div>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Home Page</a>
            <a href="${pageContext.request.contextPath}/events/list" class="btn btn-secondary">View Events</a>
            <button onclick="window.history.back();" class="btn btn-secondary">Go Back</button>
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
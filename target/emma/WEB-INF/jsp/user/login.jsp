<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>Login</h1>
    </div>
    
    <div class="form-container">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>
        
        <c:if test="${not empty message}">
            <div class="alert alert-success">
                ${message}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/users/processLogin" method="post">
            <div class="form-group">
                <label for="username">Username <span class="required">*</span></label>
                <input type="text" id="username" name="username" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>
            
            <div class="form-group form-buttons">
                <button type="submit" class="btn btn-primary">Login</button>
            </div>
        </form>
        
        <div class="form-footer">
            <p>Don't have an account? <a href="${pageContext.request.contextPath}/users/register">Register here</a></p>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
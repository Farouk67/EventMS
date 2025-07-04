<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navigation.jsp" />

<div class="container mt-5">
    <div class="row">
        <div class="col-md-6 mx-auto">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Login</h4>
                </div>
                <div class="card-body">
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
                        <div class="mb-3">
                            <label for="username" class="form-label">Username <span class="text-danger">*</span></label>
                            <input type="text" id="username" name="username" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="password" class="form-label">Password <span class="text-danger">*</span></label>
                            <input type="password" id="password" name="password" class="form-control" required>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">Login</button>
                        </div>
                    </form>
                    
                    <div class="mt-3 text-center">
                        <p>Don't have an account? <a href="${pageContext.request.contextPath}/users/register">Register here</a></p>
                    </div>
                    
                    <div class="mt-3 text-center">
                        <p>Forgot your password? <a href="${pageContext.request.contextPath}/users/forgotPassword">Reset it here</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
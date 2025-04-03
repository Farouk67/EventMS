<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Emma's Event Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <header class="main-header">
        <div class="container">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/index.jsp">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Emma's Event Management">
                </a>
            </div>
            <div class="user-actions">
                <c:choose>
                    <c:when test="${not empty sessionScope.username}">
                        <span>Welcome, ${sessionScope.username}</span>
                        <a href="${pageContext.request.contextPath}/users/profile" class="btn btn-secondary">My Profile</a>
                        <a href="${pageContext.request.contextPath}/users/logout" class="btn btn-secondary">Logout</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/users/login" class="btn btn-primary">Login</a>
                        <a href="${pageContext.request.contextPath}/users/register" class="btn btn-secondary">Register</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </header>
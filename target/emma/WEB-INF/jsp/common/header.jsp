<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <meta name="description" content="Emma's Event Management">
    <meta name="keywords" content="events, management, RSVP">
    <meta name="author" content="Farouk">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Emma's Event Management</title>
    
    <!-- Bootstrap CSS -->
     <!-- In your header.jsp -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    
    <!-- My custom CSS (after Bootstrap) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>
    <header class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Emma's Event Management" height="90">
                Emma's Event Management
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <div class="navbar-nav ms-auto">
                    <c:choose>
                        <c:when test="${not empty sessionScope.username}">
                            <span class="nav-item nav-link">Welcome, ${sessionScope.username}</span>
                            <a href="${pageContext.request.contextPath}/users/profile" class="nav-item nav-link">My Profile</a>
                            <a href="${pageContext.request.contextPath}/users/logout" class="nav-item nav-link">Logout</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/users/login" class="nav-item nav-link">Login</a>
                            <a href="${pageContext.request.contextPath}/users/register" class="nav-item nav-link">Register</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </header>
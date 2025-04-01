<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Emma's Event Management</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <header class="bg-primary text-white py-3">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-3">
                    <a href="${pageContext.request.contextPath}/" class="text-white text-decoration-none">
                        <img src="${pageContext.request.contextPath}/images/logo.png" alt="Emma's Event Management" class="logo">
                    </a>
                </div>
                <div class="col-md-9">
                    <h1>Emma's Event Management</h1>
                    <p class="lead mb-0">Your trusted partner for all event needs</p>
                </div>
            </div>
        </div>
    </header>
    <jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />
    <div class="container main-content mt-4">
        <!-- Main content will be here -->
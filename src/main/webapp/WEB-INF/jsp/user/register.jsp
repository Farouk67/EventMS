<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container">
    <div class="page-header">
        <h1>Register</h1>
    </div>
    
    <div class="form-container">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/users/processRegister" method="post">
            <div class="form-group">
                <label for="username">Username <span class="required">*</span></label>
                <input type="text" id="username" name="username" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email <span class="required">*</span></label>
                <input type="email" id="email" name="email" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">Confirm Password <span class="required">*</span></label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
            </div>
            
            <div class="form-group form-buttons">
                <button type="submit" class="btn btn-primary">Register</button>
            </div>
        </form>
        
        <div class="form-footer">
            <p>Already have an account? <a href="${pageContext.request.contextPath}/users/login">Login here</a></p>
        </div>
    </div>
</div>

<script>

document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.querySelector('form');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const emailInput = document.getElementById('email');

    registerForm.addEventListener('submit', function(event) {
        // Username validation
        const username = document.getElementById('username');
        if (username.value.trim().length < 3) {
            alert('Username must be at least 3 characters long');
            event.preventDefault();
            username.focus();
            return;
        }

        // Email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailInput.value)) {
            alert('Please enter a valid email address');
            event.preventDefault();
            emailInput.focus();
            return;
        }

        // Password validation
        if (passwordInput.value.length < 8) {
            alert('Password must be at least 8 characters long');
            event.preventDefault();
            passwordInput.focus();
            return;
        }

        // Password match validation
        if (passwordInput.value !== confirmPasswordInput.value) {
            alert('Passwords do not match');
            event.preventDefault();
            confirmPasswordInput.focus();
            return;
        }
    });

    // Optional: Password strength indicator
    passwordInput.addEventListener('input', function() {
        const strength = calculatePasswordStrength(this.value);
        updatePasswordStrengthIndicator(strength);
    });
});

function calculatePasswordStrength(password) {
    let strength = 0;
    if (password.length >= 8) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[a-z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;
    return strength;
}

function updatePasswordStrengthIndicator(strength) {
    const indicator = document.getElementById('passwordStrength');
    if (!indicator) return;

    const levels = ['Very Weak', 'Weak', 'Medium', 'Strong', 'Very Strong'];
    const colors = ['red', 'orange', 'yellow', 'lightgreen', 'green'];

    indicator.textContent = levels[strength] || 'Very Weak';
    indicator.style.color = colors[strength] || 'red';
}
</script>

<jsp:include page="../common/footer.jsp" />
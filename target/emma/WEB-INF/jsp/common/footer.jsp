<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<footer class="bg-dark text-light py-4 mt-5">
    <div class="container">
        <div class="row">
            <div class="col-md-4 mb-3">
                <h5>Emma's Event Management</h5>
                <p>Making your events memorable since 2025</p>
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Emma's Event Management" 
                    style="max-height: 100px; max-width: 250px; margin: 10px 0; float: left;" 
                    class="footer-logo">
            </div>
            <div class="col-md-4 mb-3">
                <h5>Quick Links</h5>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/events/list" class="text-light">All Events</a></li>
                    <li><a href="${pageContext.request.contextPath}/search" class="text-light">Search Events</a></li>
                    <li><a href="${pageContext.request.contextPath}/events/create" class="text-light">Create Event</a></li>
                </ul>
            </div>
            <div class="col-md-4 mb-3">
                <h5>Contact Us</h5>
                <address class="mb-0">
                    <p><i class="bi bi-envelope me-2"></i>admin@emmaevents.com</p>
                    <p><i class="bi bi-telephone me-2"></i>(077) 654-3210</p>
                    <p><i class="bi bi-geo-alt me-2"></i>3 Event Street, Coventry, West Midlands UK</p>
                </address>
            </div>
        </div>
        <hr class="my-3">
        <div class="text-center">
            <p class="mb-0">&copy; 2025 Emma's Event Management. All rights reserved.</p>
        </div>
    </div>
</footer>

<!-- Bootstrap JS Bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- Your custom JavaScript -->
<script src="${pageContext.request.contextPath}/js/eventValidation.js"></script>
<script src="${pageContext.request.contextPath}/js/searchFilter.js"></script>
<script src="${pageContext.request.contextPath}/js/rsvpManagement.js"></script>

</body>
</html>
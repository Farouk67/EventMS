<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
            <h1>Create New Event</h1>
        </div>
    </div>
    
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <div class="card shadow">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/events/insert" method="post" id="eventForm">
                        <div class="mb-3">
                            <label for="name" class="form-label">Event Name <span class="text-danger">*</span></label>
                            <input type="text" id="name" name="name" class="form-control" required onblur="updatePrediction()">
                        </div>
                        
                        <div class="mb-3">
                            <label for="type" class="form-label">Event Type <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <select id="type" name="type" class="form-select">
                                    <option value="">Select Event Type</option>
                                    <c:forEach var="type" items="${eventTypes}">
                                        <option value="${type}">${type}</option>
                                    </c:forEach>
                                </select>
                                <button type="button" class="btn btn-outline-secondary" id="predictBtn" 
                                        onclick="predictEventType()">Predict Type</button>
                            </div>
                            <div id="typePrediction" class="form-text text-info mt-1" style="display: none;">
                                <i class="bi bi-magic"></i> ML prediction: <span id="predictedType"></span>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="date" class="form-label">Event Date <span class="text-danger">*</span></label>
                            <input type="date" id="date" name="date" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="location" class="form-label">Location <span class="text-danger">*</span></label>
                            <input type="text" id="location" name="location" class="form-control" required onblur="updatePrediction()">
                        </div>
                        
                        <div class="mb-3">
                            <label for="capacity" class="form-label">Capacity <span class="text-danger">*</span></label>
                            <input type="number" id="capacity" name="capacity" class="form-control" min="1" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Description <span class="text-danger">*</span></label>
                            <textarea id="description" name="description" class="form-control" rows="5" required onblur="updatePrediction()"></textarea>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <button type="submit" class="btn btn-primary">Create Event</button>
                            <a href="${pageContext.request.contextPath}/events/list" class="btn btn-outline-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
let predictionReady = false;

// Function to check if prediction can be made
function updatePrediction() {
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const location = document.getElementById('location').value;
    
    if (name && description && location) {
        predictionReady = true;
        document.getElementById('predictBtn').classList.remove('btn-outline-secondary');
        document.getElementById('predictBtn').classList.add('btn-info');
    } else {
        predictionReady = false;
        document.getElementById('predictBtn').classList.remove('btn-info');
        document.getElementById('predictBtn').classList.add('btn-outline-secondary');
    }
}

// Function to predict event type
function predictEventType() {
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const location = document.getElementById('location').value;
    
    if (!name || !description || !location) {
        alert("Please fill in name, description, and location fields first");
        return;
    }
    
    // Show loading indicator
    document.getElementById('predictedType').textContent = "Predicting...";
    document.getElementById('typePrediction').style.display = "block";
    
    // In a real implementation, this would make an AJAX call to the server
    // Here we'll simulate the prediction using the existing server logic
    
    // Create a hidden form to send data to the server for prediction
    const form = new FormData();
    form.append('name', name);
    form.append('description', description);
    form.append('location', location);
    
    // Send the data to a prediction endpoint
    fetch('${pageContext.request.contextPath}/events/predict-type', {
        method: 'POST',
        body: form
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('predictedType').textContent = data.predictedType;
        
        // Select the predicted type in the dropdown
        const typeSelect = document.getElementById('type');
        for (let i = 0; i < typeSelect.options.length; i++) {
            if (typeSelect.options[i].value === data.predictedType) {
                typeSelect.selectedIndex = i;
                break;
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('predictedType').textContent = "Prediction failed";
    });
}

// Standard form validation code
document.getElementById('eventForm').addEventListener('submit', function(e) {
    const name = document.getElementById('name').value.trim();
    const type = document.getElementById('type').value;
    const date = document.getElementById('date').value;
    const location = document.getElementById('location').value.trim();
    const capacity = document.getElementById('capacity').value;
    const description = document.getElementById('description').value.trim();
    
    if (!name || !date || !location || !capacity || !description) {
        e.preventDefault();
        alert('Please fill in all required fields');
    }
});
</script>

<jsp:include page="../common/footer.jsp" />
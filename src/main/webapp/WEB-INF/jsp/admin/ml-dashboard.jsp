<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navigation.jsp" />

<div class="container mt-4">
    <div class="row mb-4">
        <div class="col">
            <h1>ML Model Management</h1>
        </div>
    </div>
    
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">Event Type Prediction Model</h5>
                </div>
                <div class="card-body">
                    <!-- Model Statistics -->
                    <div class="mb-4">
                        <h6>Current Model:</h6>
                        <c:if test="${modelInfo != null}">
                            <p><strong>Accuracy:</strong> ${modelInfo.accuracy}%</p>
                            <p><strong>Total Training Examples:</strong> ${modelInfo.trainingSize}</p>
                            <p><strong>Last Trained:</strong> ${modelInfo.lastTrained}</p>
                        </c:if>
                        <c:if test="${modelInfo == null}">
                            <p class="text-warning">No model information available.</p>
                        </c:if>
                    </div>
                    
                    <!-- Training Options -->
                    <div class="mb-4">
                        <h6>Training Options:</h6>
                        <form action="${pageContext.request.contextPath}/admin/train-model" method="post">
                            <div class="mb-3">
                                <label for="trainingSize" class="form-label">Number of training examples:</label>
                                <input type="number" id="trainingSize" name="trainingSize" class="form-control" 
                                       value="100" min="50" max="500">
                            </div>
                            <button type="submit" class="btn btn-primary">Train New Model</button>
                        </form>
                    </div>
                    
                    <!-- Test Prediction -->
                    <div>
                        <h6>Test Prediction:</h6>
                        <form id="testForm" class="mb-3">
                            <div class="mb-3">
                                <label for="testName" class="form-label">Event Name:</label>
                                <input type="text" id="testName" name="name" class="form-control" 
                                       placeholder="e.g., AI Summit 2025">
                            </div>
                            <div class="mb-3">
                                <label for="testDescription" class="form-label">Description:</label>
                                <textarea id="testDescription" name="description" class="form-control" 
                                          rows="3" placeholder="Enter event description"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="testLocation" class="form-label">Location:</label>
                                <input type="text" id="testLocation" name="location" class="form-control" 
                                       placeholder="e.g., London">
                            </div>
                            <button type="button" class="btn btn-outline-primary" onclick="testPrediction()">
                                Predict Type
                            </button>
                        </form>
                        
                        <div id="predictionResult" class="alert alert-info" style="display: none;">
                            <h6>Predicted Type: <span id="predictedType"></span></h6>
                            <div id="predictionDetails"></div>
                        </div>
                    </div>
                    
                    <!-- Training Data Analysis -->
                    <div id="trainingDataSection" style="display: none;">
                        <hr>
                        <h6>Training Data Analysis:</h6>
                        
                        <div class="mt-3">
                            <h6>Event Type Distribution:</h6>
                            <div id="typeDistribution" class="table-responsive">
                                <table class="table table-sm table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Event Type</th>
                                            <th>Count</th>
                                            <th>Distribution</th>
                                        </tr>
                                    </thead>
                                    <tbody id="typeDistributionBody">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        
                        <div class="mt-4">
                            <h6>Sample Generated Events:</h6>
                            <div id="sampleEvents" class="table-responsive">
                                <table class="table table-sm table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Type</th>
                                            <th>Name</th>
                                            <th>Description</th>
                                        </tr>
                                    </thead>
                                    <tbody id="sampleEventsBody">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function testPrediction() {
    const name = document.getElementById('testName').value;
    const description = document.getElementById('testDescription').value;
    const location = document.getElementById('testLocation').value;
    
    if (!name || !description || !location) {
        alert("Please fill in all fields");
        return;
    }
    
    // Show loading
    document.getElementById('predictionResult').style.display = 'block';
    document.getElementById('predictedType').textContent = 'Predicting...';
    document.getElementById('predictionDetails').innerHTML = '';
    
    // Hide training data section initially
    document.getElementById('trainingDataSection').style.display = 'none';
    
    // Send request to prediction endpoint
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('location', location);
    
    fetch('${pageContext.request.contextPath}/events/predict-type', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('predictedType').textContent = data.predictedType;
        
        // Show details if available
        let details = '';
        if (data.mlPrediction && data.rulePrediction) {
            details += `<p><strong>ML Prediction:</strong> ${data.mlPrediction}</p>`;
            details += `<p><strong>Rule-based Prediction:</strong> ${data.rulePrediction}</p>`;
        }
        document.getElementById('predictionDetails').innerHTML = details;
        
        // Display training data information if available
        if (data.trainingData) {
            displayTrainingDataInfo(data.trainingData);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('predictedType').textContent = 'Error';
        document.getElementById('predictionDetails').innerHTML = '<p>Failed to get prediction</p>';
    });
}

function displayTrainingDataInfo(trainingData) {
    const trainingSection = document.getElementById('trainingDataSection');
    trainingSection.style.display = 'block';
    
    // Display type distribution
    if (trainingData.typeCounts) {
        const typeTable = document.getElementById('typeDistributionBody');
        typeTable.innerHTML = '';
        
        // Calculate total for percentage
        let total = 0;
        for (const type in trainingData.typeCounts) {
            total += trainingData.typeCounts[type];
        }
        
        // Add rows for each type
        for (const type in trainingData.typeCounts) {
            const count = trainingData.typeCounts[type];
            const percentage = ((count / total) * 100).toFixed(1);
            
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${type}</td>
                <td>${count}</td>
                <td>
                    <div class="progress">
                        <div class="progress-bar" role="progressbar" 
                             style="width: ${percentage}%" 
                             aria-valuenow="${percentage}" 
                             aria-valuemin="0" 
                             aria-valuemax="100">${percentage}%</div>
                    </div>
                </td>
            `;
            typeTable.appendChild(row);
        }
    }
    
    // Display sample events
    if (trainingData.sampleEvents) {
        const eventsTable = document.getElementById('sampleEventsBody');
        eventsTable.innerHTML = '';
        
        trainingData.sampleEvents.forEach(event => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><span class="badge bg-primary">${event.type}</span></td>
                <td>${event.name}</td>
                <td>${event.description}</td>
            `;
            eventsTable.appendChild(row);
        });
    }
}
</script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
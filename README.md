# Event Management Platform with Machine Learning Integration

This Java-based web platform allows users to create, manage, and classify events. A unique component of the system is the use of the WEKA machine learning library to predict event types based on user-input data.

## 🛠️ Technologies Used

- **Java EE** (Servlets, JSP)
- **MySQL** – Backend relational database
- **WEKA API** – For machine learning integration
- **HTML/CSS/Bootstrap** – Front-end design
- **MVC Architecture**

## 🧠 Machine Learning Component

- Classification models trained using WEKA
- Features used include location, duration, audience, and budget
- Model trained to predict categories like “Business,” “Social,” or “Academic”
- Included performance validation and tuning via WEKA Explorer

## 🎯 Key Features

- User registration and authentication
- Event creation and management
- Automatic classification of new events
- Admin view with event analytics
- Search and filter functionality

## 🧩 Project Highlights

- Seamless integration of ML into a web-based UI
- Built a system that was not only functional but **intelligently responsive**
- Focused on data quality and real-world utility of classification results

## 🤝 Real-World Use Case

Designed to support organizations that host multiple types of events and want to automate category tagging and analytics to streamline internal processes.

## 📂 Files

- `/src/` – Java classes for controllers and ML logic
- `/webapp/` – JSP pages and static assets
- `/models/` – WEKA `.model` files and training datasets
- `/sql/` – Database schema and test data

## 🚀 Future Goals

- Deploy to a public-facing cloud platform
- Add data visualization for event trends
- Allow users to provide feedback to refine the model

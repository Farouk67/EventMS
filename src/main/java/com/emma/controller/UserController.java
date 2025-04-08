package com.emma.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.emma.model.User;
import com.emma.service.UserService;
import com.emma.service.ServiceFactory;

public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    
    public UserController() {
        super();
        // Use the ServiceFactory to get the UserService instance
        userService = ServiceFactory.getUserService();
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Double-check that the service is initialized
        if (userService == null) {
            userService = ServiceFactory.getUserService();
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            if (action == null) {
                response.sendRedirect(request.getContextPath() + "/events/list");
                return;
            }
            
            switch (action) {
                case "/register":
                    showRegistrationForm(request, response);
                    break;
                case "/processRegister":
                    processRegistration(request, response);
                    break;
                case "/login":
                    showLoginForm(request, response);
                    break;
                case "/processLogin":
                    processLogin(request, response);
                    break;
                case "/logout":
                    processLogout(request, response);
                    break;
                case "/profile":
                    showUserProfile(request, response);
                    break;
                case "/myEvents":
                    showUserEvents(request, response);
                    break;
                case "/myRSVPs":
                    showUserRSVPs(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/events/list");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void showRegistrationForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp");
        dispatcher.forward(request, response);
    }
    
    private void processRegistration(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        // Registration logic remains the same
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "All fields are required");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Check if username or email already exists
        if (userService.isUsernameTaken(username) || userService.isEmailTaken(email)) {
            request.setAttribute("errorMessage", "Username or email already exists");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Create user
        User user = new User(0, username, email, password);
        userService.registerUser(user);
        
        // Redirect to login page
        request.setAttribute("message", "Registration successful! Please login.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp");
        dispatcher.forward(request, response);
    }
    
    private void processLogin(HttpServletRequest request, HttpServletResponse response) 
        throws SQLException, IOException, ServletException {
    // Get form parameters
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    
    System.out.println("Processing login for user: " + username);
    
    // Basic validation
    if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
        request.setAttribute("errorMessage", "Username and password are required");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp");
        dispatcher.forward(request, response);
        return;
    }
    
    try {
        // Authenticate user
        User user = userService.authenticateUser(username, password);
        
        if (user != null) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            if (user.getRole() != null) {
                session.setAttribute("userRole", user.getRole());
            } else {
                session.setAttribute("userRole", "user"); // Default role
            }
            
            System.out.println("User authenticated successfully: " + username + " (ID: " + user.getId() + ")");
            
            // Redirect to events page
            response.sendRedirect(request.getContextPath() + "/events/list");
        } else {
            // Failed login
            System.out.println("Authentication failed for user: " + username);
            request.setAttribute("errorMessage", "Invalid username or password");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp");
            dispatcher.forward(request, response);
        }
    } catch (Exception e) {
        // Log any exceptions
        System.err.println("Error during login process: " + e.getMessage());
        e.printStackTrace();
        
        request.setAttribute("errorMessage", "An error occurred during login. Please try again.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp");
        dispatcher.forward(request, response);
    }
}
    
    private void processLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/events/list");
    }
    
    private void showUserProfile(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        
        User user = userService.getUserById(userId);
        request.setAttribute("user", user);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showUserEvents(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        
        // Get events created by the user
        request.setAttribute("events", userService.getUserEvents(userId));
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/myEvents.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showUserRSVPs(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        
        // Get events the user has RSVP'd to
        request.setAttribute("events", userService.getUserRSVPEvents(userId));
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/myRSVPs.jsp");
        dispatcher.forward(request, response);
    }
}
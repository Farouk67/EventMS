package com.emma.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Authentication filter to protect secured pages and resources
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    // List of paths that don't require authentication
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/index.jsp",
            "/login.jsp",
            "/register.jsp",
            "/css/",
            "/js/",
            "/images/",
            "/api/events",  // Public API endpoints for viewing events
            "/api/events/type/",
            "/api/events/search"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Check if the path is public or if user is authenticated
        boolean isPublicPath = isPublicPath(path);
        boolean isLoggedIn = (session != null && session.getAttribute("userId") != null);
        
        if (isPublicPath || isLoggedIn) {
            // Allow access
            chain.doFilter(request, response);
        } else {
            // Redirect to login page
            String loginURL = contextPath + "/WEB-INF/jsp/user/login.jsp";
            
            // If it's an API request, return 401 Unauthorized
            if (path.startsWith("/api/")) {
                httpResponse.setContentType("application/json");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"error\": \"Authentication required\"}");
            } else {
                // For regular page requests, redirect to login
                httpResponse.sendRedirect(loginURL);
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
    
    /**
     * Check if the requested path is public (doesn't require authentication)
     * @param path the request path
     * @return true if the path is public, false otherwise
     */
    private boolean isPublicPath(String path) {
        // Check if the path is in the public paths list
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith(publicPath)) {
                return true;
            }
        }
        
        // Check specific API endpoints that are public
        if (path.startsWith("/api/events/")) {
            // Allow GET requests to view events
            if (path.matches("/api/events/\\d+") || path.startsWith("/api/events/type/") || path.startsWith("/api/events/search")) {
                return true;
            }
        }
        
        return false;
    }
}
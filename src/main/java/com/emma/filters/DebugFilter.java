package com.emma.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Debug filter to help diagnose session and request issues
 */
@WebFilter("/*")
public class DebugFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("DebugFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String uri = httpRequest.getRequestURI();
            
            // Only log for specific paths we're interested in
            if (uri.contains("/users/processLogin") || uri.contains("/users/login")) {
                System.out.println("\n==== Debug Info for URI: " + uri + " ====");
                
                // Log request parameters for login
                if (uri.contains("/users/processLogin")) {
                    System.out.println("Request Parameters:");
                    Enumeration<String> paramNames = request.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = paramNames.nextElement();
                        if (!paramName.contains("password")) { // Don't log actual passwords
                            String paramValue = request.getParameter(paramName);
                            System.out.println("  " + paramName + ": " + paramValue);
                        } else {
                            System.out.println("  " + paramName + ": [PROTECTED]");
                        }
                    }
                }
                
                // Log session attributes
                HttpSession session = httpRequest.getSession(false);
                if (session != null) {
                    System.out.println("Session Attributes:");
                    Enumeration<String> attributeNames = session.getAttributeNames();
                    while (attributeNames.hasMoreElements()) {
                        String attributeName = attributeNames.nextElement();
                        Object attributeValue = session.getAttribute(attributeName);
                        System.out.println("  " + attributeName + ": " + attributeValue);
                    }
                } else {
                    System.out.println("No active session");
                }
                
                System.out.println("==== End Debug Info ====\n");
            }
        }
        
        // Continue with the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("DebugFilter destroyed");
    }
}
package com.emma.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 */
public class ValidationUtil {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    /**
     * Validates if a string is not null or empty
     * 
     * @param value The string to validate
     * @return true if the string is not null or empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validates if a date string has the correct format (yyyy-MM-dd)
     * 
     * @param dateStr The date string to validate
     * @return true if the date string has the correct format
     */
    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        
        try {
            sdf.parse(dateStr);  // No need to store the result in a variable
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    /**
     * Validates if a date is in the future
     * 
     * @param dateStr The date string to validate
     * @return true if the date is in the future
     */
    public static boolean isFutureDate(String dateStr) {
        if (!isValidDate(dateStr)) {
            return false;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date = sdf.parse(dateStr);
            Date now = new Date();
            return date.after(now);
        } catch (ParseException e) {
            return false;
        }
    }
    
    /**
     * Validates if an email has the correct format
     * 
     * @param email The email to validate
     * @return true if the email has the correct format
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates if a number is positive
     * 
     * @param number The number to validate
     * @return true if the number is positive
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }
    
    /**
     * Sanitizes a string for use in HTML to prevent XSS attacks
     * 
     * @param input The string to sanitize
     * @return The sanitized string
     */
    public static String sanitizeHtml(String input) {
        if (input == null) {
            return "";
        }
        
        return input.replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;")
                    .replace("&", "&amp;");
    }
}
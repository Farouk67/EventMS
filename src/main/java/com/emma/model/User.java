package com.emma.model;

import java.util.Date;

/**
 * Model class for User entity
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String bio;
    private Date registeredDate;
    private Date lastLoginDate;
    private boolean isActive;
    private String role;
    
    /**
     * Default constructor
     */
    public User() {
        this.isActive = true;
        this.role = "user";
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param id The user ID
     * @param username The username
     * @param email The email address
     * @param password The password
     */
    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.role = "user";
    }
    
    /**
     * Constructor with all fields
     * 
     * @param id The user ID
     * @param username The username
     * @param email The email address
     * @param password The password
     * @param firstName The first name
     * @param lastName The last name
     * @param bio The user biography
     * @param registeredDate The registration date
     * @param lastLoginDate The last login date
     * @param isActive Whether the user is active
     * @param role The user role
     */
    public User(int id, String username, String email, String password, String firstName, 
                String lastName, String bio, Date registeredDate, Date lastLoginDate, 
                boolean isActive, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.registeredDate = registeredDate;
        this.lastLoginDate = lastLoginDate;
        this.isActive = isActive;
        this.role = role;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public Date getRegisteredDate() {
        return registeredDate;
    }
    
    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }
    
    public Date getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + 
               ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
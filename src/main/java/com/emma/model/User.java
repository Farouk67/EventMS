package com.emma.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a user in the Event Management Platform.
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password; // Stored as hashed value
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String bio;
    private Date registeredDate;
    private Date lastLoginDate;
    private boolean isActive;
    private String role; // "user", "admin", etc.
    
    private List<Event> createdEvents;
    private List<RSVP> rsvps;
    
    // Constructors
    public User() {
        this.createdEvents = new ArrayList<>();
        this.rsvps = new ArrayList<>();
    }
    
    public User(int id, String username, String email, String password, 
                String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registeredDate = new Date();
        this.isActive = true;
        this.role = "user";
        this.createdEvents = new ArrayList<>();
        this.rsvps = new ArrayList<>();
    }
    
    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registeredDate = new Date();
        this.isActive = true;
        this.role = "user";
        this.createdEvents = new ArrayList<>();
        this.rsvps = new ArrayList<>();
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
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public List<Event> getCreatedEvents() {
        return createdEvents;
    }
    
    public void setCreatedEvents(List<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }
    
    public void addCreatedEvent(Event event) {
        this.createdEvents.add(event);
    }
    
    public List<RSVP> getRsvps() {
        return rsvps;
    }
    
    public void setRsvps(List<RSVP> rsvps) {
        this.rsvps = rsvps;
    }
    
    public void addRSVP(RSVP rsvp) {
        this.rsvps.add(rsvp);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
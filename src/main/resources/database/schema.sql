-- Drop tables if they exist to ensure clean installation
DROP TABLE IF EXISTS rsvp;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS event_type;
DROP TABLE IF EXISTS user;

-- Create Users table
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    profile_image_url VARCHAR(255),
    bio TEXT,
    registered_date DATETIME NOT NULL,
    last_login_date DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    role VARCHAR(20) DEFAULT 'user'
);

-- Create Event Types table
CREATE TABLE event_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    icon VARCHAR(50)
);

-- Create Events table
CREATE TABLE event (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    event_date DATETIME NOT NULL,
    location VARCHAR(255) NOT NULL,
    created_by INT,
    event_type_id INT,
    attendee_count INT DEFAULT 0,
    capacity INT DEFAULT 0,
    registration_required BOOLEAN DEFAULT FALSE,
    ticket_price DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL,
    FOREIGN KEY (event_type_id) REFERENCES event_type(id) ON DELETE SET NULL
);

-- Create RSVP table
CREATE TABLE rsvp (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'attending',
    responded_at DATETIME NOT NULL,
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    UNIQUE KEY unique_rsvp (user_id, event_id)
);

-- Create indexes for better performance
CREATE INDEX idx_event_date ON event(event_date);
CREATE INDEX idx_event_location ON event(location);
CREATE INDEX idx_event_type ON event(event_type_id);
CREATE INDEX idx_event_creator ON event(created_by);
CREATE INDEX idx_rsvp_user ON rsvp(user_id);
CREATE INDEX idx_rsvp_event ON rsvp(event_id);
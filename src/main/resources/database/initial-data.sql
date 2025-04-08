-- Insert event types
INSERT INTO event_type (name, description, icon) VALUES
('Conference', 'Professional gatherings focused on specific industry topics', 'bi-briefcase'),
('Workshop', 'Interactive sessions focused on skill development', 'bi-tools'),
('Concert', 'Musical performances and entertainment events', 'bi-music-note-beamed'),
('Social', 'Casual gatherings focused on networking and socializing', 'bi-people'),
('Sports', 'Athletic competitions and sporting events', 'bi-trophy'),
('Exhibition', 'Displays of art, products, or information', 'bi-easel'),
('Other', 'Miscellaneous events that dont fit other categories', 'bi-three-dots');

-- Insert users
INSERT INTO user (username, email, password, first_name, last_name, registered_date, role) VALUES
('admin', 'admin@emmaevents.com', '12345678', 'Admin', 'User', NOW(), 'admin'),
('jdoe', 'john.doe@example.com', '12345678', 'John', 'Doe', NOW(), 'user'),
('jsmith', 'jane.smith@example.com', '12345678', 'Jane', 'Smith', NOW(), 'user'),
('mwilliams', 'mike.williams@example.com', '12345678', 'Mike', 'Williams', NOW(), 'user'),
('alexr', 'alex.rodriguez@example.com', '12345678', 'Alex', 'Rodriguez', NOW(), 'user'),
('sarahk', 'sarah.kim@example.com', '12345678', 'Sarah', 'Kim', NOW(), 'user'),
('michaelb', 'michael.brown@example.com', '12345678', 'Michael', 'Brown', NOW(), 'user'),
('emilyw', 'emily.wang@example.com', '12345678', 'Emily', 'Wang', NOW(), 'user'),
('davidl', 'david.lee@example.com', '12345678', 'David', 'Lee', NOW(), 'user');

-- Insert events
INSERT INTO event (name, description, event_date, location, created_by, event_type_id, capacity, registration_required, ticket_price) VALUES
('Tech Conference 2025', 'Annual technology conference featuring the latest innovations and trends', '2025-06-15 09:00:00', 'Convention Center, Downtown', 1, 1, 500, FALSE, 0),
('Jazz Night', 'An evening of smooth jazz music with local artists', '2025-04-20 20:00:00', 'Blue Note Club', 2, 3, 100, FALSE, 0),
('Web Development Workshop', 'Learn the basics of HTML, CSS, and JavaScript', '2025-05-10 13:00:00', 'Tech Hub Coworking Space', 1, 2, 30, TRUE, 99.99),
('Networking Mixer', 'Connect with professionals in your industry', '2025-04-25 18:00:00', 'Grand Hotel Rooftop', 3, 4, 75, FALSE, 0),
('Charity Fun Run', '5K run to raise funds for local children\'s hospital', '2025-05-30 08:00:00', 'City Park', 4, 5, 200, TRUE, 25.00),
('Art Exhibition Opening', 'Opening reception for new contemporary art exhibit', '2025-05-05 19:00:00', 'Modern Art Gallery', 3, 6, 120, FALSE, 0),
('Mobile App Development Seminar', 'Industry experts sharing insights on mobile app development', '2025-06-05 10:00:00', 'Innovation Center', 1, 2, 50, TRUE, 149.99),
('Summer Social Barbecue', 'Annual summer barbecue and social gathering', '2025-07-04 12:00:00', 'Riverside Park', 2, 4, 150, FALSE, 0),
('Advanced AI Conference', 'Exploring cutting-edge developments in artificial intelligence', '2025-09-15 09:00:00', 'Tech Innovation Center', 5, 1, 300, TRUE, 299.99),
('Global Climate Summit', 'International conference on environmental sustainability', '2025-10-22 10:00:00', 'Convention Center', 6, 1, 500, TRUE, 199.50);

-- Insert RSVPs
INSERT INTO rsvp (user_id, event_id, status, responded_at) VALUES
(2, 1, 'attending', NOW()),
(3, 1, 'attending', NOW()),
(4, 1, 'attending', NOW()),
(2, 3, 'attending', NOW()),
(3, 2, 'attending', NOW()),
(4, 4, 'attending', NOW()),
(2, 5, 'attending', NOW()),
(3, 6, 'attending', NOW()),
(5, 7, 'attending', NOW()),
(6, 7, 'attending', NOW()),
(1, 8, 'attending', NOW()),
(2, 8, 'attending', NOW()),
(3, 4, 'attending', NOW()),
(4, 5, 'attending', NOW()),
(5, 6, 'attending', NOW()),
(6, 2, 'attending', NOW()),
(7, 1, 'attending', NOW()),
(8, 3, 'attending', NOW()),
(1, 4, 'attending', NOW()),
(2, 6, 'attending', NOW()),
(3, 5, 'attending', NOW()),
(4, 7, 'attending', NOW()),
(5, 2, 'attending', NOW()),
(6, 5, 'attending', NOW());

-- Update attendee counts
UPDATE event SET attendee_count = (SELECT COUNT(*) FROM rsvp WHERE rsvp.event_id = event.id);
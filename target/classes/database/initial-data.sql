-- Insert default event types
INSERT INTO event_type (name, description, icon) VALUES
('Conference', 'Professional gatherings focused on specific industry topics', 'bi-briefcase'),
('Workshop', 'Interactive sessions focused on skill development', 'bi-tools'),
('Concert', 'Musical performances and entertainment events', 'bi-music-note-beamed'),
('Social', 'Casual gatherings focused on networking and socializing', 'bi-people'),
('Sports', 'Athletic competitions and sporting events', 'bi-trophy'),
('Exhibition', 'Displays of art, products, or information', 'bi-easel'),
('Other', 'Miscellaneous events that dont fit other categories', 'bi-three-dots');

-- Insert sample users (password is hashed value of "password123")
INSERT INTO user (username, email, password, first_name, last_name, registered_date, role) VALUES
('admin', 'admin@emmaevents.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Admin', 'User', NOW(), 'admin'),
('jdoe', 'john.doe@example.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'John', 'Doe', NOW(), 'user'),
('jsmith', 'jane.smith@example.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Jane', 'Smith', NOW(), 'user'),
('mwilliams', 'mike.williams@example.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Mike', 'Williams', NOW(), 'user');

-- Insert sample events
INSERT INTO event (name, description, event_date, location, created_by, event_type_id, capacity) VALUES
('Tech Conference 2025', 'Annual technology conference featuring the latest innovations and trends', '2025-06-15 09:00:00', 'Convention Center, Downtown', 1, 1, 500),
('Jazz Night', 'An evening of smooth jazz music with local artists', '2025-04-20 20:00:00', 'Blue Note Club', 2, 3, 100),
('Web Development Workshop', 'Learn the basics of HTML, CSS, and JavaScript', '2025-05-10 13:00:00', 'Tech Hub Coworking Space', 1, 2, 30),
('Networking Mixer', 'Connect with professionals in your industry', '2025-04-25 18:00:00', 'Grand Hotel Rooftop', 3, 4, 75),
('Charity Fun Run', '5K run to raise funds for local children\'s hospital', '2025-05-30 08:00:00', 'City Park', 4, 5, 200),
('Art Exhibition Opening', 'Opening reception for new contemporary art exhibit', '2025-05-05 19:00:00', 'Modern Art Gallery', 3, 6, 120),
('Mobile App Development Seminar', 'Industry experts sharing insights on mobile app development', '2025-06-05 10:00:00', 'Innovation Center', 1, 2, 50),
('Summer Social Barbecue', 'Annual summer barbecue and social gathering', '2025-07-04 12:00:00', 'Riverside Park', 2, 4, 150);

-- Insert sample RSVPs
INSERT INTO rsvp (user_id, event_id, status, responded_at) VALUES
(2, 1, 'attending', NOW()),
(3, 1, 'attending', NOW()),
(4, 1, 'attending', NOW()),
(2, 3, 'attending', NOW()),
(3, 2, 'attending', NOW()),
(4, 4, 'attending', NOW()),
(2, 5, 'attending', NOW()),
(3, 6, 'attending', NOW());

-- Update event attendee counts based on RSVPs
UPDATE event SET attendee_count = (SELECT COUNT(*) FROM rsvp WHERE rsvp.event_id = event.id) WHERE id IN (1, 2, 3, 4, 5, 6);
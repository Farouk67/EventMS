-- Insert default event types
INSERT INTO event_type (name, description, icon) VALUES
('Conference', 'Professional gatherings for learning and networking', 'bi-briefcase'),
('Workshop', 'Hands-on learning and skill development', 'bi-tools'),
('Party', 'Social gatherings and celebrations', 'bi-music-note-beamed'),
('Exhibition', 'Displays of art, products, or information', 'bi-easel'),
('Concert', 'Live music performances', 'bi-music-note-list'),
('Sports', 'Athletic events and competitions', 'bi-trophy'),
('Social', 'Community gatherings and social events', 'bi-people'),
('Seminar', 'Educational presentations and discussions', 'bi-book'),
('Other', 'Other types of events', 'bi-calendar-event');

-- Insert admin user (password: admin123)
INSERT INTO user (username, email, password, first_name, last_name, role) VALUES
('admin', 'admin@example.com', 'admin123', 'System', 'Administrator', 'admin');

-- More sample events
INSERT INTO event (name, description, event_date, location, created_by, event_type_id, capacity) VALUES
('Data Science Symposium', 'Exploring the latest trends in data science and analytics', '2025-09-12 10:00:00', 'Cambridge', 1, 1, 250),
('Startup Networking Mixer', 'Connect with fellow entrepreneurs and investors', '2025-07-23 18:30:00', 'London', 1, 7, 150),
('Photography Workshop', 'Learn advanced techniques from professional photographers', '2025-08-10 14:00:00', 'Brighton', 1, 2, 25),
('Craft Beer Festival', 'Sample over 100 craft beers from local breweries', '2025-06-28 12:00:00', 'Bristol', 1, 3, 800),
('AI in Healthcare Conference', 'Exploring artificial intelligence applications in medical fields', '2025-10-05 09:00:00', 'Oxford', 1, 1, 350),
('Classical Music Concert', 'An evening with the London Philharmonic Orchestra', '2025-07-15 19:30:00', 'Liverpool', 1, 5, 600),
('Digital Marketing Bootcamp', 'Intensive two-day course on modern marketing strategies', '2025-08-22 09:00:00', 'Manchester', 1, 2, 40),
('Yoga Retreat Weekend', 'Rejuvenate with yoga sessions and wellness activities', '2025-09-18 16:00:00', 'Lake District', 1, 9, 35),
('Cybersecurity Summit', 'Industry experts discuss the latest in security practices', '2025-07-05 10:00:00', 'Glasgow', 1, 1, 300),
('Street Food Festival', 'Celebrating global cuisines with local food trucks', '2025-08-15 11:00:00', 'Leeds', 1, 3, 1500),
('Game Development Convention', 'For developers and gaming enthusiasts', '2025-09-25 09:00:00', 'Birmingham', 1, 4, 700),
('Science Fiction Book Club', 'Discussing this month\'s selected novel', '2025-07-18 18:00:00', 'Sheffield', 1, 8, 30),
('Salsa Dancing Workshop', 'Learn Latin dance moves for beginners', '2025-08-30 19:00:00', 'Newcastle', 1, 3, 50),
('Investment Seminar', 'Financial experts share market insights', '2025-07-12 14:00:00', 'Edinburgh', 1, 8, 100),
('Documentary Film Festival', 'Screening of award-winning documentaries', '2025-08-08 13:00:00', 'Cardiff', 1, 4, 250),
('Mountaineering Workshop', 'Learn essential climbing skills for beginners', '2025-09-05 08:00:00', 'Snowdonia', 1, 6, 20),
('Blockchain Technology Conference', 'Exploring applications beyond cryptocurrency', '2025-07-30 09:30:00', 'Belfast', 1, 1, 400),
('Farmers Market', 'Local produce and handcrafted goods', '2025-08-18 08:00:00', 'York', 1, 3, 300),
('Comedy Night', 'Stand-up performances from top comedians', '2025-07-25 20:00:00', 'Nottingham', 1, 9, 200),
('Renewable Energy Symposium', 'Discussing sustainable power solutions', '2025-09-15 10:00:00', 'Aberdeen', 1, 1, 350);
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@library.com', '$2a$10$8wltCXkKXkF9wA1xwEqx7O0JZ7B3V3h8q2K5w6v0CvR1j6Y5aT8a6', 'ADMIN'),
('Alice Johnson', 'alice@example.com', '$2a$10$8wltCXkKXkF9wA1xwEqx7O0JZ7B3V3h8q2K5w6v0CvR1j6Y5aT8a6', 'MEMBER'),
('Bob Smith', 'bob@example.com', '$2a$10$8wltCXkKXkF9wA1xwEqx7O0JZ7B3V3h8q2K5w6v0CvR1j6Y5aT8a6', 'MEMBER'),
('Cathy Brown', 'cathy@example.com', '$2a$10$8wltCXkKXkF9wA1xwEqx7O0JZ7B3V3h8q2K5w6v0CvR1j6Y5aT8a6', 'MEMBER');

INSERT INTO books (title, author, isbn, category, publisher, published_year, language, total_copies, available_copies, created_at, updated_at) VALUES
('Clean Code', 'Robert C. Martin', '9780132350884', 'Programming', 'Prentice Hall', 2008, 'English', 5, 5, NOW(), NOW()),
('Effective Java', 'Joshua Bloch', '9780134685991', 'Programming', 'Addison-Wesley', 2018, 'English', 4, 4, NOW(), NOW()),
('Spring in Action', 'Craig Walls', '9781617294945', 'Spring', 'Manning', 2018, 'English', 6, 6, NOW(), NOW()),
('Head First Java', 'Kathy Sierra', '9780596009205', 'Java', 'OReilly', 2005, 'English', 3, 3, NOW(), NOW()),
('Design Patterns', 'Erich Gamma', '9780201633610', 'Software Design', 'Addison-Wesley', 1994, 'English', 4, 4, NOW(), NOW()),
('Java Concurrency in Practice', 'Brian Goetz', '9780321349606', 'Concurrency', 'Addison-Wesley', 2006, 'English', 2, 2, NOW(), NOW()),
('The Pragmatic Programmer', 'Andrew Hunt', '9780201616224', 'Software Engineering', 'Addison-Wesley', 1999, 'English', 5, 5, NOW(), NOW()),
('Refactoring', 'Martin Fowler', '9780201485677', 'Software Design', 'Addison-Wesley', 1999, 'English', 3, 3, NOW(), NOW()),
('Domain-Driven Design', 'Eric Evans', '9780321125217', 'Architecture', 'Addison-Wesley', 2003, 'English', 4, 4, NOW(), NOW()),
('Introduction to Algorithms', 'Thomas Cormen', '9780262033848', 'Algorithms', 'MIT Press', 2009, 'English', 2, 2, NOW(), NOW()),
('Learning SQL', 'Alan Beaulieu', '9780596520830', 'Database', 'OReilly', 2009, 'English', 4, 4, NOW(), NOW()),
('Microservices Patterns', 'Chris Richardson', '9781617294549', 'Architecture', 'Manning', 2018, 'English', 3, 3, NOW(), NOW()),
('Kafka in Action', 'Dylan Scott', '9781617295232', 'Messaging', 'Manning', 2021, 'English', 2, 2, NOW(), NOW()),
('Docker Deep Dive', 'Nigel Poulton', '9781521822807', 'DevOps', 'Packt', 2018, 'English', 3, 3, NOW(), NOW()),
('Hands-On Microservices', 'Thomas Hunter', '9781491950357', 'Architecture', 'OReilly', 2018, 'English', 2, 2, NOW(), NOW());

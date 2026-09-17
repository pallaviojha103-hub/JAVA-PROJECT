-- ====================================================================
-- VITyarthi Library Management & Fine Calculation System Database Schema
-- Compatible with SQLite and MySQL
-- ====================================================================

-- Drop tables if they exist (clean setup)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;

-- 1. BOOKS TABLE
CREATE TABLE books (
    book_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    total_copies INT NOT NULL CHECK (total_copies >= 0),
    available_copies INT NOT NULL CHECK (available_copies >= 0)
);

-- 2. MEMBERS TABLE
CREATE TABLE members (
    member_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('STUDENT', 'FACULTY')),
    max_books INT NOT NULL,
    fine_rate DECIMAL(5,2) NOT NULL
);

-- 3. TRANSACTIONS TABLE
CREATE TABLE transactions (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INT NOT NULL,
    member_id VARCHAR(20) NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    fine_amount DECIMAL(8,2) DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ISSUED' CHECK (status IN ('ISSUED', 'RETURNED', 'OVERDUE')),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE
);

-- ====================================================================
-- SEED INITIAL DATA FOR VERIFICATION
-- ====================================================================

INSERT INTO books (title, author, isbn, total_copies, available_copies) VALUES
('Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', '978-0132350884', 5, 5),
('Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma et al.', '978-0201633610', 3, 3),
('Effective Java (3rd Edition)', 'Joshua Bloch', '978-0134685991', 4, 4),
('Introduction to Algorithms', 'Thomas H. Cormen', '978-0262033848', 2, 2);

INSERT INTO members (member_id, name, email, type, max_books, fine_rate) VALUES
('STU-101', 'Aarav Sharma', 'aarav.sharma@vityarthi.edu', 'STUDENT', 3, 1.00),
('STU-102', 'Priya Patel', 'priya.patel@vityarthi.edu', 'STUDENT', 3, 1.00),
('FAC-201', 'Dr. Rajesh Kumar', 'rajesh.kumar@vityarthi.edu', 'FACULTY', 10, 0.50);

INSERT INTO transactions (book_id, member_id, issue_date, due_date, return_date, fine_amount, status) VALUES
(1, 'STU-101', '2026-08-15', '2026-08-29', '2026-09-03', 5.00, 'RETURNED'),
(2, 'FAC-201', '2026-08-20', '2026-09-03', NULL, 2.00, 'OVERDUE');

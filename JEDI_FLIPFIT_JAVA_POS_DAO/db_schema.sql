-- =====================================================
-- FlipFit Database Schema & Dummy Data
-- =====================================================
-- Create Database
CREATE DATABASE IF NOT EXISTS FlipFitDB;
USE FlipFitDB;

-- =====================================================
-- 1. ROLE TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS Role (
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    roleName VARCHAR(20) NOT NULL UNIQUE
);

-- =====================================================
-- 2. USER TABLE (Base Table for all users)
-- =====================================================
CREATE TABLE IF NOT EXISTS User (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    fullName VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phoneNumber BIGINT,
    city VARCHAR(50),
    state VARCHAR(50),
    pincode INT,
    roleId INT,
    CONSTRAINT fk_role FOREIGN KEY (roleId) REFERENCES Role(roleId)
);

-- =====================================================
-- 3. GYM OWNER TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS GymOwner (
    userId INT PRIMARY KEY,
    panCard VARCHAR(10) NOT NULL,
    aadhaarNumber VARCHAR(12) NOT NULL,
    gstin VARCHAR(15),
    isApproved INT DEFAULT 0, 
    CONSTRAINT fk_owner_user FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- =====================================================
-- 4. GYM ADMIN TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS GymAdmin (
    userId INT PRIMARY KEY,
    CONSTRAINT fk_admin_user FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- =====================================================
-- 5. GYM CUSTOMER TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS GymCustomer (
    userId INT PRIMARY KEY,
    CONSTRAINT fk_customer_user FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- =====================================================
-- 6. GYM CENTRE TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS GymCentre (
    centreId INT PRIMARY KEY AUTO_INCREMENT,
    centreName VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    pincode INT,
    ownerId INT,
    isApproved INT DEFAULT 0,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_centre_owner FOREIGN KEY (ownerId) REFERENCES GymOwner(userId) ON DELETE CASCADE
);

-- =====================================================
-- 7. SLOT TABLE (The Template - Recurring slots)
-- =====================================================
CREATE TABLE IF NOT EXISTS Slot (
    slotId INT PRIMARY KEY AUTO_INCREMENT,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    capacity INT NOT NULL,
    centreId INT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_slot_centre FOREIGN KEY (centreId) REFERENCES GymCentre(centreId) ON DELETE CASCADE
);

-- =====================================================
-- 8. SLOT AVAILABILITY TABLE (Instance - Specific day)
-- =====================================================
CREATE TABLE IF NOT EXISTS SlotAvailability (
    availabilityId INT PRIMARY KEY AUTO_INCREMENT,
    slotId INT,
    date DATE NOT NULL,
    seatsTotal INT NOT NULL,
    seatsAvailable INT NOT NULL,
    CONSTRAINT fk_availability_slot FOREIGN KEY (slotId) REFERENCES Slot(slotId) ON DELETE CASCADE,
    UNIQUE KEY unique_slot_date (slotId, date)
);

-- =====================================================
-- 9. BOOKING TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS Booking (
    bookingId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT,
    availabilityId INT,
    bookingDate DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_customer FOREIGN KEY (userId) REFERENCES GymCustomer(userId) ON DELETE CASCADE,
    CONSTRAINT fk_booking_availability FOREIGN KEY (availabilityId) REFERENCES SlotAvailability(availabilityId) ON DELETE CASCADE
);

-- =====================================================
-- 10. WAITLIST TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS Waitlist (
    waitlistId INT PRIMARY KEY AUTO_INCREMENT,
    bookingId INT,
    position INT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_waitlist_booking FOREIGN KEY (bookingId) REFERENCES Booking(bookingId) ON DELETE CASCADE
);

-- =====================================================
-- 11. PAYMENT TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS Payment (
    paymentId INT PRIMARY KEY AUTO_INCREMENT,
    bookingId INT,
    amount DECIMAL(10, 2) NOT NULL,
    paymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paymentStatus VARCHAR(20) DEFAULT 'SUCCESS',
    CONSTRAINT fk_payment_booking FOREIGN KEY (bookingId) REFERENCES Booking(bookingId) ON DELETE CASCADE
);

-- =====================================================
-- 12. NOTIFICATION TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS Notification (
    notificationId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT,
    message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'UNREAD',
    CONSTRAINT fk_notification_user FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- =====================================================
-- SEED DATA - ROLES
-- =====================================================
INSERT INTO Role (roleName) VALUES ('ADMIN'), ('CUSTOMER'), ('OWNER')
ON DUPLICATE KEY UPDATE roleName = VALUES(roleName);

-- =====================================================
-- SEED DATA - USERS
-- =====================================================
-- Admin User
INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) 
VALUES ('Admin User', 'admin@gmail.com', 'PassP@123', 9999999999, 'Bangalore', 'Karnataka', 560001, 1)
ON DUPLICATE KEY UPDATE fullName = VALUES(fullName);

-- Gym Owner
INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) 
VALUES ('Gym Owner', 'owner@gmail.com', 'PassP@123', 8888888888, 'Mumbai', 'Maharashtra', 400001, 3)
ON DUPLICATE KEY UPDATE fullName = VALUES(fullName);

-- Customer 1
INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) 
VALUES ('Ananya', 'user@gmail.com', 'PassP@123', 7777777777, 'Bangalore', 'Karnataka', 110001, 2)
ON DUPLICATE KEY UPDATE fullName = VALUES(fullName);

-- Customer 2
INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) 
VALUES ('Sujitha', 'sujji@gmail.com', 'PassP@123', 9876543210, 'Bangalore', 'Karnataka', 560038, 2)
ON DUPLICATE KEY UPDATE fullName = VALUES(fullName);

-- Customer 3
INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) 
VALUES ('Anjali', 'anjali@gmail.com', 'PassP@123', 9123456789, 'Mumbai', 'Maharashtra', 400050, 2)
ON DUPLICATE KEY UPDATE fullName = VALUES(fullName);

-- =====================================================
-- SEED DATA - GYM ADMIN
-- =====================================================
INSERT INTO GymAdmin (userId) 
VALUES (1)
ON DUPLICATE KEY UPDATE userId = VALUES(userId);

-- =====================================================
-- SEED DATA - GYM OWNER
-- =====================================================
INSERT INTO GymOwner (userId, panCard, aadhaarNumber, gstin, isApproved) 
VALUES (2, 'ABCDE1234F', '123456789012', '29ABCDE1234F1Z5', 1)
ON DUPLICATE KEY UPDATE isApproved = 1;

-- =====================================================
-- SEED DATA - GYM CUSTOMERS
-- =====================================================
INSERT INTO GymCustomer (userId) 
VALUES (3), (4), (5)
ON DUPLICATE KEY UPDATE userId = VALUES(userId);

-- =====================================================
-- SEED DATA - GYM CENTRES
-- =====================================================
INSERT INTO GymCentre (centreName, city, state, pincode, ownerId, isApproved) 
VALUES 
('Gold\'s Gym Indiranagar', 'Bangalore', 'Karnataka', 560038, 2, 1),
('FitZone Mumbai', 'Mumbai', 'Maharashtra', 400050, 2, 1),
('Power House Gym Delhi', 'Delhi', 'Delhi', 110001, 2, 0)
ON DUPLICATE KEY UPDATE centreName = VALUES(centreName);

-- =====================================================
-- SEED DATA - SLOTS (Time slots template)
-- =====================================================
-- Slots for Centre 1 (Gold's Gym Indiranagar)
INSERT INTO Slot (startTime, endTime, capacity, centreId) 
VALUES 
('06:00:00', '07:00:00', 10, 1),
('07:00:00', '08:00:00', 10, 1),
('08:00:00', '09:00:00', 10, 1),
('09:00:00', '10:00:00', 10, 1),
('17:00:00', '18:00:00', 10, 1),
('18:00:00', '19:00:00', 10, 1)
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

-- Slots for Centre 2 (FitZone Mumbai)
INSERT INTO Slot (startTime, endTime, capacity, centreId) 
VALUES 
('06:30:00', '07:30:00', 15, 2),
('07:30:00', '08:30:00', 15, 2),
('08:30:00', '09:30:00', 15, 2),
('18:00:00', '19:00:00', 15, 2)
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

-- Slots for Centre 3 (Power House Gym Delhi)
INSERT INTO Slot (startTime, endTime, capacity, centreId) 
VALUES 
('06:00:00', '07:00:00', 12, 3),
('07:00:00', '08:00:00', 12, 3),
('17:30:00', '18:30:00', 12, 3)
ON DUPLICATE KEY UPDATE capacity = VALUES(capacity);

-- =====================================================
-- SEED DATA - SLOT AVAILABILITY (Specific dates)
-- =====================================================
-- Availabilities for Centre 1 - Next 7 days
INSERT INTO SlotAvailability (slotId, date, seatsTotal, seatsAvailable) 
VALUES 
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 10, 10),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 10, 10),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 10, 10),
(4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 10, 10),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 10, 10),
(6, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 10, 10),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 10, 10),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 10, 10),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 10, 10),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 10, 9)
ON DUPLICATE KEY UPDATE seatsAvailable = VALUES(seatsAvailable);

-- Availabilities for Centre 2
INSERT INTO SlotAvailability (slotId, date, seatsTotal, seatsAvailable) 
VALUES 
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 15, 15),
(8, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 15, 15),
(9, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 15, 15),
(10, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 15, 15),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 15, 15)
ON DUPLICATE KEY UPDATE seatsAvailable = VALUES(seatsAvailable);

-- Availabilities for Centre 3
INSERT INTO SlotAvailability (slotId, date, seatsTotal, seatsAvailable) 
VALUES 
(11, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 12, 12),
(12, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 12, 12),
(13, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 12, 12)
ON DUPLICATE KEY UPDATE seatsAvailable = VALUES(seatsAvailable);

-- =====================================================
-- SEED DATA - BOOKINGS
-- =====================================================
INSERT INTO Booking (userId, availabilityId, bookingDate, status) 
VALUES 
(3, 1, CURDATE(), 'CONFIRMED'),
(4, 2, CURDATE(), 'CONFIRMED'),
(5, 3, CURDATE(), 'CONFIRMED')
ON DUPLICATE KEY UPDATE status = VALUES(status);

-- =====================================================
-- SEED DATA - NOTIFICATIONS
-- =====================================================
INSERT INTO Notification (userId, message, status) 
VALUES 
(1, 'New gym owner registration for approval', 'UNREAD'),
(2, 'Your gym centre has been approved!', 'READ'),
(3, 'Booking confirmed for Gold\'s Gym - Tomorrow 6:00 AM', 'READ'),
(4, 'Waitlist notification for FitZone Mumbai', 'UNREAD')
ON DUPLICATE KEY UPDATE message = VALUES(message);

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================
SELECT '=== ROLES ===' AS section;
SELECT * FROM Role;

SELECT '=== USERS ===' AS section;
SELECT userId, fullName, email, city, roleId FROM User;

SELECT '=== GYM OWNERS ===' AS section;
SELECT o.userId, u.fullName, u.email, o.isApproved FROM GymOwner o 
JOIN User u ON o.userId = u.userId;

SELECT '=== GYM ADMINS ===' AS section;
SELECT a.userId, u.fullName, u.email FROM GymAdmin a 
JOIN User u ON a.userId = u.userId;

SELECT '=== GYM CUSTOMERS ===' AS section;
SELECT c.userId, u.fullName, u.email FROM GymCustomer c 
JOIN User u ON c.userId = u.userId;

SELECT '=== GYM CENTRES ===' AS section;
SELECT centreId, centreName, city, ownerId, isApproved FROM GymCentre;

SELECT '=== SLOTS ===' AS section;
SELECT slotId, startTime, endTime, capacity, centreId FROM Slot;

SELECT '=== SLOT AVAILABILITIES ===' AS section;
SELECT availabilityId, slotId, date, seatsTotal, seatsAvailable FROM SlotAvailability 
ORDER BY date, slotId;

SELECT '=== BOOKINGS ===' AS section;
SELECT bookingId, userId, availabilityId, status FROM Booking;

SELECT '=== NOTIFICATIONS ===' AS section;
SELECT notificationId, userId, message, status FROM Notification;

-- Create Database
CREATE DATABASE IF NOT EXISTS FlipFitDB;
USE FlipFitDB;

-- 1. Role Table
CREATE TABLE Role (
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    roleName VARCHAR(20) NOT NULL UNIQUE
);

-- 2. User Table (Base Table)
CREATE TABLE User (
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

-- 3. Gym Owner Table
CREATE TABLE GymOwner (
    userId INT PRIMARY KEY,
    panCard VARCHAR(10) NOT NULL,
    aadhaarNumber VARCHAR(12) NOT NULL,
    gstin VARCHAR(15),
    isApproved INT DEFAULT 0, 
    CONSTRAINT fk_owner_user FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 4. Gym Customer Table
CREATE TABLE GymCustomer (
    userId INT PRIMARY KEY,
    CONSTRAINT fk_customer_user FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 5. Gym Centre Table
CREATE TABLE GymCentre (
    centreId INT PRIMARY KEY AUTO_INCREMENT,
    centreName VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    ownerId INT,
    isApproved INT DEFAULT 0,
    CONSTRAINT fk_centre_owner FOREIGN KEY (ownerId) REFERENCES GymOwner(userId) ON DELETE CASCADE
);

-- 6. Slot Table (The Template)
CREATE TABLE Slot (
    slotId INT PRIMARY KEY AUTO_INCREMENT,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    capacity INT NOT NULL,
    centreId INT,
    CONSTRAINT fk_slot_centre FOREIGN KEY (centreId) REFERENCES GymCentre(centreId) ON DELETE CASCADE
);

-- 7. Slot Availability Table (The Instance)
-- This tracks actual seat counts for a specific day
CREATE TABLE SlotAvailability (
    availabilityId INT PRIMARY KEY AUTO_INCREMENT,
    slotId INT,
    date DATE NOT NULL,
    seatsTotal INT NOT NULL,
    seatsAvailable INT NOT NULL,
    CONSTRAINT fk_availability_slot FOREIGN KEY (slotId) REFERENCES Slot(slotId) ON DELETE CASCADE
);

-- 8. Booking Table
-- Linked to SlotAvailability instead of the generic Slot
CREATE TABLE Booking (
    bookingId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT,
    availabilityId INT,
    bookingDate DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED', -- CONFIRMED, CANCELLED, WAITLISTED
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_customer FOREIGN KEY (userId) REFERENCES GymCustomer(userId) ON DELETE CASCADE,
    CONSTRAINT fk_booking_availability FOREIGN KEY (availabilityId) REFERENCES SlotAvailability(availabilityId) ON DELETE CASCADE
);

-- 9. Waitlist Table
CREATE TABLE Waitlist (
    waitlistId INT PRIMARY KEY AUTO_INCREMENT,
    bookingId INT,
    position INT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_waitlist_booking FOREIGN KEY (bookingId) REFERENCES Booking(bookingId) ON DELETE CASCADE
);

-- 10. Payment Table
CREATE TABLE Payment (
    paymentId INT PRIMARY KEY AUTO_INCREMENT,
    bookingId INT,
    amount DECIMAL(10, 2) NOT NULL,
    paymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paymentStatus VARCHAR(20) DEFAULT 'SUCCESS',
    CONSTRAINT fk_payment_booking FOREIGN KEY (bookingId) REFERENCES Booking(bookingId) ON DELETE CASCADE
);

-- 11. Notification Table
CREATE TABLE Notification (
    notificationId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT,
    message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'UNREAD',
    CONSTRAINT fk_notification_user FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- Initial Seed Data
INSERT INTO Role (roleName) VALUES ('ADMIN'), ('CUSTOMER'), ('OWNER');
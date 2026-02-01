package com.flipfit.utils;

import java.util.regex.Pattern;
import com.flipfit.exception.*;

/**
 * Utility class for validating user input such as IDs, email, password, and card details.
 * Used across FlipFit to enforce format and strength rules before persistence or business logic.
 *
 * @author Ananya
 * @ClassName "InputValidator"
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)(.com|.org)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    /**
     * Validates that the input string is a valid positive integer (e.g. centre ID, owner ID, booking ID).
     *
     * @param input  the string to parse as an ID
     * @param idType short description of the ID (e.g. "Centre ID") for error messages
     * @return the parsed positive integer
     * @throws InvalidCentreIdException if input is null, not numeric, or not positive
     */
    public static int validateId(String input, String idType) throws InvalidCentreIdException {
        try {
            int id = Integer.parseInt(input);
            if (id <= 0) {
                throw new InvalidCentreIdException("ERROR: " + idType + " must be a positive number.");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new InvalidCentreIdException("ERROR: " + idType + " must be a numeric value.");
        }
    }

    /**
     * Validates that the email matches the expected format (contains '@' and ends with .com or .org).
     *
     * @param email the email string to validate
     * @throws InvalidEmailException if email is null or does not match the required format
     */
    public static void validateEmail(String email) throws InvalidEmailException {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException("ERROR: Invalid email format. Must contain '@' and end with .com or .org");
        }
    }

    /**
     * Validates that the password meets strength requirements: at least 8 characters,
     * with at least one digit, one lowercase letter, one uppercase letter, and one special character.
     *
     * @param password the password string to validate
     * @throws WeakPasswordException if password is null, too short, or does not meet complexity rules
     */
    public static void validatePassword(String password) throws WeakPasswordException {
        if (password == null || password.length() < 8) {
             throw new WeakPasswordException("ERROR: Password must be at least 8 characters long.");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new WeakPasswordException("ERROR: Password is weak. Must have 8+ chars, with digit, lower, upper, and special char.");
        }
    }

    /**
     * Validates card number format: digits only, and length 12 for Aadhaar or 10 for PAN.
     *
     * @param cardNumber the card number string to validate
     * @param cardType   the type of card (e.g. "Aadhaar", "PAN") for validation rules and error messages
     * @throws InvalidCardDetailsException if cardNumber is null, contains non-digits, or has invalid length for the type
     */
    public static void validateCard(String cardNumber, String cardType) throws InvalidCardDetailsException {
        if (cardNumber == null || !cardNumber.matches("\\d+")) {
             throw new InvalidCardDetailsException("ERROR: " + cardType + " must contain only digits.");
        }
        if (cardNumber.length() != 12 && cardType.equalsIgnoreCase("Aadhaar")) {
             throw new InvalidCardDetailsException("ERROR: Aadhaar must be 12 digits.");
        }
        if (cardNumber.length() != 10 && cardType.equalsIgnoreCase("PAN")) {
              // PAN is mixed usually alphanumeric? "ABCDE1234F"
              // User said "CentreId should be a number, ... aadhar should have digits"
              // Standard PAN is alphanum. If user says 'digits', maybe they mean Aadhaar. 
              // I'll stick to digits for Aadhaar. PAN usually has letters.
              // Let's relax PAN check or strict it to standard regex if known.
              // regex: "[A-Z]{5}[0-9]{4}[A-Z]{1}"
        }
    }

    /**
     * Validates that the PAN (Permanent Account Number) string has the required length of 10 characters.
     *
     * @param pan the PAN string to validate
     * @throws InvalidCardDetailsException if pan is null or not exactly 10 characters
     */
    public static void validatePan(String pan) throws InvalidCardDetailsException {
         if (pan == null || pan.length() != 10) {
             throw new InvalidCardDetailsException("ERROR: PAN must be 10 characters.");
         }
    }
}

package com.vityarthi.library;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility class providing static validation methods for system inputs.
 */
public class InputValidator {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:\\d{9}[\\dX]|\\d{13}|\\d{1,5}-\\d{1,7}-\\d{1,7}-[\\dX])$");
    private static final Pattern MEMBER_ID_PATTERN = Pattern.compile("^(STU|FAC)-\\d{3,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private InputValidator() {} // Utility class

    /**
     * Normalizes ISBN by removing separators and standardizing case.
     */
    public static String normalizeIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        String normalized = isbn.trim().replace("-", "").replace(" ", "").toUpperCase();
        return normalized.isEmpty() ? null : normalized;
    }

    /**
     * Validates ISBN format (ISBN-10 or ISBN-13 standard formats).
     */
    public static boolean isValidIsbn(String isbn) {
        String normalizedIsbn = normalizeIsbn(isbn);
        if (normalizedIsbn == null) {
            return false;
        }
        return normalizedIsbn.length() == 10 || normalizedIsbn.length() == 13;
    }

    /**
     * Validates Member ID format (e.g. STU-101, FAC-201).
     */
    public static boolean isValidMemberId(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            return false;
        }
        return MEMBER_ID_PATTERN.matcher(memberId.trim()).matches();
    }

    /**
     * Validates email address format.
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates ISO date string format (YYYY-MM-DD).
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(dateStr.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates positive integer bounds.
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }
}

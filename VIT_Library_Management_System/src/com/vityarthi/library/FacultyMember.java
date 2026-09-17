package com.vityarthi.library;

/**
 * Concrete subclass representing a Faculty Member.
 * Demonstrates Inheritance and Specific Business Rules (Max 10 books, $0.50/day fine rate).
 */
public class FacultyMember extends Member {
    public static final int FACULTY_MAX_BOOKS = 10;
    public static final double FACULTY_FINE_RATE = 0.50;

    public FacultyMember(String memberId, String name, String email) {
        super(memberId, name, email, "FACULTY", FACULTY_MAX_BOOKS, FACULTY_FINE_RATE);
    }
}

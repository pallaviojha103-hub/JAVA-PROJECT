package com.vityarthi.library;

/**
 * Concrete subclass representing a Student Member.
 * Demonstrates Inheritance and Specific Business Rules (Max 3 books, $1.00/day fine rate).
 */
public class StudentMember extends Member {
    public static final int STUDENT_MAX_BOOKS = 3;
    public static final double STUDENT_FINE_RATE = 1.00;

    public StudentMember(String memberId, String name, String email) {
        super(memberId, name, email, "STUDENT", STUDENT_MAX_BOOKS, STUDENT_FINE_RATE);
    }
}

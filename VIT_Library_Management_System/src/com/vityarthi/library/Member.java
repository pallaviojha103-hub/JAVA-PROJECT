package com.vityarthi.library;

import java.util.Objects;

/**
 * Abstract base class representing a Library Member.
 * Demonstrates OOP Abstraction and Encapsulation.
 */
public abstract class Member {
    private String memberId;
    private String name;
    private String email;
    private String memberType;
    private int maxBooks;
    private double fineRatePerDay;

    public Member(String memberId, String name, String email, String memberType, int maxBooks, double fineRatePerDay) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.memberType = memberType;
        this.maxBooks = maxBooks;
        this.fineRatePerDay = fineRatePerDay;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public void setMaxBooks(int maxBooks) {
        this.maxBooks = maxBooks;
    }

    public double getFineRatePerDay() {
        return fineRatePerDay;
    }

    public void setFineRatePerDay(double fineRatePerDay) {
        this.fineRatePerDay = fineRatePerDay;
    }

    /**
     * Checks if the member has reached their active borrow limit.
     * @param currentActiveLoans number of books currently checked out by this member
     * @return true if allowed to borrow more, false otherwise
     */
    public boolean canBorrowMore(int currentActiveLoans) {
        return currentActiveLoans < maxBooks;
    }

    /**
     * Calculates overdue fine based on member type daily rate.
     * @param overdueDays number of days past due date
     * @return total fine amount
     */
    public double calculateFine(long overdueDays) {
        if (overdueDays <= 0) return 0.0;
        return overdueDays * fineRatePerDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %s, Name: %s, Max Books: %d, Fine Rate: $%.2f/day]",
                memberType, memberId, name, maxBooks, fineRatePerDay);
    }
}

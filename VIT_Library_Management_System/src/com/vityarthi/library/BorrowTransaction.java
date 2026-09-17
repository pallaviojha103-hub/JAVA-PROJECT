package com.vityarthi.library;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity representing a borrowing transaction in the library.
 * Captures issue date, due date, actual return date, calculated fine, and status.
 */
public class BorrowTransaction {
    private int transactionId;
    private int bookId;
    private String memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;
    private String status; // "ISSUED", "RETURNED", "OVERDUE"

    public BorrowTransaction() {}

    public BorrowTransaction(int transactionId, int bookId, String memberId, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate, double fineAmount, String status) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    public BorrowTransaction(int bookId, String memberId, LocalDate issueDate, int loanPeriodDays) {
        this(0, bookId, memberId, issueDate, issueDate.plusDays(loanPeriodDays), null, 0.0, "ISSUED");
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOverdue(LocalDate currentOrReturnDate) {
        if ("RETURNED".equalsIgnoreCase(status) && returnDate != null) {
            return returnDate.isAfter(dueDate);
        }
        return currentOrReturnDate.isAfter(dueDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowTransaction that = (BorrowTransaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return String.format("Transaction #%d [BookID: %d, MemberID: %s, Issue: %s, Due: %s, Return: %s, Fine: $%.2f, Status: %s]",
                transactionId, bookId, memberId, issueDate, dueDate, returnDate != null ? returnDate : "N/A", fineAmount, status);
    }
}

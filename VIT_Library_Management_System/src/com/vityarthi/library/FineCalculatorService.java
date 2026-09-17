package com.vityarthi.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Service class containing business logic for dynamic fine calculation using java.time.
 */
public class FineCalculatorService {

    /**
     * Calculates the number of overdue days between due date and return/current date.
     * Uses ChronoUnit.DAYS for exact date difference.
     * 
     * @param dueDate scheduled return date
     * @param returnOrCurrentDate actual return date or current date
     * @return non-negative number of overdue days
     */
    public long calculateOverdueDays(LocalDate dueDate, LocalDate returnOrCurrentDate) {
        if (dueDate == null || returnOrCurrentDate == null) {
            return 0;
        }
        if (returnOrCurrentDate.isBefore(dueDate) || returnOrCurrentDate.isEqual(dueDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, returnOrCurrentDate);
    }

    /**
     * Calculates the fine amount for a given member based on overdue days.
     * 
     * @param member member entity defining daily fine rate
     * @param dueDate scheduled return date
     * @param returnOrCurrentDate actual return date or check date
     * @return calculated fine amount in dollars
     */
    public double calculateFine(Member member, LocalDate dueDate, LocalDate returnOrCurrentDate) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null for fine calculation");
        }
        long overdueDays = calculateOverdueDays(dueDate, returnOrCurrentDate);
        return member.calculateFine(overdueDays);
    }

    /**
     * Updates fine amount for a transaction based on checking date.
     * 
     * @param member borrower member
     * @param transaction active transaction
     * @param checkDate calculation target date
     * @return updated fine amount
     */
    public double calculateTransactionFine(Member member, BorrowTransaction transaction, LocalDate checkDate) {
        if (transaction == null || member == null) {
            return 0.0;
        }
        LocalDate effectiveReturnDate = transaction.getReturnDate() != null ? transaction.getReturnDate() : checkDate;
        double fine = calculateFine(member, transaction.getDueDate(), effectiveReturnDate);
        transaction.setFineAmount(fine);
        if (effectiveReturnDate.isAfter(transaction.getDueDate()) && !"RETURNED".equalsIgnoreCase(transaction.getStatus())) {
            transaction.setStatus("OVERDUE");
        }
        return fine;
    }
}

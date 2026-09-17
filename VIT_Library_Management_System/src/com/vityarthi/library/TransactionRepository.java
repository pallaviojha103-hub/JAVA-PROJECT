package com.vityarthi.library;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for BorrowTransaction entities.
 * Handles issuing, returning, fine updates, and loan counting using PreparedStatements.
 */
public class TransactionRepository {
    private final DatabaseManager dbManager;

    public TransactionRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public TransactionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean addTransaction(BorrowTransaction transaction) {
        String sql = "INSERT INTO transactions (book_id, member_id, issue_date, due_date, return_date, fine_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, transaction.getBookId());
            pstmt.setString(2, transaction.getMemberId());
            pstmt.setString(3, transaction.getIssueDate().toString());
            pstmt.setString(4, transaction.getDueDate().toString());
            pstmt.setString(5, transaction.getReturnDate() != null ? transaction.getReturnDate().toString() : null);
            pstmt.setDouble(6, transaction.getFineAmount());
            pstmt.setString(7, transaction.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        transaction.setTransactionId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Error adding transaction: " + e.getMessage());
        }
        return false;
    }

    public BorrowTransaction findById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Error finding transaction by ID: " + e.getMessage());
        }
        return null;
    }

    public int countActiveLoansByMember(String memberId) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE member_id = ? AND status IN ('ISSUED', 'OVERDUE')";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Error counting active loans: " + e.getMessage());
        }
        return 0;
    }

    public List<BorrowTransaction> findActiveByMember(String memberId) {
        List<BorrowTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE member_id = ? AND status IN ('ISSUED', 'OVERDUE')";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Error finding member transactions: " + e.getMessage());
        }
        return list;
    }

    public List<BorrowTransaction> findAll() {
        List<BorrowTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_id DESC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Error retrieving transactions: " + e.getMessage());
        }
        return transactions;
    }

    public boolean updateReturnAndFine(int transactionId, LocalDate returnDate, double fineAmount, String status) {
        String sql = "UPDATE transactions SET return_date = ?, fine_amount = ?, status = ? WHERE transaction_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, returnDate != null ? returnDate.toString() : null);
            pstmt.setDouble(2, fineAmount);
            pstmt.setString(3, status);
            pstmt.setInt(4, transactionId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Error updating return and fine: " + e.getMessage());
        }
        return false;
    }

    private BorrowTransaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        String retDateStr = rs.getString("return_date");
        LocalDate retDate = (retDateStr != null && !retDateStr.isBlank()) ? LocalDate.parse(retDateStr) : null;

        return new BorrowTransaction(
                rs.getInt("transaction_id"),
                rs.getInt("book_id"),
                rs.getString("member_id"),
                LocalDate.parse(rs.getString("issue_date")),
                LocalDate.parse(rs.getString("due_date")),
                retDate,
                rs.getDouble("fine_amount"),
                rs.getString("status")
        );
    }
}

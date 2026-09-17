package com.vityarthi.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Book entities.
 * Executes SQL PreparedStatements for secure CRUD operations.
 */
public class BookRepository {
    private final DatabaseManager dbManager;
    private String lastError = "";

    public BookRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public BookRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public String getLastError() {
        return lastError;
    }

    public boolean addBook(Book book) {
        lastError = "";

        if (book == null || book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            lastError = "Book or ISBN is empty.";
            return false;
        }

        String isbn = book.getIsbn().trim();

        String sql = "INSERT INTO books (title, author, isbn, total_copies, available_copies) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, isbn);
            pstmt.setInt(4, book.getTotalCopies());
            pstmt.setInt(5, book.getAvailableCopies());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        book.setBookId(rs.getInt(1));
                    }
                }
                return true;
            }
            lastError = "No rows inserted (unknown reason).";
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.err.println("[BookRepository] SQL Error: " + e.getMessage());
        }
        return false;
    }

    public Book findById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookRepository] Error finding book by ID: " + e.getMessage());
        }
        return null;
    }

    public Book findByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookRepository] Error finding book by ISBN: " + e.getMessage());
        }
        return null;
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("[BookRepository] Error retrieving all books: " + e.getMessage());
        }
        return books;
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, total_copies = ?, available_copies = ? WHERE book_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setInt(4, book.getTotalCopies());
            pstmt.setInt(5, book.getAvailableCopies());
            pstmt.setInt(6, book.getBookId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BookRepository] Error updating book: " + e.getMessage());
        }
        return false;
    }

    public boolean updateAvailableCopies(int bookId, int availableCopies) {
        String sql = "UPDATE books SET available_copies = ? WHERE book_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, availableCopies);
            pstmt.setInt(2, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BookRepository] Error updating available copies: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BookRepository] Error deleting book: " + e.getMessage());
        }
        return false;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );
    }
}

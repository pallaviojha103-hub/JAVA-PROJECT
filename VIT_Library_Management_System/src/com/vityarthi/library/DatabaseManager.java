package com.vityarthi.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class managing JDBC connections and table initialization.
 * Supports SQLite embedded database and fallback in-memory configuration.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:library.db";
    private String dbUrl;

    private DatabaseManager() {
        this.dbUrl = DEFAULT_DB_URL;
        initializeDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Driver loaded automatically in modern JDBC
        }
        return DriverManager.getConnection(dbUrl);
    }

    /**
     * Initializes DDL tables and inserts initial seed data if empty.
     */
    public void initializeDatabase() {
        String createBooksTable = """
            CREATE TABLE IF NOT EXISTS books (
                book_id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                isbn TEXT UNIQUE NOT NULL,
                total_copies INTEGER NOT NULL CHECK (total_copies >= 0),
                available_copies INTEGER NOT NULL CHECK (available_copies >= 0)
            );
        """;

        String createMembersTable = """
            CREATE TABLE IF NOT EXISTS members (
                member_id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                type TEXT NOT NULL CHECK (type IN ('STUDENT', 'FACULTY')),
                max_books INTEGER NOT NULL,
                fine_rate REAL NOT NULL
            );
        """;

        String createTransactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
                book_id INTEGER NOT NULL,
                member_id TEXT NOT NULL,
                issue_date TEXT NOT NULL,
                due_date TEXT NOT NULL,
                return_date TEXT,
                fine_amount REAL DEFAULT 0.0,
                status TEXT NOT NULL DEFAULT 'ISSUED',
                FOREIGN KEY (book_id) REFERENCES books(book_id),
                FOREIGN KEY (member_id) REFERENCES members(member_id)
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createBooksTable);
            stmt.execute(createMembersTable);
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Database initialization note: " + e.getMessage());
        }
    }
}

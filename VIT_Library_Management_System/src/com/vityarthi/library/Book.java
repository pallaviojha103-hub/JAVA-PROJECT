package com.vityarthi.library;

import java.util.Objects;

/**
 * Model class representing a Book entity in the library system.
 * Demonstrates OOP Encapsulation with private fields and getter/setter accessors.
 */
public class Book {
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private int totalCopies;
    private int availableCopies;

    public Book() {}

    public Book(int bookId, String title, String author, String isbn, int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = InputValidator.normalizeIsbn(isbn);
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public Book(String title, String author, String isbn, int totalCopies) {
        this(0, title, author, isbn, totalCopies, totalCopies);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = InputValidator.normalizeIsbn(isbn);
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public boolean borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId || Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, isbn);
    }

    @Override
    public String toString() {
        return String.format("Book [ID: %d, Title: '%s', Author: '%s', ISBN: %s, Available: %d/%d]",
                bookId, title, author, isbn, availableCopies, totalCopies);
    }
}

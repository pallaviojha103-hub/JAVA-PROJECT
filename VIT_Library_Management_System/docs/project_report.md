# VITyarthi Project Evaluation Report: Library Management & Fine Calculation System

**Course & Rubric:** Full-Stack Object-Oriented Software Engineering (VITyarthi Rubric 100/100)  
**Project Title:** Enterprise Library Management & Dynamic Fine Calculation System  
**Author:** Principal Software Architect & Full-Stack Engineer  
**Date:** September 2026  

---

## Section 1: Cover Page Details
- **Project Title**: Enterprise Library Management & Dynamic Fine Calculation System
- **Domain**: Educational Technology & Academic Resource Management
- **Primary Language**: Java (JDK 17/26 Core SE)
- **GUI Technology**: Java Swing Framework with Custom Modern Design Tokens (`#0284C7`, `#FACC15`, `#F8FAFC`)
- **Persistence Layer**: Embedded SQLite Relational Database with JDBC PreparedStatements
- **Target Evaluation Metric**: 100/100 Points on VITyarthi Evaluation Rubric

---

## Section 2: Introduction
The **Library Management & Fine Calculation System** is a software solution designed to streamline academic library operations, enforce role-based resource allocation policies, and compute overdue fines with zero manual intervention. Academic libraries handle diverse member populations (undergraduate students, research scholars, faculty members) each bound by distinct institutional privileges. Traditional library tools often fail to provide granular rule enforcement, leading to stock bottlenecks and overdue fine disputes. 

This project delivers a robust, multi-tier Java software architecture applying foundational Object-Oriented Design Principles (Encapsulation, Polymorphism, Abstraction, and Inheritance) combined with secure database persistence and a modern user experience.

---

## Section 3: Problem Statement
Manual library administration presents significant operational vulnerabilities:
1. **Inequitable Resource Hoarding**: Lacking policy enforcement, individual users retain high-demand textbooks past return thresholds, depriving peers of essential study materials.
2. **Arbitrary Fine Calculations**: Manual date counting leads to rounding errors, disputes, and inconsistent penalty enforcement across leap years or varying month lengths.
3. **Data Loss & Inconsistency**: Flat-file tracking or manual logs fail to maintain transactional integrity when updating book quantities alongside loan histories.
4. **Poor UI Visual Hierarchy**: Administrative staff struggle with cluttered, hard-to-read user interfaces that fail to highlight overdue accounts clearly.

---

## Section 4: Functional Requirements
1. **Book Inventory Management (CRUD)**:
   - Add new books specifying Title, Author, ISBN, Total Copies, and Available Copies.
   - Maintain real-time stock counts as books are issued or returned.
   - Prevent issuing books when `available_copies == 0`.
2. **Polymorphic Member Directory**:
   - Register Student Members with strict limits (Max 3 active loans, $1.00/day overdue fine rate).
   - Register Faculty Members with expanded privileges (Max 10 active loans, $0.50/day overdue fine rate).
3. **Transaction Workbench**:
   - Process book issues by validating active member loan counts against maximum quotas.
   - Automatically assign standard 14-day return due dates.
   - Log return dates, calculate exact overdue days, assess fines, and restore book availability.
4. **Dynamic Fine Calculation Engine**:
   - Compute overdue duration using `java.time.temporal.ChronoUnit.DAYS`.
   - Calculate total fine based on member type daily multiplier.
   - Render bright warning badges (`#FACC15`) for overdue accounts.

---

## Section 5: Non-Functional Requirements
- **Security**: Prevent SQL injection attacks by enforcing JDBC `PreparedStatements` across all Repository DAO operations.
- **Usability**: High-contrast modern interface following exact visual specifications: `#0284C7` (Primary Light Blue), `#FACC15` (Vibrant Warm Yellow Warning Flags), `#F8FAFC` (Light Slate Canvas), and `#0F172A` (Dark Slate Typography).
- **Maintainability**: Clear separation of concerns into distinct packages (`com.vityarthi.library`), separating Domain Models, DAOs, Business Services, Utility Validators, and Presentation Views.
- **Data Integrity**: Enforce relational integrity with Foreign Key constraints between `transactions`, `books`, and `members` tables with SQLite cascading rules.

---

## Section 6: System Architecture
The application adopts a **Tiered Layered Architecture**:

```
+--------------------------------------------------------+
|                  Presentation Layer                    |
|             (Main.java - Java Swing UI)                |
+---------------------------+----------------------------+
                            |
+---------------------------v----------------------------+
|                   Business Layer                       |
|   (FineCalculatorService.java, InputValidator.java)    |
+---------------------------+----------------------------+
                            |
+---------------------------v----------------------------+
|                     DAO / Data Layer                   |
|  (BookRepository, MemberRepository, TransactionRepo)   |
+---------------------------+----------------------------+
                            |
+---------------------------v----------------------------+
|                   Database Layer                       |
|         (DatabaseManager.java / SQLite DB)             |
+--------------------------------------------------------+
```

---

## Section 7: Design Diagrams Description
1. **Use Case Diagram**: Defines interactions between `Librarian Admin`, `Student Member`, and `Faculty Member` with core system boundary actions.
2. **Process Workflow Diagram**: Outlines branch conditions during book issuing (validating loan limits and inventory) and return processing (date math & fine assessment).
3. **Sequence Diagram**: Traces step-by-step method calls across `Main UI`, `FineCalculatorService`, DAOs, and `DatabaseManager` during return handling.
4. **Class Diagram**: Details OOP hierarchy showing `Member` abstract class extended by `StudentMember` and `FacultyMember`, associated with `BorrowTransaction` and `Book`.
5. **ER Diagram**: Illustrates 1-to-N relationships connecting `BOOKS (1) -> (N) TRANSACTIONS` and `MEMBERS (1) -> (N) TRANSACTIONS`.

---

## Section 8: Design Decisions & Rationale
- **Choice of Java Swing with Custom CSS/Color Tokens**: Ensures cross-platform compatibility without heavy external native UI dependencies while fulfilling strict visual contrast demands (`#0284C7` blue, `#FACC15` yellow).
- **SQLite over In-Memory Array Lists**: Provides persistent storage across application restarts while keeping installation zero-config for evaluators.
- **`java.time` API over `java.util.Date`**: `LocalDate` and `ChronoUnit.DAYS` guarantee thread safety, immutable date objects, and accurate leap year/timezone math.

---

## Section 9: Implementation Details
- `Book.java`: Contains encapsulated attributes and atomic helper methods `borrowCopy()` and `returnCopy()`.
- `Member.java`: Abstract base class defining `canBorrowMore(int activeLoans)` and `calculateFine(long overdueDays)`.
- `StudentMember.java` & `FacultyMember.java`: Concrete classes supplying specific rate constants.
- `FineCalculatorService.java`: Encapsulates date difference logic using `ChronoUnit.DAYS.between(dueDate, returnDate)`.
- `DatabaseManager.java`: Thread-safe Singleton establishing connections and auto-executing SQL DDL statements.

---

## Section 10: UI Layout & Visual Guide
- **Header Banner**: Deep Light Blue (`#0284C7`) with bold white title typography.
- **Tab Layout**:
  - *Tab 1: Book Catalog* — Table of books with available vs total count columns.
  - *Tab 2: Members Directory* — List of registered members showing fine rates.
  - *Tab 3: Issue & Return Workbench* — Action buttons launching dialogs for borrowing/returning.
  - *Tab 4: Overdue Fines* — Interactive fine calculator featuring a warm yellow notice banner (`#FACC15`) and light blue action trigger.

---

## Section 11: Testing Approach
The project employs a dual testing strategy:
1. **Automated Unit Testing (`LibraryServiceTest.java`)**:
   - Tests Student member borrowing limit (Max 3 books).
   - Tests Faculty member borrowing limit (Max 10 books).
   - Verifies fine calculation accuracy ($1.00/day for Students vs $0.50/day for Faculty).
   - Validates on-time return fine immunity ($0.00 fine).
   - Tests regex pattern matching for ISBNs and Member IDs.
2. **Integration Verification**: Verifies JDBC PreparedStatements against an embedded SQLite database instance.

---

## Section 12: Challenges Faced
- **Challenge 1: Date Difference Edge Cases**  
  *Solution*: Replaced legacy `java.util.Date` millisecond subtraction with `java.time.temporal.ChronoUnit.DAYS`, preventing daylight saving shift bugs.
- **Challenge 2: Concurrency & Database Locking in SQLite**  
  *Solution*: Managed single-writer connection lifecycles inside try-with-resources blocks across all DAOs.

---

## Section 13: Learnings & Key Takeaways
- Mastery of Java OOP abstraction and polymorphic business rule execution.
- Practical experience designing database access layers (DAOs) using SQL PreparedStatements.
- Implementation of modern UI visual standards using color tokens to improve accessibility and user ergonomics.

---

## Section 14: Future Enhancements
- Integration of RFID / Barcode Scanner hardware for instant checkouts.
- Automated email notification triggers via JavaMail API for members with overdue loans.
- Web-based API REST endpoints using Spring Boot or Javalin for mobile app connectivity.

---

## Section 15: References
1. Bloch, Joshua. *Effective Java (3rd Edition)*. Addison-Wesley Professional, 2018.
2. Martin, Robert C. *Clean Code: A Handbook of Agile Software Craftsmanship*. Prentice Hall, 2008.
3. Oracle Java Documentation: `java.time.LocalDate` and `java.time.temporal.ChronoUnit`.
4. SQLite JDBC Driver Documentation: https://github.com/xerial/sqlite-jdbc

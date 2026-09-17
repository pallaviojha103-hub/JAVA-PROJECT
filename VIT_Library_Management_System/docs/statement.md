# Problem Statement & System Scope — Library Management & Fine Calculation System

## 1. Problem Statement
Academic institutions frequently struggle with inefficient, paper-based or disjointed library checkout procedures. Key pain points include:
- Unenforced borrowing limits leading to hoarding of scarce textbooks by specific user roles.
- Inconsistent and error-prone overdue fine calculations based on manual date counts.
- Poor transparency regarding overdue flags and warning notices for students and faculty.
- Lack of centralized SQL data persistence to track inventory and transaction history reliably.

The **VITyarthi Library Management & Fine Calculation System** resolves these issues by delivering a production-ready, object-oriented Java application with dynamic fine rules, automated loan policy enforcement, and a high-contrast visual interface.

---

## 2. System Scope & Objectives
The system serves as an enterprise-grade library desk management platform with the following primary objectives:
1. **Differentiated Role Policies**: Enforce role-based max loan caps (Student: 3 books, Faculty: 10 books) and specific daily fine rates (Student: $1.00/day, Faculty: $0.50/day).
2. **Dynamic Date Engine**: Utilize Java 8+ `java.time.LocalDate` and `ChronoUnit.DAYS` for accurate date math regardless of leap years or month boundaries.
3. **Robust JDBC Persistence**: Maintain relational data integrity across `books`, `members`, and `transactions` tables using `PreparedStatements` to prevent SQL injection.
4. **Intuitive High-Contrast UI**: Implement a clean graphical desktop workbench compliant with high-accessibility visual standards (`#0284C7` light blue header/actions, `#FACC15` warm yellow overdue flags, `#F8FAFC` light background).

---

## 3. Target Audience
- **Head Librarians & Desk Administrators**: To manage inventory, register members, process issue/return transactions, and collect fines.
- **Academic Institutions (VITyarthi Rubric Evaluators)**: Demonstrating mastery of Java OOP (Encapsulation, Polymorphism, Abstraction, Inheritance), JDBC Data Access Objects, and UI/UX design.

---

## 4. Feature Matrix

| Feature | Description | Implementation Component |
| :--- | :--- | :--- |
| **Book Inventory Management** | Add, search, and track total vs. available book copies in real-time. | `Book.java`, `BookRepository.java` |
| **Role-Based Borrowing** | Subclass rules for Students (max 3) and Faculty (max 10). | `Member.java`, `StudentMember.java`, `FacultyMember.java` |
| **Transaction Processing** | Automated issue validation, due date generation (14 days), and return logging. | `BorrowTransaction.java`, `TransactionRepository.java` |
| **Dynamic Fine Calculation** | Automated overdue day calculation using `ChronoUnit.DAYS` with daily rate multipliers. | `FineCalculatorService.java` |
| **Input Validation** | Strict regex validation for ISBNs, Member IDs (`STU-xxx`, `FAC-xxx`), and dates. | `InputValidator.java` |
| **JDBC SQL Driver** | Thread-safe Singleton managing SQLite connection and schema creation. | `DatabaseManager.java` |
| **High-Contrast GUI Workbench** | Swing interface with color palette specs (`#0284C7`, `#FACC15`, `#F8FAFC`). | `Main.java` |
| **Automated Unit Testing** | JUnit 5 unit test suite verifying fine logic and borrowing limits. | `LibraryServiceTest.java` |

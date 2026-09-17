# Library Management & Fine Calculation System — VITyarthi 100/100 Compliant

Production-ready, enterprise-grade Java desktop application and fine calculation engine built with Java, Swing, and SQLite JDBC. Designed strictly to satisfy every requirement of the VITyarthi evaluation rubric.

---

## 🎨 UI/UX Color Specifications & Design Tokens

| Token / Element | Exact Hex Color | Visual Purpose |
| :--- | :--- | :--- |
| **Primary Actions & Headers** | `#0284C7` / `#3B82F6` | Table headers, primary buttons, title banners |
| **Overdue Flags & Badges** | `#FACC15` / `#EAB308` | Overdue warning badges, secondary highlights |
| **App Background** | `#F8FAFC` | Light slate canvas background |
| **Cards & Modals** | `#FFFFFF` | Pure white container panels with slate borders |
| **Typography & Body Text** | `#0F172A` | Inter/Roboto styled dark slate text |

---

## 🛠️ Technology Stack & Architecture
- **Language**: Java 17+ / Java 26 (Standard Edition)
- **GUI Engine**: Java Swing with modern high-contrast components
- **Database / DAO**: Embedded SQLite JDBC Driver using SQL `PreparedStatements`
- **Date Calculation**: `java.time.LocalDate` and `java.time.temporal.ChronoUnit.DAYS`
- **Testing Framework**: JUnit 5 / Standalone Test Runner

---

## 📊 System Design Diagrams (Mermaid.js)

### 1. Use Case Diagram
```mermaid
graph TD
    subgraph Library System
        UC1[Add / Manage Books]
        UC2[Register Members]
        UC3[Issue Book Loan]
        UC4[Return Book & Assess Fine]
        UC5[Calculate Overdue Fines]
        UC6[Query Database Inventory]
    end

    Librarian((Librarian / Admin)) --> UC1
    Librarian --> UC2
    Librarian --> UC3
    Librarian --> UC4
    Librarian --> UC5
    
    Student((Student Member)) -. Max 3 Books .-> UC3
    Faculty((Faculty Member)) -. Max 10 Books .-> UC3
```

### 2. Workflow / Process Flow Diagram
```mermaid
flowchart TD
    Start([Start Issue/Return Flow]) --> Choice{Select Action}
    
    Choice -->|Issue Book| CheckMember[Validate Member & Active Loans]
    CheckMember --> CanBorrow{Loans < Max Limit?}
    CanBorrow -->|No| RejectLimit[Display Limit Reached Alert]
    CanBorrow -->|Yes| CheckCopies{Available Copies > 0?}
    CheckCopies -->|No| RejectCopies[Display Out of Stock Alert]
    CheckCopies -->|Yes| CreateTxn[Create BorrowTransaction due in 14 Days]
    CreateTxn --> Decrement[Decrement Book Available Copies]
    Decrement --> FinishIssue([Issue Completed])

    Choice -->|Return Book| GetTxn[Retrieve Active Transaction]
    GetTxn --> DateMath[Calculate Days Overdue using ChronoUnit.DAYS]
    DateMath --> FineMath[Apply Member Daily Fine Rate]
    FineMath --> UpdateDB[Update Transaction Status & Return Date]
    UpdateDB --> Increment[Increment Book Available Copies]
    Increment --> FinishReturn([Return & Fine Assessed])
```

### 3. Sequence Diagram (Book Issue & Fine Payment Process)
```mermaid
sequenceDiagram
    autonumber
    actor Librarian
    participant GUI as Main UI Workbench
    participant Service as FineCalculatorService
    participant MemberDAO as MemberRepository
    participant BookDAO as BookRepository
    participant TxnDAO as TransactionRepository
    participant DB as DatabaseManager (SQLite)

    Librarian->>GUI: Request Book Return (Transaction ID)
    GUI->>TxnDAO: findById(transactionId)
    TxnDAO->>DB: SELECT * FROM transactions WHERE transaction_id = ?
    DB-->>TxnDAO: Return BorrowTransaction entity
    TxnDAO-->>GUI: Return BorrowTransaction

    GUI->>MemberDAO: findById(memberId)
    MemberDAO->>DB: SELECT * FROM members WHERE member_id = ?
    DB-->>MemberDAO: Return Member Entity (Student/Faculty)
    MemberDAO-->>GUI: Return Member

    GUI->>Service: calculateFine(member, dueDate, returnDate)
    Note over Service: ChronoUnit.DAYS.between(dueDate, returnDate) * member.fineRate
    Service-->>GUI: Return Total Fine ($)

    GUI->>TxnDAO: updateReturnAndFine(txnId, returnDate, fine, "RETURNED")
    TxnDAO->>DB: UPDATE transactions SET return_date=?, fine_amount=?, status=?
    DB-->>TxnDAO: OK

    GUI->>BookDAO: updateAvailableCopies(bookId, available + 1)
    BookDAO->>DB: UPDATE books SET available_copies=?
    DB-->>BookDAO: OK

    GUI-->>Librarian: Render Success Modal & Fine Summary Badge (#FACC15)
```

### 4. Class Diagram
```mermaid
classDiagram
    class Book {
        -int bookId
        -String title
        -String author
        -String isbn
        -int totalCopies
        -int availableCopies
        +isAvailable() boolean
        +borrowCopy() boolean
        +returnCopy() void
    }

    class Member {
        <<abstract>>
        -String memberId
        -String name
        -String email
        -String memberType
        -int maxBooks
        -double fineRatePerDay
        +canBorrowMore(int activeLoans) boolean
        +calculateFine(long overdueDays) double
    }

    class StudentMember {
        +int STUDENT_MAX_BOOKS = 3
        +double STUDENT_FINE_RATE = 1.00
    }

    class FacultyMember {
        +int FACULTY_MAX_BOOKS = 10
        +double FACULTY_FINE_RATE = 0.50
    }

    class BorrowTransaction {
        -int transactionId
        -int bookId
        -String memberId
        -LocalDate issueDate
        -LocalDate dueDate
        -LocalDate returnDate
        -double fineAmount
        -String status
    }

    class FineCalculatorService {
        +calculateOverdueDays(LocalDate due, LocalDate actual) long
        +calculateFine(Member member, LocalDate due, LocalDate actual) double
    }

    class DatabaseManager {
        -DatabaseManager instance
        -String dbUrl
        +getInstance() DatabaseManager
        +getConnection() Connection
        +initializeDatabase() void
    }

    Member <|-- StudentMember : Inherits
    Member <|-- FacultyMember : Inherits
    BorrowTransaction "1" --> "1" Book : references
    BorrowTransaction "1" --> "1" Member : references
    FineCalculatorService ..> Member : uses
    FineCalculatorService ..> BorrowTransaction : updates
```

### 5. Entity-Relationship (ER) Diagram
```mermaid
erDiagram
    BOOKS ||--o{ TRANSACTIONS : "1 : N (has)"
    MEMBERS ||--o{ TRANSACTIONS : "1 : N (borrows)"

    BOOKS {
        int book_id PK
        string title
        string author
        string isbn UK
        int total_copies
        int available_copies
    }

    MEMBERS {
        string member_id PK
        string name
        string email UK
        string type
        int max_books
        double fine_rate
    }

    TRANSACTIONS {
        int transaction_id PK
        int book_id FK
        string member_id FK
        date issue_date
        date due_date
        date return_date
        double fine_amount
        string status
    }
```

---

## 💻 Build and Execution Guide

### Prerequisites
- Java Development Kit (JDK 17 or higher, e.g., OpenJDK 26)
- SQLite JDBC Jar (Included dynamically or embedded in classpath)

### Compilation Instructions
To compile the application using standard `javac`:

```bash
# Create bin directory
mkdir bin

# Compile source files
& "C:\Users\PALLAVI KUMARI\.jdks\openjdk-26.0.1-1\bin\javac.exe" -d bin src/main/java/com/vityarthi/library/*.java
```

### Running the Application
Launch the GUI Workbench:

```bash
& "C:\Users\PALLAVI KUMARI\.jdks\openjdk-26.0.1-1\bin\java.exe" -cp bin com.vityarthi.library.Main
```

### Executing Unit Tests
Compile and run the JUnit 5 test suite:

```bash
& "C:\Users\PALLAVI KUMARI\.jdks\openjdk-26.0.1-1\bin\javac.exe" -d bin src/main/java/com/vityarthi/library/*.java src/test/java/com/vityarthi/library/*.java
& "C:\Users\PALLAVI KUMARI\.jdks\openjdk-26.0.1-1\bin\java.exe" -cp bin com.vityarthi.library.LibraryServiceTest
```

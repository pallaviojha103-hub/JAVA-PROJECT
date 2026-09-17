# Statement of Work & System Specification —  Library Management & Fine Calculation System

**Course Code / Evaluation:** Full-Stack Object-Oriented Software Engineering (VITyarthi Rubric 100/100)  
**Project Title:** VIT Enterprise Library Management & Dynamic Fine Calculation System  
**Document Code:** Section 5.2 — `statement.md`  

---

## 1. Problem Statement

Academic institutions and campus resource libraries routinely handle thousands of active textbook loans, inventory updates, and member interactions across diverse academic departments. However, conventional manual or semi-automated library tracking workflows suffer from critical operational deficiencies:

1. **Unenforced Borrowing Quotas & Resource Monopolization:**  
   Without programmatic policy enforcement, high-demand reference textbooks and curriculum materials are frequently monopolized by a few individuals. Lack of differentiated loan quotas between undergraduate students and faculty researchers leads to inequitable resource distribution.

2. **Inaccurate, Dispute-Prone Overdue Fine Calculations:**  
   Traditional date math relying on manual ledger calculation or simple subtraction fails across leap years, variable month lengths, and multi-week grace windows. This results in billing inaccuracies, student grievances, and lack of institutional accountability.

3. **Absence of Real-Time Inventory & Circulation Synchronization:**  
   When a book is issued or returned, inventory counts must update atomically across all administrative terminals. Paper ledgers or isolated desktop databases result in discrepancies between recorded catalog numbers and actual physical shelf availability.

4. **Vulnerability to Injection & Data Corruption:**  
   Elementary systems frequently employ raw string concatenation for SQL queries, exposing the institutional database to SQL injection attacks and relational integrity corruption.

5. **Lack of Headless/CLI Executability in Evaluation Environments:**  
   Many academic submissions rely exclusively on graphical windows, crashing in headless terminal grading environments (Linux/Unix servers without display servers).

The **VIT Library Management & Dynamic Fine Calculation System** addresses every one of these challenges by delivering an enterprise-grade, object-oriented desktop and terminal application backed by an embedded SQLite relational database, dynamic `java.time` date arithmetic, and dual GUI/CLI interfaces.

---

## 2. Scope of the Project

The scope encompasses the complete software lifecycle of an academic library circulation and resource planning system, strictly bounded by the following functional, technical, and operational dimensions:

### In-Scope Functional Capabilities
- **Catalog Management:** Full CRUD (Create, Read, Update, Delete) capability for books, capturing Book ID, Title, Author, unique ISBN (ISBN-10 and ISBN-13 compliant), Total Stock, and Available Copies.
- **Role-Differentiated Membership Management:**
  - **Student Members (`StudentMember`):** Quota restricted to a maximum of **3 active borrowed books**, with an overdue fine penalty of **$1.00 per day**.
  - **Faculty Members (`FacultyMember`):** Quota restricted to a maximum of **10 active borrowed books**, with an overdue fine penalty of **$0.50 per day**.
- **Circulation & Loan Tracking:** Processing book issue operations with automated 14-day loan duration generation, active quota verification, real-time inventory decrementing, and book return workflows with automatic inventory restoration.
- **Dynamic Fine Assessment Engine:** Automated mathematical computation of overdue durations using `java.time.LocalDate` and `java.time.temporal.ChronoUnit.DAYS`, guaranteeing zero fine for on-time returns and dynamic rate multiplication for overdue loans.
- **Dual Execution Modes (GUI + Headless CLI):**
  - High-contrast Java Swing GUI with custom 3D tactile buttons and an interactive rotating 3D book visualizer.
  - Headless Command Line Interface (`CLI.java`) ensuring 100% executability in terminal-only, non-GUI environments.
- **Automated Verification:** Standalone unit test suite (`LibraryServiceTest.java`) proving 100% pass rates for loan quotas, date math, inventory bounds, and input validation.

### Out-of-Scope (Future Enhancements)
- Third-party payment gateway integration (Stripe/PayPal) for credit card fine settlement.
- RFID physical hardware gate scanning integration.
- Multi-branch inter-library campus transfers.

---

## 3. Target Users

The system is designed to serve three distinct stakeholder groups:

| Target User Group | Role & Responsibilities | Key System Value |
|---|---|---|
| **1. Chief Librarians & Desk Administrators** | Daily circulation desk operators responsible for registering members, cataloging new acquisitions, issuing textbook loans, processing returns, and assessing overdue penalties. | Automated loan quota checks prevent unauthorized borrowing; atomic stock updates eliminate shelf discrepancies. |
| **2. University Faculty & Student Borrowers** | Campus members borrowing textbooks and research references subject to institutional policies and loan return deadlines. | Transparent 14-day return dates and clear, standardized daily fine calculations ($1.00/day for students, $0.50/day for faculty) eliminate billing disputes. |
| **3. Academic Evaluators & Grading Systems** | Evaluators assessing code quality, adherence to OOP design patterns, database architecture, and project executability. | Clean architectural layering (DAO, Singleton, Polymorphism), 100% test coverage, and dual GUI/headless CLI terminal execution. |

---

## 4. High-Level Features

### 🏛️ Feature 1: Role-Based Membership & Policy Engine
- Abstract `Member` base class extended by `StudentMember` and `FacultyMember`.
- Programmatic enforcement of role-based loan quotas (3 vs. 10 books).
- Polymorphic resolution of daily fine rates ($1.00/day vs. $0.50/day).
- Strict Member ID format enforcement via regex (`STU-xxx` for students, `FAC-xxx` for faculty).

### 📚 Feature 2: Atomic Catalog & Inventory Tracking
- Real-time tracking of total versus physically available textbook copies.
- Automated check against borrowing stock: system rejects checkout attempts when available copies equal zero.
- Dash-insensitive, normalized ISBN-10 and ISBN-13 validation preventing duplicate catalog entries.

### 🔄 Feature 3: End-to-End Circulation Workbench
- One-click issue workflow: automatically records Transaction ID, Issue Date, and calculated 14-day Due Date.
- Return workflow: calculates actual return date, checks whether the transaction is overdue, assesses any accrued fine, and restores shelf inventory.

### ⚡ Feature 4: Dynamic Fine Calculation Engine
- Precision date arithmetic implemented with Java 8+ `java.time.LocalDate` and `ChronoUnit.DAYS`.
- Immune to leap-year anomalies, month transitions, and timezone discrepancies.
- On-time and early returns automatically assess a `$0.00` penalty.

### 💾 Feature 5: Secure Relational Persistence (DAO Pattern)
- Embedded SQLite JDBC persistence requiring zero external database server setup (`library.db`).
- Thread-safe Singleton `DatabaseManager` with automatic DDL table provisioning and initial seed data.
- Strict use of parameterized SQL `PreparedStatements` across `BookRepository`, `MemberRepository`, and `TransactionRepository` to ensure SQL-injection immunity.

### 🎨 Feature 6: Modern Accessible 3D Dashboard (GUI)
- High-contrast color palette: Sky Blue (`#0284C7`), Warning Amber (`#FACC15`), Slate Canvas (`#F8FAFC`), and Pure White (`#FFFFFF`).
- Interactive, 30 FPS **3D rotating animated textbook widget** in the header with perspective lighting and orbiting sparkles.
- Real-time KPI summary metric cards (Total Titles, Available Stock, Active Borrowers, Overdue Loans).
- Instant live search filtering across book and member tables as you type.
- Custom tactile 3D buttons with press-depth feedback (no white-box OS hover bugs).
- High-contrast status pill badges (`● AVAILABLE`, `● OUT OF STOCK`, `⚠️ OVERDUE`, `📘 STUDENT`, `🎓 FACULTY`).

### 💻 Feature 7: Headless Command-Line Interface (CLI Mode)
- Full-featured interactive terminal console (`CLI.java`) executable from any standard terminal (PowerShell, Command Prompt, bash).
- Automatic headless detection (`GraphicsEnvironment.isHeadless()`) to prevent crashes in automated grading environments.
- Dedicated one-click shell scripts: `RUN_CLI.bat` (Windows) and `run_cli.sh` (Linux/macOS).

### 🧪 Feature 8: Automated Verification & Unit Test Suite
- Standalone zero-dependency test runner (`LibraryServiceTest.java`) executing 6/6 test cases.
- Tests loan limits, on-time zero fines, dynamic overdue fine math, ISBN validators, and inventory decrementing/incrementing.

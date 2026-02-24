# Java JDBC: Employee & Department Manager

## Learning Objective
This project is a focused exploration of **Java Database Connectivity (JDBC)** and the **DAO pattern**, built to understand low-level persistence concerns—SQL execution, transactions, and error handling—before moving to abstraction-heavy frameworks such as Spring Boot and Spring Data JPA.

## Core Technical Highlights
- **Explicit Transaction Management**  
  Manual control using `setAutoCommit(false)` with explicit `commit()` and `rollback()` to maintain consistency across multi-step operations (e.g., updating an employee’s role while transferring departments).

- **DAO-Based Architecture**  
  Clear separation between business logic and data access logic, improving maintainability and making later migration to ORM-based solutions straightforward.

- **CLI Input Handling**  
  Addressed common `Scanner` pitfalls, including newline buffering issues when mixing numeric and string inputs.

- **Relational Integrity Awareness**  
  Foreign key constraints are enforced at the database level, with application-side handling to surface meaningful feedback when constraint violations occur.

- **Defensive Data Access**
  Handled non-existent identifiers and no-row-affected cases (e.g., updates or deletes on missing IDs), ensuring predictable application behavior instead of silent failures.

## Technology Stack
- **Language:** Java 25  
- **Database:** PostgreSQL  
- **Persistence:** JDBC (no ORM)

## Known Limitations
- **Validation Scope**  
  This project intentionally omits advanced validation (e.g., regex-based email checks or full input sanitization). The focus is strictly on JDBC mechanics and SQL interaction.

- **CLI Robustness**  
  While basic exception scenarios (such as `InputMismatchException`) were tested, the CLI is not hardened against all possible malformed input.

## Future Direction
The next iteration will migrate this application to **Spring Data JPA**, enabling a direct comparison between manual JDBC workflows and ORM-driven development—particularly around transaction management and boilerplate reduction.

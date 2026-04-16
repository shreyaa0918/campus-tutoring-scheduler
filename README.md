# Campus Tutoring Scheduler — Phase I Implementation

**Course:** ACS 56000-01 Software Engineering  
**Team:** Shreya Komarabattini, Shreya Sagar  
**Date:** April 2026

---

## System Overview

The Campus Tutoring Scheduler is a web-based system that allows university students to book tutoring sessions for academic courses such as Math, CS, and Chemistry. Tutors manage their availability and appointment requests, while Admins oversee tutor assignments, courses, and reporting.

---

## Project Structure

```
CampusTutoringScheduler/
└── src/
    └── main/
        └── java/
            └── com/
                └── tutoring/
                    ├── Main.java                        ← Entry point / simulation runner
                    ├── model/
                    │   ├── User.java                    ← Abstract base class (generalization)
                    │   ├── Student.java                 ← Extends User; booking operations
                    │   ├── Tutor.java                   ← Extends User; session management
                    │   ├── Admin.java                   ← System-level management
                    │   ├── Appointment.java             ← Central booking entity
                    │   ├── Course.java                  ← Academic subject model
                    │   └── Availability.java            ← Tutor time slot model
                    ├── service/
                    │   └── NotificationService.java     ← Confirmations, reminders, cancellations
                    └── util/
                        └── BookingValidator.java        ← BR1 and BR2 enforcement
```

---

## UML → Code Mapping

| UML Element | Java Implementation |
|---|---|
| `User` base class | `model/User.java` (abstract) |
| `Student` (inherits User) | `model/Student.java` |
| `Tutor` (inherits User) | `model/Tutor.java` |
| `Admin` | `model/Admin.java` |
| `Appointment` | `model/Appointment.java` |
| `Course` | `model/Course.java` |
| `Availability` | `model/Availability.java` |
| `NotificationService` | `service/NotificationService.java` |
| BR1 / BR2 validation | `util/BookingValidator.java` |

### Sequence Diagrams Implemented

| Sequence Diagram | Simulated In |
|---|---|
| Book Appointment | `Main.java` → SEQUENCE 1 section |
| Cancel / Reschedule Appointment | `Main.java` → SEQUENCE 2 section |

### Business Rules Enforced

| Rule | Description | Implementation |
|---|---|---|
| **BR1** | Max 2 active appointments per week | `BookingValidator.checkBookingLimit()` — `<<include>>` |
| **BR2** | Late cancellation if within 6 hours | `BookingValidator.isLateCancellation()` — `<<extend>>` |
| **BR3** | Slot released when tutor declines | `Tutor.makeSlotAvailable()` — `<<extend>>` |
| **BR4** | Rating only after completed session | `Student.rateTutor()` — `<<extend>>` |

---

## How to Run

### Prerequisites
- Java SDK 11 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code with Java Extension Pack)

### Option A — Using an IDE
1. Open the `CampusTutoringScheduler` folder as a project
2. Navigate to `src/main/java/com/tutoring/Main.java`
3. Right-click → Run `Main`

### Option B — Command Line (javac)
```bash
# From the project root
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out com.tutoring.Main
```

---

## Naming Conventions

| Element | Convention | Example |
|---|---|---|
| Classes | PascalCase | `NotificationService` |
| Methods | camelCase | `requestAppointment()` |
| Variables | camelCase | `activeAppointments` |
| Constants | UPPER_SNAKE_CASE | `MAX_ACTIVE_APPOINTMENTS` |
| Packages | lowercase | `com.tutoring.model` |

---

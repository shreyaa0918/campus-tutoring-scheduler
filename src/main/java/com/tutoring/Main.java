package com.tutoring;

import com.tutoring.model.*;
import com.tutoring.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point for the Campus Tutoring Scheduler simulation.
 *
 * Demonstrates two core sequence flows:
 *   1. Book Appointment (Sequence Diagram 1)
 *   2. Cancel / Reschedule Appointment (Sequence Diagram 2)
 *
 * Also demonstrates Admin operations.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=================================================");
        System.out.println("  Campus Tutoring Scheduler — Phase I Simulation");
        System.out.println("  ACS 56000-01 Software Engineering");
        System.out.println("  Team: Shreya Komarabattini, Shreya Sagar");
        System.out.println("=================================================\n");

        // -------------------------------------------------------
        // SETUP: Initialize system entities
        // -------------------------------------------------------

        NotificationService notifier = new NotificationService();

        // Courses
        Course calculus   = new Course("C001", "Calculus I", "Mathematics");
        Course python     = new Course("C002", "Computer Science - Python", "Programming");
        Course chemistry  = new Course("C003", "Chemistry", "Science");

        // Tutors
        Tutor sarahJohnson = new Tutor("T001", "Sarah Johnson",
                "sarah.johnson@university.edu", "pass123",
                "Graduate student in Mathematics with 3 years of tutoring experience.");
        sarahJohnson.addCourse("Calculus I");
        sarahJohnson.addCourse("Linear Algebra");
        sarahJohnson.setAvailability("Monday Feb 17 10:00 AM");
        sarahJohnson.setAvailability("Monday Feb 17 11:00 AM");
        sarahJohnson.setAvailability("Wednesday Feb 19 1:00 PM");

        Tutor michaelChen = new Tutor("T002", "Michael Chen",
                "michael.chen@university.edu", "pass456",
                "Computer Science PhD student specializing in Python and algorithms.");
        michaelChen.addCourse("Computer Science - Python");
        michaelChen.setAvailability("Thursday Feb 20 2:00 PM");
        michaelChen.setAvailability("Friday Feb 21 10:00 AM");

        // Students
        Student alex = new Student("S001", "Alex Rivera",
                "alex.rivera@university.edu", "studentpass");

        // Admin
        Admin admin = new Admin("A001", "Admin User",
                "admin@university.edu", "adminpass");

        // System-level collections
        List<Tutor>       allTutors       = new ArrayList<>();
        List<Course>      allCourses      = new ArrayList<>();
        List<Appointment> allAppointments = new ArrayList<>();

        allTutors.add(sarahJohnson);
        allTutors.add(michaelChen);
        allCourses.add(calculus);
        allCourses.add(python);
        allCourses.add(chemistry);

        // -------------------------------------------------------
        // SEQUENCE DIAGRAM 1: Book Appointment
        // -------------------------------------------------------
        System.out.println("\n─────────────────────────────────────────────────");
        System.out.println("  SEQUENCE 1: Book Appointment");
        System.out.println("─────────────────────────────────────────────────\n");

        // Step 1: Student logs in
        boolean loggedIn = alex.login("studentpass");
        if (!loggedIn) return;

        // Step 2: Search for tutors
        List<Tutor> results = alex.searchTutor("Calculus I", "Monday", allTutors);
        if (results.isEmpty()) {
            System.out.println("No tutors found.");
            return;
        }

        // Step 3: View tutor profile
        Tutor selectedTutor = results.get(0);
        System.out.println("[PROFILE] Viewing profile: " + selectedTutor);

        // Step 4: Request appointment (includes BR1 check + notification)
        Appointment appt1 = alex.requestAppointment(
                selectedTutor,
                calculus,
                "Monday Feb 17 10:00 AM",
                "Virtual",
                "Need help with integration by parts.",
                notifier
        );

        if (appt1 != null) {
            allAppointments.add(appt1);
        }

        // Step 5: Request a second appointment (should be allowed — limit is 2)
        Appointment appt2 = alex.requestAppointment(
                michaelChen,
                python,
                "Thursday Feb 20 2:00 PM",
                "In-Person",
                "Stuck on recursion problems.",
                notifier
        );

        if (appt2 != null) {
            allAppointments.add(appt2);
        }

        // Step 6: Attempt a third appointment — should be REJECTED by BR1
        System.out.println("\n[TEST] Attempting third appointment (should be rejected by BR1)...");
        Tutor emilyDavis = new Tutor("T003", "Emily Davis",
                "emily.davis@university.edu", "pass789",
                "Chemistry specialist.");
        emilyDavis.addCourse("Chemistry");
        emilyDavis.setAvailability("Friday Feb 21 11:00 AM");

        Appointment appt3 = alex.requestAppointment(
                emilyDavis,
                chemistry,
                "Friday Feb 21 11:00 AM",
                "Virtual",
                "",
                notifier
        );
        // appt3 should be null (limit exceeded)

        // Step 7: Log out
        alex.logout();

        // -------------------------------------------------------
        // SEQUENCE DIAGRAM 2: Cancel / Reschedule Appointment
        // -------------------------------------------------------
        System.out.println("\n─────────────────────────────────────────────────");
        System.out.println("  SEQUENCE 2: Cancel / Reschedule Appointment");
        System.out.println("─────────────────────────────────────────────────\n");

        // Student logs back in
        alex.login("studentpass");

        // View appointments
        System.out.println("[VIEW] My Appointments (" + alex.getAppointments().size() + " total):");
        for (Appointment a : alex.getAppointments()) {
            System.out.println("  - " + a);
        }

        // Reschedule appt1 to a different slot
        if (appt1 != null) {
            System.out.println("\n[ACTION] Rescheduling appointment: " + appt1.getAppointmentId());
            alex.rescheduleAppointment(appt1, "Wednesday Feb 19 1:00 PM", notifier);
        }

        // Cancel appt2
        if (appt2 != null) {
            System.out.println("\n[ACTION] Cancelling appointment: " + appt2.getAppointmentId());
            alex.cancelAppointment(appt2, notifier);
        }

        // Student logs out
        alex.logout();

        // -------------------------------------------------------
        // ADMIN OPERATIONS
        // -------------------------------------------------------
        System.out.println("\n─────────────────────────────────────────────────");
        System.out.println("  ADMIN OPERATIONS");
        System.out.println("─────────────────────────────────────────────────\n");

        admin.login("adminpass");

        // Assign tutor to a new course
        admin.assignTutor(sarahJohnson, chemistry);

        // Mark first session completed and rate tutor
        if (appt1 != null) {
            sarahJohnson.markCompleted(appt1);
            System.out.println("\n[STUDENT] Rating tutor after completed session...");
            alex.rateTutor(sarahJohnson, 4.9);
        }

        // Generate system report
        admin.generateReports(allAppointments);

        // View audit log
        admin.viewAuditLog();

        admin.logout();

        System.out.println("=================================================");
        System.out.println("  Simulation Complete");
        System.out.println("=================================================");
    }
}

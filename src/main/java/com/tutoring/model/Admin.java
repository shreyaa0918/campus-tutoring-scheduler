package com.tutoring.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an administrator with system-level management capabilities.
 * Manages courses, tutor assignments, reports, and user accounts.
 * All Admin actions are logged for auditing (NFR requirement).
 */
public class Admin extends User {

    private List<String> auditLog;

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.auditLog = new ArrayList<>();
    }

    /**
     * Adds a new course to the system.
     *
     * @param course     the course to add
     * @param courseList the system's master course list
     */
    public void manageCourses(Course course, List<Course> courseList) {
        courseList.add(course);
        logAction("Added course: " + course.getName());
        System.out.println("[ADMIN] Course added: " + course.getName());
    }

    /**
     * Assigns a tutor to a specific course.
     *
     * @param tutor  the tutor to assign
     * @param course the course to assign them to
     */
    public void assignTutor(Tutor tutor, Course course) {
        tutor.addCourse(course.getName());
        logAction("Assigned tutor " + tutor.getName() + " to course: " + course.getName());
        System.out.println("[ADMIN] Tutor " + tutor.getName()
                + " assigned to course: " + course.getName());
    }

    /**
     * Generates a summary report of all appointments.
     *
     * @param appointments the full list of appointments to report on
     */
    public void generateReports(List<Appointment> appointments) {
        logAction("Generated appointment report. Total records: " + appointments.size());
        System.out.println("\n===== APPOINTMENT REPORT =====");
        System.out.printf("%-15s %-20s %-20s %-12s %-12s%n",
                "Appt ID", "Student", "Tutor", "Date/Time", "Status");
        System.out.println("-".repeat(80));
        for (Appointment a : appointments) {
            System.out.printf("%-15s %-20s %-20s %-12s %-12s%n",
                    a.getAppointmentId(),
                    a.getStudent().getName(),
                    a.getTutor().getName(),
                    a.getDateTime(),
                    a.getStatus());
        }
        System.out.println("==============================\n");
    }

    /**
     * Deactivates a user account (student or tutor).
     *
     * @param user the user to deactivate
     */
    public void deactivateUser(User user) {
        user.setActive(false);
        logAction("Deactivated user: " + user.getEmail());
        System.out.println("[ADMIN] User deactivated: " + user.getEmail());
    }

    /**
     * Reactivates a previously deactivated user account.
     *
     * @param user the user to reactivate
     */
    public void reactivateUser(User user) {
        user.setActive(true);
        logAction("Reactivated user: " + user.getEmail());
        System.out.println("[ADMIN] User reactivated: " + user.getEmail());
    }

    /**
     * Prints the full audit log to console.
     */
    public void viewAuditLog() {
        System.out.println("\n===== AUDIT LOG =====");
        for (int i = 0; i < auditLog.size(); i++) {
            System.out.println((i + 1) + ". " + auditLog.get(i));
        }
        System.out.println("=====================\n");
    }

    private void logAction(String action) {
        String entry = "[" + java.time.LocalDateTime.now() + "] ADMIN(" + name + "): " + action;
        auditLog.add(entry);
    }

    public List<String> getAuditLog() {
        return auditLog;
    }
}

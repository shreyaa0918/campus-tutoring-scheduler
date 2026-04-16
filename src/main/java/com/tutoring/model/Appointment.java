package com.tutoring.model;

/**
 * Central entity representing a tutoring session booking.
 * Connects Student and Tutor; enforces booking lifecycle logic.
 */
public class Appointment {

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, DECLINED, COMPLETED, LATE_CANCELLED
    }

    private String appointmentId;
    private Student student;
    private Tutor tutor;
    private Course course;
    private String dateTime;
    private String meetingMode;
    private String notes;
    private Status status;
    private boolean lateCancellation;

    public Appointment(String appointmentId, Student student, Tutor tutor,
                       Course course, String dateTime, String meetingMode, String notes) {
        this.appointmentId = appointmentId;
        this.student = student;
        this.tutor = tutor;
        this.course = course;
        this.dateTime = dateTime;
        this.meetingMode = meetingMode;
        this.notes = notes;
        this.status = Status.PENDING;
        this.lateCancellation = false;
    }

    /**
     * Creates and persists the appointment (called upon tutor acceptance).
     */
    public void create() {
        this.status = Status.CONFIRMED;
        System.out.println("[APPOINTMENT] Created: " + appointmentId
                + " | " + course.getName() + " | " + dateTime);
    }

    /**
     * Cancels this appointment.
     */
    public void cancel() {
        if (lateCancellation) {
            this.status = Status.LATE_CANCELLED;
        } else {
            this.status = Status.CANCELLED;
        }
        System.out.println("[APPOINTMENT] Cancelled: " + appointmentId);
    }

    /**
     * Reschedules this appointment to a new time slot.
     *
     * @param newDateTime the replacement time slot
     */
    public void reschedule(String newDateTime) {
        String old = this.dateTime;
        this.dateTime = newDateTime;
        System.out.println("[APPOINTMENT] Rescheduled: " + appointmentId
                + " from " + old + " to " + newDateTime);
    }

    /**
     * Flags this appointment as a late cancellation (BR2).
     */
    public void markLateCancellation() {
        this.lateCancellation = true;
        System.out.println("[APPOINTMENT] Late cancellation flag set: " + appointmentId);
    }

    // --- Getters and Setters ---

    public String getAppointmentId() { return appointmentId; }
    public Student getStudent() { return student; }
    public Tutor getTutor() { return tutor; }
    public Course getCourse() { return course; }
    public String getDateTime() { return dateTime; }
    public String getMeetingMode() { return meetingMode; }
    public String getNotes() { return notes; }
    public Status getStatus() { return status; }
    public boolean isLateCancellation() { return lateCancellation; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Appointment[%s] %s with %s | %s | %s | %s",
                appointmentId,
                student.getName(),
                tutor.getName(),
                course.getName(),
                dateTime,
                status);
    }
}

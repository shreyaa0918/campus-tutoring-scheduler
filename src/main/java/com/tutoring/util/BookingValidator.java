package com.tutoring.util;

import com.tutoring.model.Appointment;
import com.tutoring.model.Student;

/**
 * Utility class for validating booking-related business rules.
 *
 * Business Rules enforced:
 *   BR1 — Maximum 2 active appointments per week per student
 *   BR2 — Late cancellation/reschedule if within 6 hours of session
 */
public class BookingValidator {

    private static final int LATE_CANCELLATION_HOURS = 6;

    /**
     * Enforces BR1: checks whether a student has exceeded their weekly booking limit.
     * Modeled as <<include>> in the use case diagram.
     *
     * @param student the student requesting a new appointment
     * @return true if the booking limit has NOT been exceeded
     */
    public static boolean checkBookingLimit(Student student) {
        long activeCount = student.getActiveAppointmentCount();
        System.out.println("[VALIDATOR] BR1 Check — Active appointments: "
                + activeCount + " / " + Student.MAX_ACTIVE_APPOINTMENTS);
        return activeCount < Student.MAX_ACTIVE_APPOINTMENTS;
    }

    /**
     * Enforces BR2: determines if a cancellation/reschedule is occurring
     * within 6 hours of the scheduled session time.
     * Modeled as <<extend>> in the use case diagram.
     *
     * Simplified simulation: flags as late if slot contains "Today" in description.
     * In a production system this would compare actual timestamps.
     *
     * @param appointment the appointment being modified
     * @return true if the action qualifies as a late cancellation
     */
    public static boolean isLateCancellation(Appointment appointment) {
        // Production implementation would parse dateTime and compare to LocalDateTime.now()
        // Simplified for simulation purposes:
        String slot = appointment.getDateTime().toLowerCase();
        boolean isLate = slot.contains("today") || slot.contains("imminent");
        System.out.println("[VALIDATOR] BR2 Check — Late cancellation: " + isLate);
        return isLate;
    }
}

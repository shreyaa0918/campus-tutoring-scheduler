package main.java.com.tutoring.service;

import main.java.com.tutoring.model.Appointment;
import main.java.com.tutoring.model.Tutor;
import main.java.com.tutoring.model.User;

/**
 * Handles all automated notifications within the system.
 * Triggered by key events: booking, confirmation, cancellation, and reminders.
 * Modeled as <<include>> dependencies in the use case diagram.
 */
public class NotificationService {

    /**
     * Sends a booking confirmation to the student.
     *
     * @param user        the recipient user
     * @param appointment the confirmed appointment
     */
    public void sendConfirmation(User user, Appointment appointment) {
        System.out.println("[NOTIFICATION] Confirmation email sent to: " + user.getEmail());
        System.out.println("  → Appointment: " + appointment.getAppointmentId()
                + " | " + appointment.getCourse().getName()
                + " | " + appointment.getDateTime()
                + " | Mode: " + appointment.getMeetingMode());
    }

    /**
     * Sends a session reminder to a user.
     *
     * @param user        the recipient user
     * @param appointment the upcoming appointment
     */
    public void sendReminder(User user, Appointment appointment) {
        System.out.println("[NOTIFICATION] Reminder sent to: " + user.getEmail());
        System.out.println("  → Upcoming session: " + appointment.getDateTime()
                + " with " + (user.equals(appointment.getStudent())
                        ? appointment.getTutor().getName()
                        : appointment.getStudent().getName()));
    }

    /**
     * Sends a cancellation notice to the given user.
     *
     * @param user        the recipient user (student or tutor)
     * @param appointment the cancelled appointment
     */
    public void sendCancellation(User user, Appointment appointment) {
        System.out.println("[NOTIFICATION] Cancellation notice sent to: " + user.getEmail());
        System.out.println("  → Cancelled: " + appointment.getAppointmentId()
                + " | " + appointment.getDateTime()
                + (appointment.isLateCancellation() ? " [LATE CANCELLATION]" : ""));
    }

    /**
     * Notifies a tutor of a new incoming appointment request.
     *
     * @param tutor       the tutor to notify
     * @param appointment the incoming appointment request
     */
    public void notifyTutorRequest(Tutor tutor, Appointment appointment) {
        System.out.println("[NOTIFICATION] New appointment request sent to tutor: "
                + tutor.getEmail());
        System.out.println("  → From: " + appointment.getStudent().getName()
                + " | Course: " + appointment.getCourse().getName()
                + " | Slot: " + appointment.getDateTime());
    }
}

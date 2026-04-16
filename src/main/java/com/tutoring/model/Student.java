package com.tutoring.model;

import com.tutoring.service.NotificationService;
import com.tutoring.util.BookingValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student user who can book tutoring sessions.
 * Extends User with booking-specific operations.
 */
public class Student extends User {

    // BR1: Maximum 2 active appointments per week
    public static final int MAX_ACTIVE_APPOINTMENTS = 2;

    private List<Appointment> appointments;

    public Student(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.appointments = new ArrayList<>();
    }

    /**
     * Searches for tutors based on course and availability criteria.
     *
     * @param course       the course subject to search for
     * @param availability the requested availability (e.g., "Monday 10AM")
     * @param allTutors    the full list of tutors in the system
     * @return list of matching tutors
     */
    public List<Tutor> searchTutor(String course, String availability, List<Tutor> allTutors) {
        List<Tutor> results = new ArrayList<>();
        for (Tutor tutor : allTutors) {
            if (tutor.isActive() && tutor.getCourses().contains(course)) {
                results.add(tutor);
            }
        }
        System.out.println("[SEARCH] Found " + results.size() + " tutor(s) for course: " + course);
        return results;
    }

    /**
     * Submits an appointment request to a tutor.
     * Enforces BR1 (booking limit) before proceeding.
     *
     * @param tutor    the selected tutor
     * @param course   the course for the session
     * @param slot     the requested time slot
     * @param mode     the meeting mode (In-Person or Virtual)
     * @param notes    optional notes for the tutor
     * @param notifier the notification service
     * @return the created Appointment, or null if rejected
     */
    public Appointment requestAppointment(Tutor tutor, Course course, String slot,
                                          String mode, String notes,
                                          NotificationService notifier) {

        // BR1: Check booking limit (<<include>>)
        if (!BookingValidator.checkBookingLimit(this)) {
            System.out.println("[BOOKING] Request rejected: weekly limit of "
                    + MAX_ACTIVE_APPOINTMENTS + " appointments reached.");
            return null;
        }

        // Check tutor availability
        if (!tutor.isSlotAvailable(slot)) {
            System.out.println("[BOOKING] Slot not available: " + slot);
            return null;
        }

        // Create appointment and notify tutor
        String apptId = "APPT-" + System.currentTimeMillis();
        Appointment appointment = new Appointment(apptId, this, tutor, course, slot, mode, notes);

        System.out.println("[BOOKING] Appointment request sent to tutor: " + tutor.getName());
        notifier.notifyTutorRequest(tutor, appointment);

        // Tutor accepts or declines
        boolean accepted = tutor.acceptRequest(appointment);

        if (accepted) {
            appointment.setStatus(Appointment.Status.CONFIRMED);
            appointments.add(appointment);
            notifier.sendConfirmation(this, appointment);
            System.out.println("[BOOKING] Appointment confirmed: " + apptId);
            return appointment;
        } else {
            // BR3: Tutor declines → slot becomes available again
            appointment.setStatus(Appointment.Status.DECLINED);
            tutor.makeSlotAvailable(slot);
            System.out.println("[BOOKING] Appointment declined by tutor. Slot released.");
            return null;
        }
    }

    /**
     * Cancels an existing appointment.
     * May apply BR2 (late cancellation policy) conditionally.
     *
     * @param appointment the appointment to cancel
     * @param notifier    the notification service
     */
    public void cancelAppointment(Appointment appointment, NotificationService notifier) {
        if (!appointments.contains(appointment)) {
            System.out.println("[CANCEL] Appointment not found for this student.");
            return;
        }

        // BR2: Check late cancellation rule (<<extend>>)
        if (BookingValidator.isLateCancellation(appointment)) {
            System.out.println("[CANCEL] Late cancellation flagged (within 6 hours of session).");
            appointment.markLateCancellation();
        }

        appointment.cancel();
        appointments.remove(appointment);
        notifier.sendCancellation(this, appointment);
        notifier.sendCancellation(appointment.getTutor(), appointment);
        System.out.println("[CANCEL] Appointment cancelled: " + appointment.getAppointmentId());
    }

    /**
     * Reschedules an appointment to a new time slot.
     * Validates the new slot availability before updating.
     *
     * @param appointment the appointment to reschedule
     * @param newSlot     the new requested time slot
     * @param notifier    the notification service
     */
    public void rescheduleAppointment(Appointment appointment, String newSlot,
                                      NotificationService notifier) {
        if (!appointments.contains(appointment)) {
            System.out.println("[RESCHEDULE] Appointment not found.");
            return;
        }

        // BR2: Late reschedule check
        if (BookingValidator.isLateCancellation(appointment)) {
            System.out.println("[RESCHEDULE] Warning: rescheduling within 6 hours of session.");
        }

        // Validate new slot availability (<<include>>)
        Tutor tutor = appointment.getTutor();
        if (!tutor.isSlotAvailable(newSlot)) {
            System.out.println("[RESCHEDULE] New slot not available: " + newSlot);
            return;
        }

        String oldSlot = appointment.getDateTime();
        appointment.reschedule(newSlot);
        tutor.makeSlotAvailable(oldSlot);
        tutor.blockSlot(newSlot);

        notifier.sendCancellation(appointment.getTutor(), appointment);
        System.out.println("[RESCHEDULE] Appointment rescheduled to: " + newSlot);
    }

    /**
     * Rates a tutor after a completed session.
     * Only applies if the session has been marked completed (BR4).
     *
     * @param tutor  the tutor to rate
     * @param rating the rating value (1.0 to 5.0)
     */
    public void rateTutor(Tutor tutor, double rating) {
        // BR4: Rating only allowed if session is completed (<<extend>>)
        boolean hasCompletedSession = appointments.stream()
                .anyMatch(a -> a.getTutor().equals(tutor)
                        && a.getStatus() == Appointment.Status.COMPLETED);

        if (!hasCompletedSession) {
            System.out.println("[RATING] Cannot rate: no completed session found with " + tutor.getName());
            return;
        }

        if (rating < 1.0 || rating > 5.0) {
            System.out.println("[RATING] Invalid rating. Must be between 1.0 and 5.0.");
            return;
        }

        tutor.addRating(rating);
        System.out.println("[RATING] Rated " + tutor.getName() + ": " + rating + " stars.");
    }

    // --- Getters ---

    public List<Appointment> getAppointments() { return appointments; }

    public long getActiveAppointmentCount() {
        return appointments.stream()
                .filter(a -> a.getStatus() == Appointment.Status.CONFIRMED
                        || a.getStatus() == Appointment.Status.PENDING)
                .count();
    }
}

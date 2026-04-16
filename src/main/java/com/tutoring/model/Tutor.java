package main.java.com.tutoring.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tutor who provides academic sessions.
 * Extends User with session-management operations.
 */
public class Tutor extends User {

    private String bio;
    private double rating;
    private int ratingCount;
    private List<String> courses;
    private List<Availability> availabilitySlots;

    public Tutor(String userId, String name, String email, String password, String bio) {
        super(userId, name, email, password);
        this.bio = bio;
        this.rating = 0.0;
        this.ratingCount = 0;
        this.courses = new ArrayList<>();
        this.availabilitySlots = new ArrayList<>();
    }

    /**
     * Adds or updates availability slots.
     *
     * @param timeSlot the time slot string (e.g., "Monday 10:00 AM")
     */
    public void setAvailability(String timeSlot) {
        Availability avail = new Availability(
                "AV-" + System.currentTimeMillis(), timeSlot, true);
        availabilitySlots.add(avail);
        System.out.println("[AVAILABILITY] Slot added for " + name + ": " + timeSlot);
    }

    /**
     * Accepts an incoming appointment request.
     *
     * @param appointment the appointment to evaluate
     * @return true if accepted
     */
    public boolean acceptRequest(Appointment appointment) {
        // Simulated acceptance logic — in a real system this would be async/UI driven
        System.out.println("[TUTOR] " + name + " accepted appointment: "
                + appointment.getAppointmentId());
        blockSlot(appointment.getDateTime());
        return true;
    }

    /**
     * Declines an appointment request.
     * Triggers BR3: the slot is released back as available.
     *
     * @param appointment the appointment to decline
     */
    public void declineRequest(Appointment appointment) {
        System.out.println("[TUTOR] " + name + " declined appointment: "
                + appointment.getAppointmentId());
        // BR3: slot becomes available again — handled via makeSlotAvailable()
        makeSlotAvailable(appointment.getDateTime());
    }

    /**
     * Marks a tutoring session as completed.
     *
     * @param appointment the completed appointment
     */
    public void markCompleted(Appointment appointment) {
        appointment.setStatus(Appointment.Status.COMPLETED);
        System.out.println("[SESSION] Session marked complete: " + appointment.getAppointmentId());
    }

    /**
     * Checks whether a given time slot is available.
     *
     * @param timeSlot the slot to check
     * @return true if the slot exists and is available
     */
    public boolean isSlotAvailable(String timeSlot) {
        return availabilitySlots.stream()
                .anyMatch(a -> a.getTimeSlot().equals(timeSlot) && a.isAvailable());
    }

    /**
     * Marks a slot as unavailable (booked).
     *
     * @param timeSlot the slot to block
     */
    public void blockSlot(String timeSlot) {
        availabilitySlots.stream()
                .filter(a -> a.getTimeSlot().equals(timeSlot))
                .findFirst()
                .ifPresent(a -> a.setAvailable(false));
    }

    /**
     * Releases a slot back to available state (BR3).
     *
     * @param timeSlot the slot to free
     */
    public void makeSlotAvailable(String timeSlot) {
        availabilitySlots.stream()
                .filter(a -> a.getTimeSlot().equals(timeSlot))
                .findFirst()
                .ifPresent(a -> {
                    a.setAvailable(true);
                    System.out.println("[AVAILABILITY] Slot released: " + timeSlot);
                });
    }

    /**
     * Adds a new course to this tutor's offered subjects.
     *
     * @param courseName the course to add
     */
    public void addCourse(String courseName) {
        if (!courses.contains(courseName)) {
            courses.add(courseName);
        }
    }

    /**
     * Updates the tutor's cumulative rating.
     *
     * @param newRating the new rating to incorporate
     */
    public void addRating(double newRating) {
        this.rating = ((this.rating * this.ratingCount) + newRating) / (this.ratingCount + 1);
        this.ratingCount++;
    }

    // --- Getters ---

    public String getBio() { return bio; }
    public double getRating() { return Math.round(rating * 10.0) / 10.0; }
    public List<String> getCourses() { return courses; }
    public List<Availability> getAvailabilitySlots() { return availabilitySlots; }

    @Override
    public String toString() {
        return name + " | Rating: " + getRating() + " | Courses: " + courses;
    }
}

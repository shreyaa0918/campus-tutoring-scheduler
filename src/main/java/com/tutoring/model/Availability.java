package main.java.com.tutoring.model;

/**
 * Models a tutor's availability time slot.
 * Each tutor can have multiple availability entries.
 */
public class Availability {

    private String availabilityId;
    private String timeSlot;
    private boolean isAvailable;

    public Availability(String availabilityId, String timeSlot, boolean isAvailable) {
        this.availabilityId = availabilityId;
        this.timeSlot = timeSlot;
        this.isAvailable = isAvailable;
    }

    public String getAvailabilityId() { return availabilityId; }
    public String getTimeSlot() { return timeSlot; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    @Override
    public String toString() {
        return timeSlot + " [" + (isAvailable ? "Available" : "Booked") + "]";
    }
}

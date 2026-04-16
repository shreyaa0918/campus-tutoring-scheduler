package com.tutoring.model;

/**
 * Base class representing a system user.
 * Extended by Student and Tutor via inheritance.
 */
public abstract class User {

    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected boolean isActive;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = true;
    }

    /**
     * Authenticates the user by verifying password.
     *
     * @param inputPassword the password entered by the user
     * @return true if authentication succeeds
     */
    public boolean login(String inputPassword) {
        if (!isActive) {
            System.out.println("[AUTH] Account is deactivated: " + email);
            return false;
        }
        if (this.password.equals(inputPassword)) {
            System.out.println("[AUTH] Login successful: " + email);
            return true;
        }
        System.out.println("[AUTH] Login failed: incorrect password for " + email);
        return false;
    }

    /**
     * Logs out the current user session.
     */
    public void logout() {
        System.out.println("[AUTH] User logged out: " + email);
    }

    /**
     * Resets the user's password.
     *
     * @param newPassword the new password to set
     */
    public void resetPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("[AUTH] Password reset for: " + email);
    }

    // --- Getters and Setters ---

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}

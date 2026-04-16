package com.tutoring.model;

/**
 * Represents an academic course or subject offered for tutoring.
 */
public class Course {

    private String courseId;
    private String name;
    private String topic;

    public Course(String courseId, String name, String topic) {
        this.courseId = courseId;
        this.name = name;
        this.topic = topic;
    }

    public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public String getTopic() { return topic; }

    @Override
    public String toString() {
        return name + " (" + topic + ")";
    }
}

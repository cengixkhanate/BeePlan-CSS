package pro_bee_css;
import java.io.*;
import java.util.*;


// Represents a course with its details
class Course {
    String name;
    int theoreticalHours;
    int practicalHours;
    String instructor;
    List<TimeSlot> timeSlots;
    boolean isCommonCourse;

    public Course(String name, int theoreticalHours, int practicalHours, 
                  String instructor, boolean isCommonCourse) {
        this.name = name;
        this.theoreticalHours = theoreticalHours;
        this.practicalHours = practicalHours;
        this.instructor = instructor;
        this.timeSlots = new ArrayList<>();
        this.isCommonCourse = isCommonCourse;
    }

    public void addTimeSlot(TimeSlot slot) {
        timeSlots.add(slot);
    }
}
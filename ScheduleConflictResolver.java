package pro_bee_css;
import java.io.*;
import java.util.*;

class ScheduleConflictResolver {
    private List<Course> courses;
    private Map<String, List<TimeSlot>> instructorSchedules;
    private Map<String, List<TimeSlot>> classroomSchedules;

    public ScheduleConflictResolver(List<Course> courses) {
        this.courses = courses;
        this.instructorSchedules = new HashMap<>();
        this.classroomSchedules = new HashMap<>();
    }

    public boolean resolveSchedule() {
        // Sort courses to prioritize common courses
        courses.sort((c1, c2) -> Boolean.compare(c2.isCommonCourse, c1.isCommonCourse));

        for (Course course : courses) {
            if (!scheduleCourse(course)) {
                System.out.println("Could not schedule course: " + course.name);
                return false;
            }
        }
        return true;
    }

    private boolean scheduleCourse(Course course) {
        // Define possible days and time slots
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int[] possibleStartHours = {9, 10, 11, 13, 14, 15, 16};
        List<String> classrooms = course.isCommonCourse ? 
            Arrays.asList("Amfi-1", "Amfi-2", "Amfi-3", "Amfi-4", "Amfi-5", "Amfi-6") :
            Arrays.asList("B01", "B02", "B03", "B04", "B05", 
                          "A01", "A02", "A03", "A04", "A05",
                          "101", "102", "103", "104", "105");

        // Try to schedule theoretical hours
        TimeSlot theoreticalSlot = findAvailableSlot(course, days, possibleStartHours, 
                                                     classrooms, false, course.theoreticalHours);
        if (theoreticalSlot == null) return false;
        course.addTimeSlot(theoreticalSlot);

        // Try to schedule practical hours (if any)
        if (course.practicalHours > 0) {
            // Ensure practical hours are after theoretical
            int[] practicalStartHours = Arrays.stream(possibleStartHours)
                .filter(h -> h > theoreticalSlot.startHour + theoreticalSlot.duration)
                .toArray();

            TimeSlot practicalSlot = findAvailableSlot(course, days, practicalStartHours, 
                                                       classrooms, true, course.practicalHours);
            if (practicalSlot == null) return false;
            course.addTimeSlot(practicalSlot);
        }

        return true;
    }

    private TimeSlot findAvailableSlot(Course course, String[] days, int[] startHours, 
                                       List<String> classrooms, boolean isPractical, int duration) {
        for (String day : days) {
            for (int startHour : startHours) {
                for (String classroom : classrooms) {
                    TimeSlot potentialSlot = new TimeSlot(day, startHour, duration, classroom, isPractical);
                    
                    // Check instructor availability
                    if (!isInstructorAvailable(course.instructor, potentialSlot)) continue;
                    
                    // Check classroom availability
                    if (!isClassroomAvailable(classroom, potentialSlot)) continue;
                    
                    // Avoid scheduling on Friday between 13:20 and 15:10
                    if (day.equals("Friday") && startHour >= 13 && startHour < 15) continue;
                    
                    // Record the slot
                    recordSlot(course.instructor, classroom, potentialSlot);
                    return potentialSlot;
                }
            }
        }
        return null;
    }

    private boolean isInstructorAvailable(String instructor, TimeSlot slot) {
        List<TimeSlot> instructorSchedule = instructorSchedules.getOrDefault(instructor, new ArrayList<>());
        return instructorSchedule.stream().noneMatch(s -> s.conflictsWith(slot));
    }

    private boolean isClassroomAvailable(String classroom, TimeSlot slot) {
        List<TimeSlot> classroomSchedule = classroomSchedules.getOrDefault(classroom, new ArrayList<>());
        return classroomSchedule.stream().noneMatch(s -> s.conflictsWith(slot));
    }

    private void recordSlot(String instructor, String classroom, TimeSlot slot) {
        instructorSchedules.computeIfAbsent(instructor, k -> new ArrayList<>()).add(slot);
        classroomSchedules.computeIfAbsent(classroom, k -> new ArrayList<>()).add(slot);
    }

    // Print the final schedule
    public void printSchedule() {
        courses.forEach(course -> {
            System.out.println("\nCourse: " + course.name);
            course.timeSlots.forEach(slot -> 
                System.out.printf("  %s: %s hours at %s on %s\n", 
                    slot.isPractical ? "Practical" : "Theoretical", 
                    slot.duration, slot.classroom, slot.day)
            );
        });
    }
}


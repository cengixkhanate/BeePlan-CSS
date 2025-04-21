package pro_bee_css;
import java.io.*;
import java.util.*;

public class BeePlanScheduler {
    public static void main(String[] args) {
        // Read common and curriculum courses
        List<Course> commonCourses = readCommonCourses("common_courses.txt");
        List<Course> curriculumCourses = readCurriculumCourses("curriculum_courses.txt");

        // Combine and process all courses
        List<Course> allCourses = new ArrayList<>();
        allCourses.addAll(commonCourses);
        allCourses.addAll(curriculumCourses);

        // Resolve scheduling conflicts
        ScheduleConflictResolver resolver = new ScheduleConflictResolver(allCourses);
        
        if (resolver.resolveSchedule()) {
            System.out.println("Schedule successfully created!");
            resolver.printSchedule();
        } else {
            System.out.println("Could not create a conflict-free schedule.");
        }
    }

    // Method to read common courses (simplified for brevity)
    private static List<Course> readCommonCourses(String filename) {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            br.readLine(); // Skip separator

            String line;
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.split("\\|");
                Course course = new Course(
                    parts[0].trim(), 
                    Integer.parseInt(parts[1].trim()), 
                    Integer.parseInt(parts[2].trim()), 
                    parts[3].trim(),
                    true // Mark as common course
                );
                courses.add(course);
            }
        } catch (IOException e) {
            System.err.println("Error reading common courses: " + e.getMessage());
        }
        return courses;
    }

    // Method to read curriculum courses (simplified for brevity)
    private static List<Course> readCurriculumCourses(String filename) {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            br.readLine(); // Skip separator

            String line;
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.split("\\|");
                Course course = new Course(
                    parts[0].trim(), 
                    Integer.parseInt(parts[1].trim()), 
                    Integer.parseInt(parts[2].trim()), 
                    parts[3].trim(),
                    false // Mark as curriculum course
                );
                courses.add(course);
            }
        } catch (IOException e) {
            System.err.println("Error reading curriculum courses: " + e.getMessage());
        }
        return courses;
    }
}

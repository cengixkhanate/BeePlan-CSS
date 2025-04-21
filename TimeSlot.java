package pro_bee_css;

class TimeSlot {
    String day;
    int startHour;
    int duration;
    String classroom;
    boolean isPractical;

    public TimeSlot(String day, int startHour, int duration, 
                    String classroom, boolean isPractical) {
        this.day = day;
        this.startHour = startHour;
        this.duration = duration;
        this.classroom = classroom;
        this.isPractical = isPractical;
    }

    // Check for time slot conflicts
    public boolean conflictsWith(TimeSlot other) {
        if (!this.day.equals(other.day)) return false;
        
        int thisEnd = this.startHour + this.duration;
        int otherEnd = other.startHour + other.duration;
        
        return !(this.startHour >= otherEnd || otherEnd <= this.startHour);
    }
}

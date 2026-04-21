package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 */

public class CourseOpening {
    String code, year, semester, lecturers;

    public CourseOpening(String code, String year, String semester, String lecturers) {
        this.code = code;
        this.year = year;
        this.semester = semester;
        this.lecturers = lecturers;
    }

    public String getCourseCode() { return code; }
    public String getYear() { return year; }
    public String getSemester() { return semester; }
    public String getLecturers() { return lecturers; }
}
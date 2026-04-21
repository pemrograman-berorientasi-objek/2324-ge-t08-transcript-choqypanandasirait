package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 */

public class Enrollment {
    String code, studentId, year, semester;
    String grade;
    String remedial;

    public Enrollment(String code, String studentId, String year, String semester) {
        this.code = code;
        this.studentId = studentId;
        this.year = year;
        this.semester = semester;
    }

    public boolean match(String a, String b, String c, String d) {
        return code.equals(a) && studentId.equals(b) && year.equals(c) && semester.equals(d);
    }

    public boolean sameOpening(CourseOpening co) {
        return code.equals(co.getCourseCode()) &&
               year.equals(co.getYear()) &&
               semester.equals(co.getSemester());
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setRemedial(String remedial) {
        this.remedial = remedial;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return code; }
    public String getYear() { return year; }
    public String getGrade() { return grade; }

    public String finalGrade() {
        return (remedial != null) ? remedial : grade;
    }

    public String toString() {
        if (remedial != null)
            return code + "|" + studentId + "|" + year + "|" + semester + "|" + remedial + "(" + grade + ")";
        return code + "|" + studentId + "|" + year + "|" + semester + "|" + grade;
    }
}
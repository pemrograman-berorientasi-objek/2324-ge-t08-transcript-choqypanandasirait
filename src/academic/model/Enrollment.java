// ==============================
// FILE: academic/model/Enrollment.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 */
/**
 * Menyimpan pengambilan matkul mahasiswa
 */
public class Enrollment {

    String code, studentId, year, semester;
    String grade;
    String remedial;

    public Enrollment(String code, String studentId,
                      String year, String semester) {

        this.code = code;
        this.studentId = studentId;
        this.year = year;
        this.semester = semester;
    }

    // cek data sama atau tidak
    public boolean match(String a, String b, String c, String d) {
        return code.equals(a)
                && studentId.equals(b)
                && year.equals(c)
                && semester.equals(d);
    }

    // cocok dengan opening
    public boolean sameOpening(CourseOpening co) {
        return code.equals(co.getCourseCode())
                && year.equals(co.getYear())
                && semester.equals(co.getSemester());
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setRemedial(String remedial) {
        this.remedial = remedial;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return code;
    }

    public String getYear() {
        return year;
    }

    public String getSemester() {
        return semester;
    }

    public String getGrade() {
        return grade;
    }

    // nilai akhir
    public String finalGrade() {
        if (remedial != null) {
            return remedial;
        }
        return grade;
    }

    public String toString() {

        if (remedial != null) {
            return code + "|" + studentId + "|" +
                   year + "|" + semester + "|" +
                   remedial + "(" + grade + ")";
        }

        return code + "|" + studentId + "|" +
               year + "|" + semester + "|" + grade;
    }
}
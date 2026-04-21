package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 */

public class Course {
    String code, name, grade;
    int credit;

    public Course(String code, String name, int credit, String grade) {
        this.code = code;
        this.name = name;
        this.credit = credit;
        this.grade = grade;
    }

    public String getCode() {
        return code;
    }

    public int getCredit() {
        return credit;
    }

    public String toString() {
        return code + "|" + name + "|" + credit + "|" + grade;
    }
}
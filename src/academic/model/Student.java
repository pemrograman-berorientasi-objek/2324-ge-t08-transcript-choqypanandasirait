package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 */

public class Student {
    String id, name, year, studyProgram;

    public Student(String id, String name, String year, String studyProgram) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.studyProgram = studyProgram;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        return id + "|" + name + "|" + year + "|" + studyProgram;
    }
}
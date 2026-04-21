package academic.driver;

import java.util.*;
import academic.model.*;

/**
 * @author 12S24012 Choqy Pananda Sirait
 */
public class Driver1 {

    public static void main(String[] _args) {
        Scanner input = new Scanner(System.in);

        ArrayList<Lecturer> lecturers = new ArrayList<>();
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<CourseOpening> openings = new ArrayList<>();
        ArrayList<Enrollment> enrollments = new ArrayList<>();

        while (true) {
            String line = input.nextLine();

            if (line.equals("---")) {
                break;
            }

            String[] data = line.split("#");

            switch (data[0]) {

                case "lecturer-add":
                    lecturers.add(new Lecturer(data[1], data[2], data[3], data[4], data[5]));
                    break;

                case "course-add":
                    courses.add(new Course(data[1], data[2], Integer.parseInt(data[3]), data[4]));
                    break;

                case "student-add":
                    students.add(new Student(data[1], data[2], data[3], data[4]));
                    break;

                case "course-open":
                    openings.add(new CourseOpening(data[1], data[2], data[3], data[4]));
                    break;

                case "enrollment-add":
                    enrollments.add(new Enrollment(data[1], data[2], data[3], data[4]));
                    break;

                case "enrollment-grade":
                    for (Enrollment e : enrollments) {
                        if (e.match(data[1], data[2], data[3], data[4])) {
                            e.setGrade(data[5]);
                        }
                    }
                    break;

                case "enrollment-remedial":
                    for (Enrollment e : enrollments) {
                        if (e.match(data[1], data[2], data[3], data[4])) {
                            e.setRemedial(data[5]);
                        }
                    }
                    break;

                case "student-details":
                    for (Student s : students) {
                        if (s.getId().equals(data[1])) {
                            double total = 0;
                            int sks = 0;

                            for (Enrollment e : enrollments) {
                                if (e.getStudentId().equals(s.getId())) {
                                    Course c = findCourse(courses, e.getCourseCode());
                                    if (c != null && e.getGrade() != null) {
                                        total += convert(e.finalGrade()) * c.getCredit();
                                        sks += c.getCredit();
                                    }
                                }
                            }

                            double gpa = (sks == 0) ? 0 : total / sks;
                            System.out.printf("%s|%.2f|%d\n", s.toString(), gpa, sks);
                        }
                    }
                    break;

                case "course-history":
                    for (CourseOpening co : openings) {
                        if (co.getCourseCode().equals(data[1])) {
                            Course c = findCourse(courses, co.getCourseCode());

                            String lecturerText = "";
                            String[] initials = co.getLecturers().split(",");

                            for (int i = 0; i < initials.length; i++) {
                                Lecturer lec = findLecturer(lecturers, initials[i]);
                                lecturerText += lec.getInitial() + " (" + lec.getEmail() + ")";
                                if (i != initials.length - 1) lecturerText += ",";
                            }

                            System.out.println(c.toString() + "|" + co.getYear() + "|" + co.getSemester() + "|" + lecturerText);

                            for (Enrollment e : enrollments) {
                                if (e.sameOpening(co)) {
                                    System.out.println(e.toString());
                                }
                            }
                        }
                    }
                    break;

                case "student-transcript":
                    for (Student s : students) {
                        if (s.getId().equals(data[1])) {

                            HashMap<String, Enrollment> latest = new HashMap<>();

                            for (Enrollment e : enrollments) {
                                if (e.getStudentId().equals(s.getId()) && e.getGrade() != null) {
                                    latest.put(e.getCourseCode(), e);
                                }
                            }

                            double total = 0;
                            int sks = 0;

                            for (Enrollment e : latest.values()) {
                                Course c = findCourse(courses, e.getCourseCode());
                                total += convert(e.finalGrade()) * c.getCredit();
                                sks += c.getCredit();
                            }

                            double gpa = total / sks;

                            System.out.printf("%s|%.2f|%d\n", s.toString(), gpa, sks);

                            ArrayList<Enrollment> list = new ArrayList<>(latest.values());

                            Collections.sort(list, new Comparator<Enrollment>() {
                                public int compare(Enrollment a, Enrollment b) {
                                    return a.getYear().compareTo(b.getYear());
                                }
                            });

                            for (Enrollment e : list) {
                                System.out.println(e.toString());
                            }
                        }
                    }
                    break;
            }
        }

        for (Lecturer l : lecturers) System.out.println(l);
        for (Course c : courses) System.out.println(c);
        for (Student s : students) System.out.println(s);
        for (Enrollment e : enrollments) System.out.println(e);

        input.close();
    }

    static Course findCourse(ArrayList<Course> list, String code) {
        for (Course c : list)
            if (c.getCode().equals(code)) return c;
        return null;
    }

    static Lecturer findLecturer(ArrayList<Lecturer> list, String init) {
        for (Lecturer l : list)
            if (l.getInitial().equals(init)) return l;
        return null;
    }

    static double convert(String g) {
        switch (g) {
            case "A": return 4;
            case "AB": return 3.5;
            case "B": return 3;
            case "BC": return 2.5;
            case "C": return 2;
            case "D": return 1;
            default: return 0;
        }
    }

    static class Lecturer {
        String id, name, initial, email, study;

        Lecturer(String a, String b, String c, String d, String e) {
            id = a; name = b; initial = c; email = d; study = e;
        }

        public String getInitial() { return initial; }
        public String getEmail() { return email; }

        public String toString() {
            return id + "|" + name + "|" + initial + "|" + email + "|" + study;
        }
    }
}
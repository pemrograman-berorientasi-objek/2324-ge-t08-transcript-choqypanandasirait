package academic.driver;

import java.util.*;
import academic.model.*;

/**
 * Main driver program
 * membaca command dari input
 */
public class Driver1 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Menyimpan seluruh data
        ArrayList<Lecturer> lecturers = new ArrayList<>();
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<CourseOpening> openings = new ArrayList<>();
        ArrayList<Enrollment> enrollments = new ArrayList<>();

        // membaca terus sampai ---
        while (true) {

            String line = input.nextLine();

            if (line.equals("---")) {
                break;
            }

            // pisahkan command dengan #
            String[] data = line.split("#");

            switch (data[0]) {

                // tambah dosen
                case "lecturer-add":
                    lecturers.add(
                        new Lecturer(
                            data[1], data[2], data[3], data[4], data[5]
                        )
                    );
                    break;

                // tambah matkul
                case "course-add":
                    courses.add(
                        new Course(
                            data[1],
                            data[2],
                            Integer.parseInt(data[3]),
                            data[4]
                        )
                    );
                    break;

                // tambah mahasiswa
                case "student-add":
                    students.add(
                        new Student(
                            data[1], data[2], data[3], data[4]
                        )
                    );
                    break;

                // buka kelas
                case "course-open":
                    openings.add(
                        new CourseOpening(
                            data[1], data[2], data[3], data[4]
                        )
                    );
                    break;

                // tambah enrollment
                case "enrollment-add":
                    enrollments.add(
                        new Enrollment(
                            data[1], data[2], data[3], data[4]
                        )
                    );
                    break;

                // input nilai
                case "enrollment-grade":
                    for (Enrollment e : enrollments) {
                        if (e.match(data[1], data[2], data[3], data[4])) {
                            e.setGrade(data[5]);
                        }
                    }
                    break;

                // remedial
                case "enrollment-remedial":
                    for (Enrollment e : enrollments) {
                        if (e.match(data[1], data[2], data[3], data[4])) {
                            e.setRemedial(data[5]);
                        }
                    }
                    break;

                // detail mahasiswa
                case "student-details":
                    showStudentDetail(data[1], students, courses, enrollments);
                    break;

                // riwayat course
                case "course-history":
                    showCourseHistory(data[1], openings, courses, lecturers, enrollments);
                    break;

                // transcript mahasiswa
                case "student-transcript":
                    showTranscript(data[1], students, courses, enrollments);
                    break;
            }
        }

        // output seluruh data akhir
        for (Lecturer l : lecturers) {
            System.out.println(l);
        }

        for (Course c : courses) {
            System.out.println(c);
        }

        for (Student s : students) {
            System.out.println(s);
        }

        for (Enrollment e : enrollments) {
            System.out.println(e);
        }

        input.close();
    }

    /**
     * menampilkan student details
     */
    static void showStudentDetail(
        String id,
        ArrayList<Student> students,
        ArrayList<Course> courses,
        ArrayList<Enrollment> enrollments
    ) {

        for (Student s : students) {

            if (s.getId().equals(id)) {

                double total = 0;
                int sks = 0;

                for (Enrollment e : enrollments) {

                    if (e.getStudentId().equals(id) && e.getGrade() != null) {

                        Course c = findCourse(courses, e.getCourseCode());

                        total += convert(e.finalGrade()) * c.getCredit();
                        sks += c.getCredit();
                    }
                }

                double gpa = (sks == 0) ? 0 : total / sks;

                System.out.printf("%s|%.2f|%d\n", s, gpa, sks);
            }
        }
    }

    /**
     * transcript = ambil course terakhir
     */
    static void showTranscript(
        String id,
        ArrayList<Student> students,
        ArrayList<Course> courses,
        ArrayList<Enrollment> enrollments
    ) {

        for (Student s : students) {

            if (s.getId().equals(id)) {

                ArrayList<Enrollment> latest = new ArrayList<>();

                // cek setiap course
                for (Course c : courses) {

                    Enrollment last = null;

                    // ambil enrollment terakhir
                    for (Enrollment e : enrollments) {

                        if (e.getStudentId().equals(id)
                                && e.getCourseCode().equals(c.getCode())
                                && e.getGrade() != null) {

                            last = e;
                        }
                    }

                    if (last != null) {
                        latest.add(last);
                    }
                }

                double total = 0;
                int sks = 0;

                for (Enrollment e : latest) {

                    Course c = findCourse(courses, e.getCourseCode());

                    total += convert(e.finalGrade()) * c.getCredit();
                    sks += c.getCredit();
                }

                double gpa = total / sks;

                System.out.printf("%s|%.2f|%d\n", s, gpa, sks);

                for (Enrollment e : latest) {
                    System.out.println(e);
                }
            }
        }
    }

    /**
     * history matkul
     */
    static void showCourseHistory(
        String code,
        ArrayList<CourseOpening> openings,
        ArrayList<Course> courses,
        ArrayList<Lecturer> lecturers,
        ArrayList<Enrollment> enrollments
    ) {

        for (CourseOpening co : openings) {

            if (co.getCourseCode().equals(code)) {

                Course c = findCourse(courses, code);

                String text = "";
                String[] arr = co.getLecturers().split(",");

                for (int i = 0; i < arr.length; i++) {

                    Lecturer l = findLecturer(lecturers, arr[i]);

                    text += l.getInitial() + " (" + l.getEmail() + ")";

                    if (i != arr.length - 1) {
                        text += ",";
                    }
                }

                System.out.println(
                    c + "|" +
                    co.getYear() + "|" +
                    co.getSemester() + "|" +
                    text
                );

                for (Enrollment e : enrollments) {
                    if (e.sameOpening(co)) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

    /**
     * cari course
     */
    static Course findCourse(ArrayList<Course> list, String code) {
        for (Course c : list) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        return null;
    }

    /**
     * cari dosen
     */
    static Lecturer findLecturer(ArrayList<Lecturer> list, String init) {
        for (Lecturer l : list) {
            if (l.getInitial().equals(init)) {
                return l;
            }
        }
        return null;
    }

    /**
     * konversi nilai huruf ke angka
     */
    static double convert(String g) {

        switch (g) {
            case "A": return 4.0;
            case "AB": return 3.5;
            case "B": return 3.0;
            case "BC": return 2.5;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0;
        }
    }

    /**
     * Nested Class (syarat tugas)
     */
    static class Lecturer {

        String id, name, initial, email, study;

        Lecturer(String id, String name, String initial,
                 String email, String study) {

            this.id = id;
            this.name = name;
            this.initial = initial;
            this.email = email;
            this.study = study;
        }

        String getInitial() {
            return initial;
        }

        String getEmail() {
            return email;
        }

        public String toString() {
            return id + "|" + name + "|" +
                   initial + "|" + email + "|" + study;
        }
    }
}
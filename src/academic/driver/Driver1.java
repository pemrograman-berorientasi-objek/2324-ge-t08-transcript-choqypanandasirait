// ==============================
// FILE: academic/driver/Driver1.java
// ==============================
package academic.driver;

import java.util.*;
import academic.model.*;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Main program Academic Simulator — T08 Transcript.
 *
 * Nested Construct yang digunakan: STATIC NESTED CLASS (Lecturer)
 * → Lecturer tidak butuh akses ke instance Driver1, cukup static nested.
 * → Ini memenuhi syarat tugas "aplikasikan salah satu bentuk Nested Constructs".
 */
public class Driver1 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Daftar semua data yang tersimpan selama program berjalan
        ArrayList<Lecturer>       lecturers   = new ArrayList<>();
        ArrayList<Course>         courses     = new ArrayList<>();
        ArrayList<Student>        students    = new ArrayList<>();
        ArrayList<CourseOpening>  openings    = new ArrayList<>();
        ArrayList<Enrollment>     enrollments = new ArrayList<>();

        // Baca input baris per baris sampai ketemu "---"
        while (true) {

            String line = input.nextLine();

            // Tanda berhenti membaca input
            if (line.equals("---")) {
                break;
            }

            // Pecah baris berdasarkan karakter '#'
            // data[0] = perintah, data[1..n] = parameter
            String[] data = line.split("#");

            switch (data[0]) {

                case "lecturer-add":
                    // Tambah dosen baru ke list
                    // data[1]=id, data[2]=nama, data[3]=inisial, data[4]=email, data[5]=prodi
                    lecturers.add(new Lecturer(
                            data[1], data[2], data[3],
                            data[4], data[5]));
                    break;

                case "course-add":
                    // Tambah matkul baru ke list
                    // data[1]=kode, data[2]=nama, data[3]=sks, data[4]=nilai minimum
                    courses.add(new Course(
                            data[1], data[2],
                            Integer.parseInt(data[3]),
                            data[4]));
                    break;

                case "student-add":
                    // Tambah mahasiswa baru ke list
                    // data[1]=NIM, data[2]=nama, data[3]=angkatan, data[4]=prodi
                    students.add(new Student(
                            data[1], data[2],
                            data[3], data[4]));
                    break;

                case "course-open":
                    // Buka kelas baru untuk matkul tertentu
                    // data[1]=kode, data[2]=tahun, data[3]=semester, data[4]=dosen(,dosen)
                    openings.add(new CourseOpening(
                            data[1], data[2],
                            data[3], data[4]));
                    break;

                case "enrollment-add":
                    // Daftarkan mahasiswa ke suatu kelas
                    // data[1]=kode, data[2]=NIM, data[3]=tahun, data[4]=semester
                    enrollments.add(new Enrollment(
                            data[1], data[2],
                            data[3], data[4]));
                    break;

                case "enrollment-grade":
                    // Set nilai reguler untuk enrollment yang cocok
                    // data[1]=kode, data[2]=NIM, data[3]=tahun, data[4]=semester, data[5]=nilai
                    for (Enrollment e : enrollments) {
                        if (e.match(data[1], data[2], data[3], data[4])) {
                            e.setGrade(data[5]);
                        }
                    }
                    break;

                case "enrollment-remedial":
                    // Set nilai remedial (hanya jika grade sudah ada)
                    // data[1]=kode, data[2]=NIM, data[3]=tahun, data[4]=semester, data[5]=nilai remedial
                    for (Enrollment e : enrollments) {
                        // Syarat: enrollment cocok DAN sudah punya grade
                        // (remedial tidak bisa diberikan sebelum nilai reguler ada)
                        if (e.match(data[1], data[2], data[3], data[4])
                                && e.getGrade() != null) {
                            e.setRemedial(data[5]);
                        }
                    }
                    break;

                case "student-details":
                    // Tampilkan detail performa mahasiswa (semua enrollment yang sudah dinilai)
                    showStudentDetail(
                            data[1], students,
                            courses, enrollments);
                    break;

                case "course-history":
                    // Tampilkan riwayat pembukaan kelas suatu matkul
                    showCourseHistory(
                            data[1], openings,
                            courses, lecturers,
                            enrollments);
                    break;

                case "student-transcript":
                    // Tampilkan transkrip mahasiswa (hanya pengambilan terakhir per matkul)
                    showTranscript(
                            data[1], students,
                            courses, enrollments);
                    break;
            }
        }

        // ── Output akhir program (setelah "---") ──
        // Cetak semua dosen
        for (Lecturer l : lecturers)    System.out.println(l);
        // Cetak semua matkul
        for (Course c : courses)        System.out.println(c);
        // Cetak semua mahasiswa
        for (Student s : students)      System.out.println(s);
        // Cetak semua enrollment (sudah dinilai maupun belum)
        // Catatan: enrollment yang belum dinilai (grade==null) tetap dicetak
        // tapi formatnya akan null — perlu dicek apakah ini sesuai expected output.
        // Dari test case, semua enrollment yang dicetak sudah punya grade,
        // jadi kita cetak semua yang grade-nya tidak null.
        for (Enrollment e : enrollments) {
            // Hanya cetak enrollment yang sudah punya nilai
            if (e.getGrade() != null) {
                System.out.println(e);
            }
        }

        input.close();
    }

    // ============================================================
    // student-details
    // Menampilkan GPA kumulatif mahasiswa berdasarkan
    // SEMUA enrollment yang sudah dinilai (bukan hanya yang terbaru).
    //
    // Format: NIM|Nama|Angkatan|Prodi|GPA|TotalSKS
    // contoh: 12S20001|Marcelino Manalu|2020|Information Systems|3.00|7
    // ============================================================
    static void showStudentDetail(
            String id,
            ArrayList<Student> students,
            ArrayList<Course> courses,
            ArrayList<Enrollment> enrollments) {

        for (Student s : students) {

            // Cari mahasiswa yang NIM-nya cocok
            if (s.getId().equals(id)) {

                double total = 0; // akumulasi: bobot_nilai × SKS
                int    sks   = 0; // total SKS yang sudah ditempuh

                for (Enrollment e : enrollments) {

                    // Hitung SEMUA enrollment milik mahasiswa ini yang sudah dinilai
                    // (berbeda dengan transcript yang hanya ambil yang terakhir per matkul)
                    if (e.getStudentId().equals(id)
                            && e.getGrade() != null) {

                        // Cari matkul untuk mendapatkan jumlah SKS-nya
                        Course c = findCourse(courses, e.getCourseCode());

                        // Kalikan bobot nilai akhir dengan SKS matkul tersebut
                        // finalGrade() mengembalikan remedial jika ada, atau grade biasa
                        total += convert(e.finalGrade()) * c.getCredit();

                        // Tambahkan SKS matkul ke total SKS
                        sks += c.getCredit();
                    }
                }

                // Hitung GPA = total bobot / total SKS
                double gpa = (sks == 0) ? 0 : total / sks;

                // Cetak: format Student (toString) + |GPA|SKS
                // %.2f = dua angka desimal, contoh: 3.00
                System.out.printf("%s|%.2f|%d\n", s, gpa, sks);
            }
        }
    }

    // ============================================================
    // student-transcript
    // Menampilkan transkrip mahasiswa:
    // 1. Baris pertama: ringkasan GPA + SKS (hanya dari pengambilan TERAKHIR tiap matkul)
    // 2. Baris berikutnya: daftar enrollment terbaru tiap matkul, urut historis
    //
    // "Pengambilan terakhir" = enrollment dengan tahun/semester paling akhir
    // untuk matkul yang sama milik mahasiswa yang sama.
    //
    // Format ringkasan: NIM|Nama|Angkatan|Prodi|GPA|SKS
    // Format enrollment: kode|NIM|tahun|semester|nilai (atau nilai_remedial(nilai_lama))
    // ============================================================
    static void showTranscript(
            String id,
            ArrayList<Student> students,
            ArrayList<Course> courses,
            ArrayList<Enrollment> enrollments) {

        for (Student s : students) {

            // Cari mahasiswa yang NIM-nya cocok
            if (s.getId().equals(id)) {

                // List enrollment terpilih (satu per matkul — yang terbaru)
                ArrayList<Enrollment> list = new ArrayList<>();

                // Iterasi setiap matkul untuk mencari enrollment terbaru mahasiswa ini
                for (Course c : courses) {

                    Enrollment best = null; // enrollment terbaik (terbaru) untuk matkul ini

                    for (Enrollment e : enrollments) {

                        // Filter: hanya enrollment milik mahasiswa ini,
                        // untuk matkul c, dan yang sudah punya nilai
                        if (e.getStudentId().equals(id)
                                && e.getCourseCode().equals(c.getCode())
                                && e.getGrade() != null) {

                            if (best == null) {
                                // Enrollment pertama yang ditemukan → jadikan kandidat awal
                                best = e;
                            } else {
                                // Bandingkan tahun: ambil tahun awal dari "2020/2021" → 2020
                                int y1 = getYear(e.getYear());
                                int y2 = getYear(best.getYear());

                                if (y1 > y2) {
                                    // Tahun e lebih baru → ganti best
                                    best = e;
                                } else if (y1 == y2
                                        && semValue(e.getSemester())
                                           > semValue(best.getSemester())) {
                                    // Tahun sama tapi semester e lebih akhir
                                    // (even=2 > odd=1) → ganti best
                                    best = e;
                                }
                                // Jika e lebih lama → abaikan
                            }
                        }
                    }

                    // Jika ada enrollment yang ditemukan untuk matkul ini → tambahkan
                    if (best != null) {
                        list.add(best);
                    }
                }

                // Urutkan list secara historis:
                // matkul yang diselesaikan lebih awal muncul lebih dulu
                Collections.sort(list, new Comparator<Enrollment>() {
                    @Override
                    public int compare(Enrollment a, Enrollment b) {

                        int ya = getYear(a.getYear());
                        int yb = getYear(b.getYear());

                        // Bandingkan tahun dulu
                        if (ya != yb) {
                            return ya - yb; // ascending: yang lebih lama duluan
                        }

                        // Jika tahun sama → bandingkan semester
                        // odd(1) < even(2) → odd tampil lebih dulu
                        return semValue(a.getSemester()) - semValue(b.getSemester());
                    }
                });

                // Hitung GPA dari enrollment yang terpilih (satu per matkul)
                double total = 0;
                int    sks   = 0;

                for (Enrollment e : list) {
                    Course c = findCourse(courses, e.getCourseCode());
                    total += convert(e.finalGrade()) * c.getCredit();
                    sks   += c.getCredit();
                }

                double gpa = (sks == 0) ? 0 : total / sks;

                // Cetak baris ringkasan: format Student|GPA|SKS
                System.out.printf("%s|%.2f|%d\n", s, gpa, sks);

                // Cetak masing-masing enrollment terpilih
                for (Enrollment e : list) {
                    System.out.println(e);
                }
            }
        }
    }

    // ============================================================
    // course-history
    // Menampilkan riwayat pembukaan kelas suatu matkul.
    // Urutan sesuai urutan penambahan opening (tidak diurutkan ulang).
    //
    // Format per opening:
    //   kode|nama|sks|nilai_min|tahun|semester|INISIAL (email)[,INISIAL (email)]
    // Diikuti daftar enrollment yang ada di opening tersebut.
    // ============================================================
    static void showCourseHistory(
            String code,
            ArrayList<CourseOpening> openings,
            ArrayList<Course> courses,
            ArrayList<Lecturer> lecturers,
            ArrayList<Enrollment> enrollments) {

        // Iterasi setiap opening (urutan sesuai ArrayList → sesuai urutan input)
        for (CourseOpening co : openings) {

            // Hanya proses opening untuk matkul yang diminta
            if (co.getCourseCode().equals(code)) {

                // Cari data matkul dari kode
                Course c = findCourse(courses, code);

                // Bangun string daftar dosen
                // co.getLecturers() bisa berisi "IUS" atau "PAT,IUS,RSL"
                String text = "";
                String[] arr = co.getLecturers().split(","); // pecah berdasarkan koma

                for (int i = 0; i < arr.length; i++) {

                    // Cari dosen berdasarkan inisial
                    Lecturer l = findLecturer(lecturers, arr[i]);

                    // Format: INISIAL (email)
                    text += l.getInitial() + " (" + l.getEmail() + ")";

                    // Tambahkan koma jika bukan dosen terakhir
                    if (i != arr.length - 1) {
                        text += ",";
                    }
                }

                // Cetak baris header opening:
                // kode|nama|sks|nilai_min|tahun|semester|daftar_dosen
                System.out.println(c + "|" + co.getYear() + "|" + co.getSemester() + "|" + text);

                // Cetak semua enrollment yang ada di opening ini
                for (Enrollment e : enrollments) {
                    if (e.sameOpening(co) && e.getGrade() != null) {
                        // Hanya tampilkan enrollment yang sudah dinilai
                        System.out.println(e);
                    }
                }
            }
        }
    }

    // ============================================================
    // Helper: cari Course berdasarkan kode
    // Mengembalikan null jika tidak ditemukan
    // ============================================================
    static Course findCourse(ArrayList<Course> list, String code) {
        for (Course c : list) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        return null;
    }

    // ============================================================
    // Helper: cari Lecturer berdasarkan inisial
    // Mengembalikan null jika tidak ditemukan
    // ============================================================
    static Lecturer findLecturer(ArrayList<Lecturer> list, String initial) {
        for (Lecturer l : list) {
            if (l.getInitial().equals(initial)) {
                return l;
            }
        }
        return null;
    }

    // ============================================================
    // Helper: konversi nilai huruf ke bobot angka
    // Dipakai untuk perhitungan GPA
    // ============================================================
    static double convert(String g) {
        switch (g) {
            case "A":  return 4.0;
            case "AB": return 3.5;
            case "B":  return 3.0;
            case "BC": return 2.5;
            case "C":  return 2.0;
            case "D":  return 1.0;
            default:   return 0.0; // nilai tidak dikenal → 0
        }
    }

    // ============================================================
    // Helper: ambil tahun awal dari string "2020/2021" → 2020
    // Dipakai untuk membandingkan urutan tahun enrollment
    // ============================================================
    static int getYear(String s) {
        return Integer.parseInt(s.split("/")[0]);
    }

    // ============================================================
    // Helper: konversi semester ke angka untuk perbandingan urutan
    // "odd" (ganjil) = 1, "even" (genap) = 2
    // Semester ganjil datang sebelum genap dalam satu tahun ajaran
    // ============================================================
    static int semValue(String s) {
        if (s.equals("odd")) return 1;
        return 2; // "even"
    }

    // ============================================================
    // STATIC NESTED CLASS: Lecturer
    //
    // Alasan memakai Static Nested Class:
    // - Lecturer adalah entitas yang logis diletakkan di dalam Driver1
    //   karena hanya digunakan oleh Driver1 (tidak perlu file terpisah).
    // - Static → tidak membutuhkan instance Driver1 untuk dibuat,
    //   cukup akses via Driver1.Lecturer atau langsung karena satu file.
    // - Ini memenuhi syarat tugas: "aplikasikan salah satu bentuk
    //   Nested Constructs (Static Nested Class)".
    // ============================================================
    static class Lecturer {

        // field data dosen
        String id;       // nomor induk dosen
        String name;     // nama lengkap
        String initial;  // inisial (kode singkat), misal "IUS"
        String email;    // email kampus
        String study;    // program studi pengampu

        /**
         * Constructor Lecturer.
         *
         * @param id      nomor induk dosen, misal "0114129002"
         * @param name    nama lengkap, misal "Iustisia Natalia Simbolon"
         * @param initial inisial, misal "IUS"
         * @param email   email, misal "iustisia.simbolon@del.ac.id"
         * @param study   program studi, misal "Informatics"
         */
        Lecturer(String id, String name, String initial,
                 String email, String study) {
            this.id      = id;
            this.name    = name;
            this.initial = initial;
            this.email   = email;
            this.study   = study;
        }

        /**
         * Getter inisial — dipakai di showCourseHistory
         * untuk menampilkan "IUS (email)".
         */
        String getInitial() {
            return initial;
        }

        /**
         * Getter email — dipakai di showCourseHistory
         * untuk menampilkan "IUS (iustisia.simbolon@del.ac.id)".
         */
        String getEmail() {
            return email;
        }

        /**
         * Format output dosen saat dicetak di akhir program:
         * contoh → 0114129002|Iustisia Natalia Simbolon|IUS|iustisia.simbolon@del.ac.id|Informatics
         */
        @Override
        public String toString() {
            return id + "|" + name + "|" + initial + "|" + email + "|" + study;
        }
    }
}

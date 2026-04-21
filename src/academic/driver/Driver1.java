// ==============================
// FILE: academic/driver/Driver1.java
// ==============================
package academic.driver;

import java.util.*;
import academic.model.*;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Main program Academic Simulator - T08 Transcript.
 *
 * Nested Construct yang digunakan: STATIC NESTED CLASS (Lecturer)
 * Lecturer tidak butuh akses ke instance Driver1, cukup static nested.
 * Ini memenuhi syarat tugas "aplikasikan salah satu bentuk Nested Constructs".
 */
public class Driver1 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Daftar semua data yang tersimpan selama program berjalan
        ArrayList<Lecturer>      lecturers   = new ArrayList<>();
        ArrayList<Course>        courses     = new ArrayList<>();
        ArrayList<Student>       students    = new ArrayList<>();
        ArrayList<CourseOpening> openings    = new ArrayList<>();
        ArrayList<Enrollment>    enrollments = new ArrayList<>();

        // Baca input baris per baris sampai ketemu "---"
        while (true) {

            String line = input.nextLine();

            // Tanda berhenti membaca input
            if (line.equals("---")) {
                break;
            }

            // Pecah baris berdasarkan karakter '#'
            // data[0] = nama perintah, data[1..n] = parameter perintah
            String[] data = line.split("#");

            switch (data[0]) {

                case "lecturer-add":
                    // Tambah dosen baru ke list
                    // Format: lecturer-add#id#nama#inisial#email#prodi
                    lecturers.add(new Lecturer(
                            data[1], data[2], data[3],
                            data[4], data[5]));
                    break;

                case "course-add":
                    // Tambah mata kuliah baru ke list
                    // Format: course-add#kode#nama#sks#nilaiMinimum
                    courses.add(new Course(
                            data[1], data[2],
                            Integer.parseInt(data[3]),
                            data[4]));
                    break;

                case "student-add":
                    // Tambah mahasiswa baru ke list
                    // Format: student-add#NIM#nama#angkatan#prodi
                    students.add(new Student(
                            data[1], data[2],
                            data[3], data[4]));
                    break;

                case "course-open":
                    // Buka kelas baru untuk mata kuliah tertentu
                    // Format: course-open#kode#tahun#semester#inisialDosen(,inisialDosen)
                    openings.add(new CourseOpening(
                            data[1], data[2],
                            data[3], data[4]));
                    break;

                case "enrollment-add":
                    // Daftarkan mahasiswa ke suatu kelas
                    // Format: enrollment-add#kodeMatkul#NIM#tahun#semester
                    enrollments.add(new Enrollment(
                            data[1], data[2],
                            data[3], data[4]));
                    break;

                case "enrollment-grade":
                    // Set nilai reguler untuk enrollment yang cocok
                    // Format: enrollment-grade#kodeMatkul#NIM#tahun#semester#nilai
                    for (Enrollment e : enrollments) {
                        if (e.match(data[1], data[2], data[3], data[4])) {
                            e.setGrade(data[5]);
                        }
                    }
                    break;

                case "enrollment-remedial":
                    // Set nilai remedial - HANYA jika nilai reguler sudah ada.
                    // Jika remedial datang sebelum grade, perintah ini diabaikan.
                    // Format: enrollment-remedial#kodeMatkul#NIM#tahun#semester#nilaiRemedial
                    for (Enrollment e : enrollments) {
                        if (e.match(data[1], data[2], data[3], data[4])
                                && e.getGrade() != null) {
                            e.setRemedial(data[5]);
                        }
                    }
                    break;

                case "student-details":
                    // Tampilkan GPA kumulatif mahasiswa dari semua enrollment yang sudah dinilai
                    // Format: student-details#NIM
                    showStudentDetail(
                            data[1], students,
                            courses, enrollments);
                    break;

                case "course-history":
                    // Tampilkan riwayat semua pembukaan kelas suatu mata kuliah
                    // Format: course-history#kodeMatkul
                    showCourseHistory(
                            data[1], openings,
                            courses, lecturers,
                            enrollments);
                    break;

                case "student-transcript":
                    // Tampilkan transkrip mahasiswa (hanya pengambilan terakhir per matkul)
                    // Format: student-transcript#NIM
                    showTranscript(
                            data[1], students,
                            courses, enrollments);
                    break;
            }
        }

        // Output akhir program (dicetak setelah semua input selesai)

        // 1. Cetak semua dosen
        for (Lecturer l : lecturers) {
            System.out.println(l);
        }

        // 2. Cetak semua mata kuliah
        for (Course c : courses) {
            System.out.println(c);
        }

        // 3. Cetak semua mahasiswa
        for (Student s : students) {
            System.out.println(s);
        }

        // 4. Cetak semua enrollment yang sudah punya nilai
        //    Enrollment yang belum dinilai (grade==null) tidak ditampilkan
        for (Enrollment e : enrollments) {
            if (e.getGrade() != null) {
                System.out.println(e);
            }
        }

        input.close();
    }

    // ============================================================
    // METHOD: showStudentDetail (perintah: student-details)
    //
    // Menampilkan GPA kumulatif mahasiswa berdasarkan SEMUA
    // enrollment yang sudah dinilai, termasuk jika matkul diambil
    // lebih dari sekali (semua attempt dihitung, bukan hanya terbaru).
    //
    // Format output: NIM|Nama|Angkatan|Prodi|GPA|TotalSKS
    // Contoh: 12S20001|Marcelino Manalu|2020|Information Systems|3.00|7
    // ============================================================
    static void showStudentDetail(
            String id,
            ArrayList<Student> students,
            ArrayList<Course> courses,
            ArrayList<Enrollment> enrollments) {

        for (Student s : students) {

            // Cari mahasiswa berdasarkan NIM
            if (s.getId().equals(id)) {

                double total = 0; // akumulasi bobot x SKS
                int    sks   = 0; // total SKS dari semua enrollment yang sudah dinilai

                for (Enrollment e : enrollments) {

                    // Filter: milik mahasiswa ini dan sudah punya nilai reguler
                    if (e.getStudentId().equals(id) && e.getGrade() != null) {

                        // Ambil data matkul untuk mendapatkan jumlah SKS
                        Course c = findCourse(courses, e.getCourseCode());

                        // Bobot = nilai_akhir x SKS
                        // finalGrade() mengembalikan remedial jika ada, atau grade biasa
                        total += convert(e.finalGrade()) * c.getCredit();

                        // Tambah ke total SKS
                        sks += c.getCredit();
                    }
                }

                // GPA = total bobot / total SKS
                double gpa = (sks == 0) ? 0 : total / sks;

                // toString() Student menghasilkan "NIM|Nama|Angkatan|Prodi"
                // disambung dengan "|GPA|SKS"
                System.out.printf("%s|%.2f|%d\n", s, gpa, sks);
            }
        }
    }

    // ============================================================
    // METHOD: showTranscript (perintah: student-transcript)
    //
    // Menampilkan transkrip mahasiswa dengan aturan:
    // 1. Jika matkul diambil lebih dari sekali, hanya pengambilan
    //    TERAKHIR (tahun/semester paling akhir) yang ditampilkan.
    // 2. Daftar enrollment diurutkan historis: tahun lama duluan;
    //    jika tahun sama, odd sebelum even.
    //
    // Format baris 1: NIM|Nama|Angkatan|Prodi|GPA|TotalSKS
    // Format baris berikutnya: satu baris per enrollment terpilih
    // ============================================================
    static void showTranscript(
            String id,
            ArrayList<Student> students,
            ArrayList<Course> courses,
            ArrayList<Enrollment> enrollments) {

        for (Student s : students) {

            // Cari mahasiswa berdasarkan NIM
            if (s.getId().equals(id)) {

                // List enrollment terpilih: satu per matkul (yang terbaru)
                ArrayList<Enrollment> list = new ArrayList<>();

                // Iterasi setiap matkul untuk menemukan enrollment terbaru mahasiswa ini
                for (Course c : courses) {

                    // best = kandidat enrollment terbaru untuk matkul ini
                    Enrollment best = null;

                    for (Enrollment e : enrollments) {

                        // Filter: milik mahasiswa ini, untuk matkul c, sudah dinilai
                        if (e.getStudentId().equals(id)
                                && e.getCourseCode().equals(c.getCode())
                                && e.getGrade() != null) {

                            if (best == null) {
                                // Kandidat pertama langsung dijadikan best
                                best = e;
                            } else {
                                int y1 = getYear(e.getYear());
                                int y2 = getYear(best.getYear());

                                if (y1 > y2) {
                                    // e lebih baru (tahun lebih besar) -> ganti best
                                    best = e;
                                } else if (y1 == y2
                                        && semValue(e.getSemester())
                                           > semValue(best.getSemester())) {
                                    // Tahun sama, semester e lebih akhir (even > odd) -> ganti best
                                    best = e;
                                }
                                // Jika e lebih lama -> biarkan best tidak berubah
                            }
                        }
                    }

                    // Jika ada enrollment untuk matkul ini -> tambahkan ke list
                    if (best != null) {
                        list.add(best);
                    }
                }

                // Urutkan list secara historis (yang lebih lama tampil lebih dulu)
                Collections.sort(list, new Comparator<Enrollment>() {
                    @Override
                    public int compare(Enrollment a, Enrollment b) {
                        int ya = getYear(a.getYear());
                        int yb = getYear(b.getYear());

                        // Bandingkan tahun terlebih dahulu (ascending)
                        if (ya != yb) {
                            return ya - yb;
                        }

                        // Jika tahun sama, bandingkan semester
                        // odd(1) < even(2) -> odd tampil lebih dulu
                        return semValue(a.getSemester()) - semValue(b.getSemester());
                    }
                });

                // Hitung GPA hanya dari enrollment yang terpilih
                double total = 0;
                int    sks   = 0;

                for (Enrollment e : list) {
                    Course c = findCourse(courses, e.getCourseCode());
                    total += convert(e.finalGrade()) * c.getCredit();
                    sks   += c.getCredit();
                }

                double gpa = (sks == 0) ? 0 : total / sks;

                // Cetak baris ringkasan
                System.out.printf("%s|%.2f|%d\n", s, gpa, sks);

                // Cetak setiap enrollment yang terpilih (sudah terurut historis)
                for (Enrollment e : list) {
                    System.out.println(e);
                }
            }
        }
    }

    // ============================================================
    // METHOD: showCourseHistory (perintah: course-history)
    //
    // Menampilkan riwayat semua pembukaan kelas suatu mata kuliah.
    //
    // URUTAN OPENING: semester dulu (odd sebelum even),
    // lalu tahun ascending dalam semester yang sama.
    // Contoh: 2020/2021 odd -> 2021/2022 odd -> 2020/2021 even
    //
    // Format per opening:
    //   kode|nama|sks|nilaiMin|tahun|semester|INISIAL (email)[,INISIAL (email)]
    // Diikuti daftar enrollment yang sudah dinilai di opening tersebut.
    // ============================================================
    static void showCourseHistory(
            String code,
            ArrayList<CourseOpening> openings,
            ArrayList<Course> courses,
            ArrayList<Lecturer> lecturers,
            ArrayList<Enrollment> enrollments) {

        // Langkah 1: filter hanya opening untuk matkul yang diminta
        ArrayList<CourseOpening> filtered = new ArrayList<>();
        for (CourseOpening co : openings) {
            if (co.getCourseCode().equals(code)) {
                filtered.add(co);
            }
        }

        // Langkah 2: urutkan opening
        // Aturan: semester dulu (odd=1 < even=2), lalu tahun ascending
        Collections.sort(filtered, new Comparator<CourseOpening>() {
            @Override
            public int compare(CourseOpening a, CourseOpening b) {
                // Prioritas 1: semester (odd sebelum even)
                int sa = semValue(a.getSemester()); // odd=1, even=2
                int sb = semValue(b.getSemester());
                if (sa != sb) {
                    return sa - sb;
                }
                // Prioritas 2: tahun ascending (lama duluan)
                return getYear(a.getYear()) - getYear(b.getYear());
            }
        });

        // Langkah 3: cetak setiap opening beserta enrollment-nya
        for (CourseOpening co : filtered) {

            // Ambil data mata kuliah
            Course c = findCourse(courses, code);

            // Bangun string daftar dosen
            // co.getLecturers() bisa "IUS" atau "PAT,IUS,RSL"
            String text = "";
            String[] arr = co.getLecturers().split(",");

            for (int i = 0; i < arr.length; i++) {
                // Cari dosen berdasarkan inisial
                Lecturer l = findLecturer(lecturers, arr[i]);
                // Format tiap dosen: INISIAL (email)
                text += l.getInitial() + " (" + l.getEmail() + ")";
                // Pisahkan dengan koma jika bukan yang terakhir
                if (i != arr.length - 1) {
                    text += ",";
                }
            }

            // Cetak header opening: kode|nama|sks|nilaiMin|tahun|semester|daftar_dosen
            System.out.println(c + "|" + co.getYear() + "|" + co.getSemester() + "|" + text);

            // Cetak semua enrollment di opening ini yang sudah dinilai
            for (Enrollment e : enrollments) {
                if (e.sameOpening(co) && e.getGrade() != null) {
                    System.out.println(e);
                }
            }
        }
    }

    // ============================================================
    // HELPER: cari Course berdasarkan kode matkul
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
    // HELPER: cari Lecturer berdasarkan inisial
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
    // HELPER: konversi nilai huruf ke bobot angka (untuk hitung GPA)
    // A=4.0, AB=3.5, B=3.0, BC=2.5, C=2.0, D=1.0
    // ============================================================
    static double convert(String g) {
        switch (g) {
            case "A":  return 4.0;
            case "AB": return 3.5;
            case "B":  return 3.0;
            case "BC": return 2.5;
            case "C":  return 2.0;
            case "D":  return 1.0;
            default:   return 0.0;
        }
    }

    // ============================================================
    // HELPER: ambil tahun awal dari string tahun ajaran
    // "2020/2021" -> 2020
    // ============================================================
    static int getYear(String s) {
        return Integer.parseInt(s.split("/")[0]);
    }

    // ============================================================
    // HELPER: konversi semester ke angka
    // "odd" (ganjil) = 1, "even" (genap) = 2
    // odd tampil lebih dulu dari even
    // ============================================================
    static int semValue(String s) {
        if (s.equals("odd")) return 1;
        return 2;
    }

    // ============================================================
    // STATIC NESTED CLASS: Lecturer
    //
    // Mengapa Static Nested Class?
    // - Lecturer hanya digunakan oleh Driver1, tidak perlu file terpisah.
    // - "static" berarti tidak memerlukan instance Driver1 untuk dibuat.
    // - Memenuhi syarat tugas: menggunakan bentuk Nested Constructs.
    // ============================================================
    static class Lecturer {

        String id;       // nomor induk dosen, misal "0114129002"
        String name;     // nama lengkap dosen
        String initial;  // inisial/kode singkat, misal "IUS"
        String email;    // alamat email, misal "iustisia.simbolon@del.ac.id"
        String study;    // program studi pengampu, misal "Informatics"

        /**
         * Constructor dipanggil saat perintah lecturer-add diproses.
         *
         * @param id      nomor induk dosen
         * @param name    nama lengkap
         * @param initial inisial dosen
         * @param email   email dosen
         * @param study   program studi
         */
        Lecturer(String id, String name, String initial,
                 String email, String study) {
            this.id      = id;
            this.name    = name;
            this.initial = initial;
            this.email   = email;
            this.study   = study;
        }

        // Getter inisial dipakai di showCourseHistory untuk string "IUS (email)"
        String getInitial() {
            return initial;
        }

        // Getter email dipakai di showCourseHistory
        String getEmail() {
            return email;
        }

        // Format output: id|nama|inisial|email|prodi
        // Contoh: 0114129002|Iustisia Natalia Simbolon|IUS|iustisia.simbolon@del.ac.id|Informatics
        @Override
        public String toString() {
            return id + "|" + name + "|" + initial + "|" + email + "|" + study;
        }
    }
}

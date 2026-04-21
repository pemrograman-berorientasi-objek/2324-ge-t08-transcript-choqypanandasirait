// ==============================
// FILE: academic/model/Enrollment.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Kelas ini menyimpan data satu enrollment (pengambilan mata kuliah oleh mahasiswa).
 * Satu mahasiswa bisa memiliki lebih dari satu enrollment untuk matkul yang sama
 * (jika mengambil ulang di semester/tahun berbeda).
 *
 * Tidak ada perubahan dari versi sebelumnya — sudah benar.
 */
public class Enrollment {

    // field identitas: kode matkul, id mahasiswa, tahun, semester
    String code, studentId, year, semester;

    // nilai reguler (diisi lewat enrollment-grade)
    String grade;

    // nilai remedial, null jika tidak ada remedial (diisi lewat enrollment-remedial)
    String remedial;

    /**
     * Constructor: dipanggil saat enrollment-add diproses.
     *
     * @param code      kode matkul
     * @param studentId NIM mahasiswa
     * @param year      tahun ajaran, misal "2020/2021"
     * @param semester  "odd" atau "even"
     */
    public Enrollment(String code, String studentId,
                      String year, String semester) {
        this.code      = code;
        this.studentId = studentId;
        this.year      = year;
        this.semester  = semester;
        // grade dan remedial awalnya null
    }

    /**
     * Mengecek apakah enrollment ini sesuai dengan 4 parameter.
     * Dipakai saat enrollment-grade dan enrollment-remedial untuk
     * menemukan enrollment yang tepat dari ArrayList.
     */
    public boolean match(String a, String b, String c, String d) {
        return code.equals(a)
                && studentId.equals(b)
                && year.equals(c)
                && semester.equals(d);
    }

    /**
     * Mengecek apakah enrollment ini berada di dalam CourseOpening tertentu.
     * Dipakai di showCourseHistory untuk menampilkan daftar mahasiswa
     * yang ada di suatu opening.
     */
    public boolean sameOpening(CourseOpening co) {
        return code.equals(co.getCourseCode())
                && year.equals(co.getYear())
                && semester.equals(co.getSemester());
    }

    /** Setter nilai reguler — dipanggil saat enrollment-grade. */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * Setter nilai remedial — dipanggil saat enrollment-remedial.
     * Catatan: remedial hanya bisa diset SETELAH grade sudah ada
     * (dijamin oleh urutan input soal).
     */
    public void setRemedial(String remedial) {
        this.remedial = remedial;
    }

    /** Getter NIM mahasiswa — dipakai untuk filter di Driver1. */
    public String getStudentId() {
        return studentId;
    }

    /** Getter kode matkul — dipakai untuk mencari Course terkait. */
    public String getCourseCode() {
        return code;
    }

    /** Getter tahun ajaran. */
    public String getYear() {
        return year;
    }

    /** Getter semester. */
    public String getSemester() {
        return semester;
    }

    /**
     * Getter nilai reguler (grade).
     * Bisa null jika belum dinilai — dipakai untuk cek apakah
     * enrollment sudah selesai (e.getGrade() != null).
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Mengembalikan nilai akhir yang berlaku:
     * - Jika ada remedial → nilai remedial yang dipakai (karena lebih baik)
     * - Jika tidak ada → nilai reguler
     *
     * Dipakai untuk perhitungan GPA (convert(e.finalGrade())).
     */
    public String finalGrade() {
        if (remedial != null) {
            return remedial; // remedial menggantikan grade lama
        }
        return grade;
    }

    /**
     * Format output enrollment:
     * - Jika ada remedial: kode|NIM|tahun|semester|REMEDIAL(GRADE)
     *   contoh: 12S1101|12S20003|2020/2021|odd|AB(B)
     *   artinya: nilai awal B, diperbaiki jadi AB lewat remedial
     * - Jika tidak ada remedial: kode|NIM|tahun|semester|GRADE
     *   contoh: 12S1101|12S20002|2020/2021|odd|B
     */
    @Override
    public String toString() {
        if (remedial != null) {
            // format: NILAI_BARU(NILAI_LAMA) — remedial ada, tampilkan keduanya
            return code + "|" + studentId + "|"
                    + year + "|" + semester + "|"
                    + remedial + "(" + grade + ")";
        }
        // format biasa tanpa remedial
        return code + "|" + studentId + "|"
                + year + "|" + semester + "|" + grade;
    }
}

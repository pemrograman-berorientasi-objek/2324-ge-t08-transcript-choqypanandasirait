// ==============================
// FILE: academic/model/Enrollment.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Menyimpan data satu pengambilan mata kuliah oleh seorang mahasiswa
 * pada tahun ajaran dan semester tertentu.
 *
 * Satu mahasiswa bisa memiliki lebih dari satu Enrollment untuk matkul
 * yang sama, jika mengambil ulang di tahun/semester yang berbeda.
 */
public class Enrollment {

    // Kode matkul yang diambil, misal "12S1101"
    String code;

    // NIM mahasiswa yang mengambil, misal "12S20001"
    String studentId;

    // Tahun ajaran pengambilan, misal "2020/2021"
    String year;

    // Semester pengambilan: "odd" atau "even"
    String semester;

    // Nilai reguler yang diberikan dosen (null jika belum dinilai)
    String grade;

    // Nilai remedial (null jika tidak ada remedial)
    // Remedial hanya bisa diset SETELAH grade sudah ada
    String remedial;

    /**
     * Constructor dipanggil saat perintah enrollment-add diproses.
     * grade dan remedial awalnya null (belum ada nilai).
     *
     * @param code      kode matkul
     * @param studentId NIM mahasiswa
     * @param year      tahun ajaran
     * @param semester  "odd" atau "even"
     */
    public Enrollment(String code, String studentId,
                      String year, String semester) {
        this.code      = code;
        this.studentId = studentId;
        this.year      = year;
        this.semester  = semester;
        // grade dan remedial default null
    }

    /**
     * Mengecek apakah enrollment ini cocok dengan 4 parameter identitas.
     * Dipakai saat enrollment-grade dan enrollment-remedial untuk
     * menemukan enrollment yang tepat dari ArrayList enrollments.
     *
     * @param a kode matkul
     * @param b NIM mahasiswa
     * @param c tahun ajaran
     * @param d semester
     * @return true jika semua 4 field cocok
     */
    public boolean match(String a, String b, String c, String d) {
        return code.equals(a)
                && studentId.equals(b)
                && year.equals(c)
                && semester.equals(d);
    }

    /**
     * Mengecek apakah enrollment ini berada di dalam CourseOpening tertentu.
     * Cocok jika kode matkul, tahun, dan semester sama.
     * Dipakai di showCourseHistory untuk mencetak daftar mahasiswa per opening.
     *
     * @param co CourseOpening yang dibandingkan
     * @return true jika enrollment ini ada di opening co
     */
    public boolean sameOpening(CourseOpening co) {
        return code.equals(co.getCourseCode())
                && year.equals(co.getYear())
                && semester.equals(co.getSemester());
    }

    /**
     * Set nilai reguler.
     * Dipanggil saat perintah enrollment-grade diproses.
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * Set nilai remedial.
     * Dipanggil saat perintah enrollment-remedial diproses.
     * PERHATIAN: di Driver1 sudah ada pengecekan bahwa remedial
     * hanya bisa diset jika grade sudah ada (getGrade() != null).
     */
    public void setRemedial(String remedial) {
        this.remedial = remedial;
    }

    /**
     * Getter NIM mahasiswa.
     * Dipakai di showStudentDetail dan showTranscript untuk filter
     * enrollment berdasarkan mahasiswa: e.getStudentId().equals(id)
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Getter kode matkul.
     * Dipakai di showTranscript untuk mencocokkan enrollment dengan Course.
     */
    public String getCourseCode() {
        return code;
    }

    /**
     * Getter tahun ajaran.
     * Dipakai di showTranscript untuk membandingkan enrollment mana yang terbaru.
     */
    public String getYear() {
        return year;
    }

    /**
     * Getter semester.
     * Dipakai di showTranscript untuk membandingkan urutan semester.
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Getter nilai reguler.
     * Bisa null jika belum dinilai.
     * Dipakai untuk pengecekan: e.getGrade() != null
     * artinya enrollment sudah selesai dan bisa dihitung GPA-nya.
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Mengembalikan nilai akhir yang berlaku untuk perhitungan GPA:
     * - Jika ada remedial -> kembalikan remedial (remedial menggantikan grade)
     * - Jika tidak ada remedial -> kembalikan grade
     *
     * Dipakai di convert(e.finalGrade()) untuk menghitung bobot nilai.
     */
    public String finalGrade() {
        if (remedial != null) {
            return remedial;
        }
        return grade;
    }

    /**
     * Format output enrollment saat dicetak.
     *
     * Jika ada remedial:
     *   kode|NIM|tahun|semester|REMEDIAL(GRADE)
     *   Contoh: 12S1101|12S20003|2020/2021|odd|AB(B)
     *   Artinya: nilai reguler B, diperbaiki jadi AB lewat remedial
     *
     * Jika tidak ada remedial:
     *   kode|NIM|tahun|semester|GRADE
     *   Contoh: 12S1101|12S20002|2020/2021|odd|B
     */
    @Override
    public String toString() {
        if (remedial != null) {
            // Format: NILAI_BARU(NILAI_LAMA)
            return code + "|" + studentId + "|"
                    + year + "|" + semester + "|"
                    + remedial + "(" + grade + ")";
        }
        // Format biasa tanpa remedial
        return code + "|" + studentId + "|"
                + year + "|" + semester + "|" + grade;
    }
}

// ==============================
// FILE: academic/model/Course.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Kelas ini menyimpan data satu mata kuliah.
 * Tidak ada perubahan dari versi sebelumnya — sudah benar.
 */
public class Course {

    // field: kode matkul, nama matkul, grade minimum, jumlah SKS
    String code, name, grade;
    int credit;

    /**
     * Constructor: dipanggil saat course-add diproses di Driver1.
     *
     * @param code   kode matkul, misal "12S1101"
     * @param name   nama matkul, misal "Dasar Sistem Informasi"
     * @param credit jumlah SKS, misal 3
     * @param grade  nilai minimum kelulusan, misal "D"
     */
    public Course(String code, String name, int credit, String grade) {
        this.code   = code;
        this.name   = name;
        this.credit = credit;
        this.grade  = grade;
    }

    /**
     * Getter kode matkul — dipakai untuk mencari course
     * berdasarkan kode di Driver1 (findCourse).
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter SKS — dipakai untuk menghitung total SKS
     * dalam perhitungan GPA di showStudentDetail & showTranscript.
     */
    public int getCredit() {
        return credit;
    }

    /**
     * Format output saat dicetak di akhir program:
     * contoh → 12S1101|Dasar Sistem Informasi|3|D
     */
    @Override
    public String toString() {
        return code + "|" + name + "|" + credit + "|" + grade;
    }
}

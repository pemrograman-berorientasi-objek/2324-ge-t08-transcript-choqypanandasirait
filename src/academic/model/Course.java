// ==============================
// FILE: academic/model/Course.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Menyimpan data satu mata kuliah.
 */
public class Course {

    // Kode unik matkul, misal "12S1101"
    String code;

    // Nama lengkap matkul, misal "Dasar Sistem Informasi"
    String name;

    // Nilai minimum kelulusan, misal "D"
    String grade;

    // Jumlah SKS, misal 3
    int credit;

    /**
     * Constructor dipanggil saat perintah course-add diproses di Driver1.
     *
     * @param code   kode matkul
     * @param name   nama matkul
     * @param credit jumlah SKS
     * @param grade  nilai minimum kelulusan
     */
    public Course(String code, String name, int credit, String grade) {
        this.code   = code;
        this.name   = name;
        this.credit = credit;
        this.grade  = grade;
    }

    /**
     * Getter kode matkul.
     * Dipakai di Driver1 untuk mencari Course berdasarkan kode (findCourse).
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter jumlah SKS.
     * Dipakai di showStudentDetail dan showTranscript untuk menghitung GPA.
     * Rumus: total += convert(nilaiAkhir) * getCredit()
     */
    public int getCredit() {
        return credit;
    }

    /**
     * Format output saat dicetak.
     * Contoh: 12S1101|Dasar Sistem Informasi|3|D
     *
     * Juga digunakan sebagai prefix di course-history:
     * 12S1101|Dasar Sistem Informasi|3|D|2020/2021|odd|IUS (...)
     */
    @Override
    public String toString() {
        return code + "|" + name + "|" + credit + "|" + grade;
    }
}

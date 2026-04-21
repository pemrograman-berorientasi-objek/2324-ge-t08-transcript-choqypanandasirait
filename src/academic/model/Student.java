// ==============================
// FILE: academic/model/Student.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Menyimpan data seorang mahasiswa.
 */
public class Student {

    // NIM mahasiswa, misal "12S20001"
    String id;

    // Nama lengkap, misal "Marcelino Manalu"
    String name;

    // Tahun angkatan masuk, misal "2020"
    String year;

    // Program studi, misal "Information Systems"
    String studyProgram;

    /**
     * Constructor dipanggil saat perintah student-add diproses di Driver1.
     *
     * @param id           NIM mahasiswa
     * @param name         nama lengkap
     * @param year         angkatan
     * @param studyProgram program studi
     */
    public Student(String id, String name, String year, String studyProgram) {
        this.id           = id;
        this.name         = name;
        this.year         = year;
        this.studyProgram = studyProgram;
    }

    /**
     * Getter NIM.
     * Dipakai untuk mencocokkan mahasiswa di showStudentDetail
     * dan showTranscript: s.getId().equals(id)
     */
    public String getId() {
        return id;
    }

    /**
     * Format output mahasiswa saat dicetak.
     * Contoh: 12S20001|Marcelino Manalu|2020|Information Systems
     *
     * Format ini juga digunakan sebagai prefix di student-details
     * dan student-transcript via printf("%s|%.2f|%d", s, gpa, sks)
     * yang memanggil toString() secara otomatis.
     */
    @Override
    public String toString() {
        return id + "|" + name + "|" + year + "|" + studyProgram;
    }
}

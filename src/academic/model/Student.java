// ==============================
// FILE: academic/model/Student.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Kelas ini menyimpan data satu mahasiswa.
 * Tidak ada perubahan dari versi sebelumnya — sudah benar.
 */
public class Student {

    // field: NIM, nama, angkatan, program studi
    String id, name, year, studyProgram;

    /**
     * Constructor: dipanggil saat student-add diproses di Driver1.
     *
     * @param id           NIM mahasiswa, misal "12S20001"
     * @param name         nama mahasiswa, misal "Marcelino Manalu"
     * @param year         angkatan, misal "2020"
     * @param studyProgram program studi, misal "Information Systems"
     */
    public Student(String id, String name, String year, String studyProgram) {
        this.id           = id;
        this.name         = name;
        this.year         = year;
        this.studyProgram = studyProgram;
    }

    /**
     * Getter NIM — dipakai untuk mencocokkan mahasiswa
     * pada student-details dan student-transcript.
     */
    public String getId() {
        return id;
    }

    /**
     * Format output saat dicetak di akhir program:
     * contoh → 12S20001|Marcelino Manalu|2020|Information Systems
     *
     * Format ini juga dipakai sebagai PREFIX saat student-details
     * dan student-transcript dicetak (via %s|... di printf).
     */
    @Override
    public String toString() {
        return id + "|" + name + "|" + year + "|" + studyProgram;
    }
}

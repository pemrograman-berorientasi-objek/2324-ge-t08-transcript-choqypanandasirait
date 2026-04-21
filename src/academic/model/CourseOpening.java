// ==============================
// FILE: academic/model/CourseOpening.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Menyimpan data pembukaan kelas (course opening).
 * Satu mata kuliah bisa dibuka di banyak semester berbeda.
 * Setiap pembukaan memiliki dosen pengampu yang bisa lebih dari satu.
 */
public class CourseOpening {

    // Kode matkul yang dibuka, misal "12S1101"
    String code;

    // Tahun ajaran, misal "2020/2021"
    String year;

    // Semester: "odd" (ganjil) atau "even" (genap)
    String semester;

    // Inisial dosen pengampu, bisa lebih dari satu dipisah koma
    // Contoh: "IUS" atau "PAT,IUS,RSL"
    String lecturers;

    /**
     * Constructor dipanggil saat perintah course-open diproses di Driver1.
     *
     * @param code      kode matkul
     * @param year      tahun ajaran
     * @param semester  "odd" atau "even"
     * @param lecturers inisial dosen (bisa dipisah koma jika lebih dari satu)
     */
    public CourseOpening(String code, String year,
                         String semester, String lecturers) {
        this.code      = code;
        this.year      = year;
        this.semester  = semester;
        this.lecturers = lecturers;
    }

    /**
     * Getter kode matkul.
     * Dipakai di showCourseHistory untuk mencocokkan opening dengan kode yang dicari.
     * Dipakai di Driver1 saat memfilter opening: co.getCourseCode().equals(code)
     */
    public String getCourseCode() {
        return code;
    }

    /**
     * Getter tahun ajaran.
     * Dipakai di showCourseHistory untuk ditampilkan dan untuk sorting.
     */
    public String getYear() {
        return year;
    }

    /**
     * Getter semester.
     * Dipakai di showCourseHistory untuk ditampilkan dan untuk sorting.
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Getter string dosen pengampu.
     * Dipakai di showCourseHistory, di-split dengan koma untuk
     * menampilkan setiap dosen dalam format "INISIAL (email)".
     */
    public String getLecturers() {
        return lecturers;
    }
}

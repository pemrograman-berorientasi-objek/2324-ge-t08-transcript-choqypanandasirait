// ==============================
// FILE: academic/model/CourseOpening.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 *
 * Kelas ini menyimpan data pembukaan kelas (course opening):
 * satu mata kuliah dibuka pada tahun/semester tertentu
 * dan diajar oleh satu atau lebih dosen.
 *
 * Tidak ada perubahan dari versi sebelumnya — sudah benar.
 */
public class CourseOpening {

    // field: kode matkul, tahun ajaran, semester, daftar inisial dosen (dipisah koma)
    String code, year, semester, lecturers;

    /**
     * Constructor: dipanggil saat course-open diproses di Driver1.
     *
     * @param code      kode matkul, misal "12S1101"
     * @param year      tahun ajaran, misal "2020/2021"
     * @param semester  semester, "odd" atau "even"
     * @param lecturers inisial dosen, misal "IUS" atau "PAT,IUS,RSL"
     */
    public CourseOpening(String code, String year,
                         String semester, String lecturers) {
        this.code      = code;
        this.year      = year;
        this.semester  = semester;
        this.lecturers = lecturers;
    }

    /** Getter kode matkul — dipakai di showCourseHistory untuk mencocokkan kode. */
    public String getCourseCode() {
        return code;
    }

    /** Getter tahun ajaran — dipakai di showCourseHistory untuk output. */
    public String getYear() {
        return year;
    }

    /** Getter semester — dipakai di showCourseHistory untuk output. */
    public String getSemester() {
        return semester;
    }

    /**
     * Getter string dosen — dipakai di showCourseHistory.
     * Contoh nilai: "IUS" atau "PAT,IUS,RSL"
     * Akan di-split dengan koma untuk ditampilkan satu per satu.
     */
    public String getLecturers() {
        return lecturers;
    }
}

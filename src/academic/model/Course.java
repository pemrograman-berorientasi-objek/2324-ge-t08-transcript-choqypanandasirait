// ==============================
// FILE: academic/model/Course.java
// ==============================
package academic.model;

/**
 * @author 12S24012 Choqy Pananda Sirait
 */
/**
 * Menyimpan data mata kuliah
 */
public class Course {

    // kode matkul, nama, nilai minimum, sks
    String code, name, grade;
    int credit;

    // constructor
    public Course(String code, String name, int credit, String grade) {
        this.code = code;
        this.name = name;
        this.credit = credit;
        this.grade = grade;
    }

    // ambil kode
    public String getCode() {
        return code;
    }

    // ambil sks
    public int getCredit() {
        return credit;
    }

    // format output
    public String toString() {
        return code + "|" + name + "|" + credit + "|" + grade;
    }
}
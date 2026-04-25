package dto.responseDto.eligibility;

public class CAEligibilityDTO {
    private String studentId;
    private String regNo;
    private String studentName;
    private String courseId;
    private String courseCode;
    private String courseName;
    private double caMaxMarks;
    private double caMarks;
    private double caPercentage;
    private String eligibilityStatus;

    public CAEligibilityDTO() {}

    public CAEligibilityDTO(String studentId, String regNo, String studentName,
                            String courseId, String courseCode, String courseName,
                            double caMaxMarks, double caMarks,
                            double caPercentage, String eligibilityStatus) {
        this.studentId = studentId;
        this.regNo = regNo;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.caMaxMarks = caMaxMarks;
        this.caMarks = caMarks;
        this.caPercentage = caPercentage;
        this.eligibilityStatus = eligibilityStatus;
    }

    public String getStudentId() { return studentId; }
    public String getRegNo() { return regNo; }
    public String getStudentName() { return studentName; }
    public String getCourseId() { return courseId; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public double getCaMaxMarks() { return caMaxMarks; }
    public double getCaMarks() { return caMarks; }
    public double getCaPercentage() { return caPercentage; }
    public String getEligibilityStatus() { return eligibilityStatus; }

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setCaMaxMarks(double caMaxMarks) { this.caMaxMarks = caMaxMarks; }
    public void setCaMarks(double caMarks) { this.caMarks = caMarks; }
    public void setCaPercentage(double caPercentage) { this.caPercentage = caPercentage; }
    public void setEligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; }
}
package dao.report;

import model.AcademicReportRow;
import utility.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AcademicReportDAO {

    public List<AcademicReportRow> getStudentFullAcademicReport(String studentId) {
        return getFullAcademicReportInternal(studentId, null);
    }

    public List<AcademicReportRow> getBatchFullAcademicReport(String batch) {
        return getFullAcademicReportInternal(null, batch);
    }

    private List<AcademicReportRow> getFullAcademicReportInternal(String studentId, String batch) {
        String sql = "SELECT cr.student_id, s.reg_no, u.username, s.batch, " +
                "cr.course_id, c.course_code, c.name AS course_name, cr.academic_year, cr.semester, " +
                "COALESCE(att.attendance_percentage, 0) AS attendance_percentage, " +
                "COALESCE(med.medical_count, 0) AS medical_count, " +
                "COALESCE(ca.ca_marks, 0) AS ca_marks, " +
                "COALESCE(fin.final_marks, 0) AS final_marks, " +
                "COALESCE(cr.total_marks, (COALESCE(ca.ca_marks,0) + COALESCE(fin.final_marks,0))) AS total_marks, " +
                "cr.grade, " +
                "sr.sgpa, sr.cgpa, " +
                "CASE WHEN COALESCE(att.attendance_percentage,0) >= 80 AND COALESCE(ca.ca_marks,0) >= 40 " +
                "THEN 'Eligible' ELSE 'Not Eligible' END AS eligibility_status " +
                "FROM course_result cr " +
                "INNER JOIN students s ON s.user_id = cr.student_id " +
                "INNER JOIN users u ON u.user_id = s.user_id " +
                "INNER JOIN course c ON c.course_id = cr.course_id " +
                "LEFT JOIN semester_result sr ON sr.student_id = cr.student_id AND sr.academic_year = cr.academic_year AND sr.semester = cr.semester " +
                "LEFT JOIN ( " +
                "   SELECT a.student_id, se.course_id, ROUND((SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) * 100.0) / NULLIF(COUNT(*),0),2) AS attendance_percentage " +
                "   FROM attendance a INNER JOIN session se ON se.session_id = a.session_id " +
                "   GROUP BY a.student_id, se.course_id " +
                ") att ON att.student_id = cr.student_id AND att.course_id = cr.course_id " +
                "LEFT JOIN ( " +
                "   SELECT student_id, course_id, COUNT(*) AS medical_count " +
                "   FROM medical GROUP BY student_id, course_id " +
                ") med ON med.student_id = cr.student_id AND med.course_id = cr.course_id " +
                "LEFT JOIN ( " +
                "   SELECT sm.student_id, at.course_id, ROUND(SUM(sm.marks * at.weight / 100),2) AS ca_marks " +
                "   FROM student_marks sm INNER JOIN assessment_type at ON at.assessment_type_id = sm.assessment_type_id " +
                "   WHERE at.component = 'CA' " +
                "   GROUP BY sm.student_id, at.course_id " +
                ") ca ON ca.student_id = cr.student_id AND ca.course_id = cr.course_id " +
                "LEFT JOIN ( " +
                "   SELECT sm.student_id, at.course_id, ROUND(SUM(sm.marks * at.weight / 100),2) AS final_marks " +
                "   FROM student_marks sm INNER JOIN assessment_type at ON at.assessment_type_id = sm.assessment_type_id " +
                "   WHERE at.component = 'Final' " +
                "   GROUP BY sm.student_id, at.course_id " +
                ") fin ON fin.student_id = cr.student_id AND fin.course_id = cr.course_id " +
                "WHERE (? IS NULL OR cr.student_id = ?) AND (? IS NULL OR s.batch = ?) " +
                "ORDER BY s.reg_no, cr.academic_year, cr.semester, c.course_code";

        List<AcademicReportRow> rows = new ArrayList<>();

        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, studentId);
            ps.setString(3, batch);
            ps.setString(4, batch);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AcademicReportRow row = new AcademicReportRow();
                    row.setStudentId(rs.getString("student_id"));
                    row.setRegNo(rs.getString("reg_no"));
                    row.setStudentName(rs.getString("username"));
                    row.setBatch(rs.getString("batch"));
                    row.setCourseId(rs.getString("course_id"));
                    row.setCourseCode(rs.getString("course_code"));
                    row.setCourseName(rs.getString("course_name"));
                    row.setAcademicYear(rs.getInt("academic_year"));
                    row.setSemester(Integer.parseInt(rs.getString("semester")));
                    row.setAttendancePercentage(rs.getDouble("attendance_percentage"));
                    row.setMedicalCount(rs.getInt("medical_count"));
                    row.setCaMarks(rs.getDouble("ca_marks"));
                    row.setFinalMarks(rs.getDouble("final_marks"));
                    row.setTotalMarks(rs.getDouble("total_marks"));
                    row.setGrade(rs.getString("grade"));
                    row.setSgpa(rs.getDouble("sgpa"));
                    row.setCgpa(rs.getDouble("cgpa"));
                    row.setEligibilityStatus(rs.getString("eligibility_status"));
                    rows.add(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }
}

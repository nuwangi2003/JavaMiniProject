package dao.eligibility;

import dto.responseDto.eligibility.StudentEligibilityDTO;
import utility.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentEligibilityDAO {

    public List<StudentEligibilityDTO> getEligibilityByCourse(String lecturerId, String courseId) {
        List<StudentEligibilityDTO> list = new ArrayList<>();

        String sql = """
                SELECT 
                    s.user_id AS student_id,
                    s.reg_no,
                    u.username AS student_name,
                    c.course_id,
                    c.course_code,
                    c.name AS course_name,
                    COALESCE(total_sessions.total_hours, 0) AS total_hours,
                    COALESCE(attended.attended_hours, 0) AS attended_hours
                FROM course_registration cr
                INNER JOIN students s ON cr.student_id = s.user_id
                INNER JOIN users u ON s.user_id = u.user_id
                INNER JOIN course c ON cr.course_id = c.course_id
                INNER JOIN lecturer_course lc ON lc.course_id = c.course_id
                LEFT JOIN (
                    SELECT course_id, SUM(session_hours) AS total_hours
                    FROM session
                    GROUP BY course_id
                ) total_sessions ON total_sessions.course_id = c.course_id
                LEFT JOIN (
                    SELECT 
                        a.student_id,
                        se.course_id,
                        SUM(a.hours_attended) AS attended_hours
                    FROM attendance a
                    INNER JOIN session se ON a.session_id = se.session_id
                    WHERE a.status = 'Present'
                    GROUP BY a.student_id, se.course_id
                ) attended ON attended.student_id = s.user_id 
                           AND attended.course_id = c.course_id
                WHERE lc.lecturer_id = ?
                  AND c.course_id = ?
                ORDER BY s.reg_no
                """;

        try (Connection con = DataSource.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, lecturerId);
            ps.setString(2, courseId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double totalHours = rs.getDouble("total_hours");
                double attendedHours = rs.getDouble("attended_hours");

                double percentage = 0.0;
                if (totalHours > 0) {
                    percentage = (attendedHours / totalHours) * 100.0;
                }

                percentage = Math.round(percentage * 100.0) / 100.0;

                String status = percentage >= 80.0 ? "Eligible" : "Not Eligible";

                StudentEligibilityDTO dto = new StudentEligibilityDTO(
                        rs.getString("student_id"),
                        rs.getString("reg_no"),
                        rs.getString("student_name"),
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        totalHours,
                        attendedHours,
                        percentage,
                        status
                );

                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
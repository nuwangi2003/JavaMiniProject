package dao.gpa;

import model.BatchGPAReportRow;
import model.GPAReport;
import utility.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPADAO {

    public Double calculateSGPA(String studentId, int academicYear, int semester) {
        String sql = "SELECT cr.grade, c.course_credit " +
                "FROM course_result cr " +
                "INNER JOIN course c ON c.course_id = cr.course_id " +
                "WHERE cr.student_id = ? AND cr.academic_year = ? AND cr.semester = ?";

        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setInt(2, academicYear);
            ps.setString(3, String.valueOf(semester));

            try (ResultSet rs = ps.executeQuery()) {
                double qualityPoints = 0.0;
                int totalCredits = 0;

                while (rs.next()) {
                    String grade = rs.getString("grade");
                    int credit = rs.getInt("course_credit");
                    qualityPoints += gradeToPoints(grade) * credit;
                    totalCredits += credit;
                }

                if (totalCredits == 0) {
                    return null;
                }
                return round2(qualityPoints / totalCredits);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double calculateCGPA(String studentId) {
        String sql = "SELECT cr.grade, c.course_credit " +
                "FROM course_result cr " +
                "INNER JOIN course c ON c.course_id = cr.course_id " +
                "WHERE cr.student_id = ?";

        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                double qualityPoints = 0.0;
                int totalCredits = 0;

                while (rs.next()) {
                    String grade = rs.getString("grade");
                    int credit = rs.getInt("course_credit");
                    qualityPoints += gradeToPoints(grade) * credit;
                    totalCredits += credit;
                }

                if (totalCredits == 0) {
                    return null;
                }
                return round2(qualityPoints / totalCredits);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GPAReport> getStudentGPAReport(String studentId) {
        List<GPAReport> reports = new ArrayList<>();

        String semesterResultSql = "SELECT student_id, academic_year, semester, sgpa, cgpa " +
                "FROM semester_result WHERE student_id = ? ORDER BY academic_year, semester";

        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(semesterResultSql)) {

            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(new GPAReport(
                            rs.getString("student_id"),
                            rs.getInt("academic_year"),
                            Integer.parseInt(rs.getString("semester")),
                            rs.getDouble("sgpa"),
                            rs.getDouble("cgpa")
                    ));
                }
            }

            if (!reports.isEmpty()) {
                return reports;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deriveStudentGPAFromCourseResults(studentId);
    }

    public List<BatchGPAReportRow> getBatchGPAReport(String batch, int academicYear, int semester) {
        String studentsSql = "SELECT s.user_id, s.reg_no, u.username " +
                "FROM students s INNER JOIN users u ON u.user_id = s.user_id " +
                "WHERE s.batch = ? ORDER BY s.reg_no";

        List<BatchGPAReportRow> rows = new ArrayList<>();

        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(studentsSql)) {

            ps.setString(1, batch);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String studentId = rs.getString("user_id");
                    String regNo = rs.getString("reg_no");
                    String name = rs.getString("username");

                    Double sgpa = calculateSGPA(studentId, academicYear, semester);
                    Double cgpa = calculateCGPA(studentId);

                    rows.add(new BatchGPAReportRow(studentId, regNo, name, sgpa, cgpa));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private List<GPAReport> deriveStudentGPAFromCourseResults(String studentId) {
        String sql = "SELECT academic_year, semester, grade, c.course_credit " +
                "FROM course_result cr " +
                "INNER JOIN course c ON c.course_id = cr.course_id " +
                "WHERE student_id = ? ORDER BY academic_year, semester";

        List<GPAReport> reports = new ArrayList<>();

        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                Map<String, double[]> map = new HashMap<>();

                while (rs.next()) {
                    int year = rs.getInt("academic_year");
                    int sem = Integer.parseInt(rs.getString("semester"));
                    String key = year + "-" + sem;
                    double[] agg = map.computeIfAbsent(key, k -> new double[]{0.0, 0.0});
                    double points = gradeToPoints(rs.getString("grade"));
                    int credit = rs.getInt("course_credit");
                    agg[0] += points * credit;
                    agg[1] += credit;
                }

                double cumulativeQp = 0.0;
                double cumulativeCr = 0.0;
                for (Map.Entry<String, double[]> entry : map.entrySet()) {
                    String[] y = entry.getKey().split("-");
                    int year = Integer.parseInt(y[0]);
                    int sem = Integer.parseInt(y[1]);
                    double[] agg = entry.getValue();

                    Double sgpa = agg[1] == 0 ? null : round2(agg[0] / agg[1]);
                    cumulativeQp += agg[0];
                    cumulativeCr += agg[1];
                    Double cgpa = cumulativeCr == 0 ? null : round2(cumulativeQp / cumulativeCr);

                    reports.add(new GPAReport(studentId, year, sem, sgpa, cgpa));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reports;
    }

    private double gradeToPoints(String grade) {
        if (grade == null) {
            return 0.0;
        }
        return switch (grade.trim().toUpperCase()) {
            case "A+", "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "C-" -> 1.7;
            case "D+" -> 1.3;
            case "D" -> 1.0;
            default -> 0.0;
        };
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}

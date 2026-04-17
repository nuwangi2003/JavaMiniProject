package service.gpa;

import dao.gpa.GPADAO;
import model.BatchGPAReportRow;
import model.GPAReport;

import java.util.List;

public class GPAService {

    private final GPADAO dao;

    public GPAService(GPADAO dao) {
        this.dao = dao;
    }

    public Double calculateSGPA(String studentId, int academicYear, int semester) {
        return dao.calculateSGPA(studentId, academicYear, semester);
    }

    public Double calculateCGPA(String studentId) {
        return dao.calculateCGPA(studentId);
    }

    public List<GPAReport> getStudentGPAReport(String studentId) {
        return dao.getStudentGPAReport(studentId);
    }

    public List<BatchGPAReportRow> getBatchGPAReport(String batch, int academicYear, int semester) {
        return dao.getBatchGPAReport(batch, academicYear, semester);
    }

    public GPAReport getMyLatestGPA(String studentId) {
        List<GPAReport> reports = dao.getStudentGPAReport(studentId);
        if (reports == null || reports.isEmpty()) {
            return null;
        }
        return reports.get(reports.size() - 1);
    }
}

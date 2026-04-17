package service.report;

import dao.report.AcademicReportDAO;
import model.AcademicReportRow;

import java.util.List;

public class AcademicReportService {

    private final AcademicReportDAO dao;

    public AcademicReportService(AcademicReportDAO dao) {
        this.dao = dao;
    }

    public List<AcademicReportRow> getStudentFullAcademicReport(String studentId) {
        return dao.getStudentFullAcademicReport(studentId);
    }

    public List<AcademicReportRow> getBatchFullAcademicReport(String batch) {
        return dao.getBatchFullAcademicReport(batch);
    }
}

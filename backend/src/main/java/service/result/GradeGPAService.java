package service.result;

import dao.result.GradeGPADAO;
import dto.requestDto.result.GradeGPAFilterDTO;
import dto.responseDto.result.GradeGPAReportDTO;
import dto.responseDto.result.GradeGPARowDTO;

import java.util.List;

public class GradeGPAService {

    private final GradeGPADAO dao = new GradeGPADAO();

    public GradeGPAReportDTO generate(GradeGPAFilterDTO dto) {
        return dao.generateReport(dto);
    }
    public boolean saveSemesterResults(GradeGPAFilterDTO filter, List<GradeGPARowDTO> rows) {
        return dao.saveSemesterResults(filter, rows);
    }
}
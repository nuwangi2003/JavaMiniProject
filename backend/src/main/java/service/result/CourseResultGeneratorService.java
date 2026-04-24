package service.result;

import dao.result.CourseResultGeneratorDAO;
import dto.requestDto.result.GenerateCourseResultReqDTO;

public class CourseResultGeneratorService {

    private final CourseResultGeneratorDAO dao = new CourseResultGeneratorDAO();

    public int generateCourseResults(GenerateCourseResultReqDTO dto) {
        if (dto.getCourseId() == null || dto.getCourseId().isBlank()) {
            return 0;
        }

        if (dto.getAcademicYear() <= 0) {
            return 0;
        }

        if (dto.getAcademicLevel() < 1 || dto.getAcademicLevel() > 4) {
            return 0;
        }

        if (dto.getSemester() == null || dto.getSemester().isBlank()) {
            return 0;
        }

        return dao.generateCourseResults(dto);
    }
}
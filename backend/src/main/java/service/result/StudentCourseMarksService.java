package service.result;

import dao.result.StudentCourseMarksDAO;
import dto.responseDto.result.StudentCourseMarksDTO;

import java.util.List;

public class StudentCourseMarksService {

    private final StudentCourseMarksDAO dao = new StudentCourseMarksDAO();

    public List<StudentCourseMarksDTO> getAllStudentCourseMarks() {
        return dao.getAllStudentCourseMarks();
    }
}
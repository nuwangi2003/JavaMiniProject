package service.eligibility;

import dao.eligibility.CAEligibilityDAO;
import dto.responseDto.eligibility.CAEligibilityDTO;

import java.util.List;

public class CAEligibilityService {

    private final CAEligibilityDAO dao = new CAEligibilityDAO();

    public List<CAEligibilityDTO> getCAEligibility(String lecturerId, String courseId) {
        return dao.getCAEligibility(lecturerId, courseId);
    }
}
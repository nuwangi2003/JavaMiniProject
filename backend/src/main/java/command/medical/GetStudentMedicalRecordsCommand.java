package command.medical;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import dto.requestDto.medical.GetStudentMedicalRecordsRequestDTO;
import dto.responseDto.medical.MedicalResponseDTO;
import model.Medical;
import service.medical.MedicalService;

import java.util.List;

public class GetStudentMedicalRecordsCommand implements Command {
    private final MedicalService medicalService;
    private final ObjectMapper mapper = new ObjectMapper();

    public GetStudentMedicalRecordsCommand(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            if (!isAllowedReader(context.getRole())) {
                context.getOutput().println("{\"success\":false,\"message\":\"Forbidden: insufficient role\"}");
                return;
            }
            GetStudentMedicalRecordsRequestDTO request = mapper.convertValue(data, GetStudentMedicalRecordsRequestDTO.class);
            String studentId = resolveStudentId(request, context);

            if (studentId == null || studentId.isBlank()) {
                context.getOutput().println("{\"success\":false,\"message\":\"Student ID is required\"}");
                return;
            }

            List<Medical> records = medicalService.getStudentMedicalRecords(studentId);
            MedicalResponseDTO response = new MedicalResponseDTO(true, "Student medical records fetched", records);
            context.getOutput().println(mapper.writeValueAsString(response));
        } catch (Exception e) {
            e.printStackTrace();
            context.getOutput().println("{\"success\":false,\"message\":\"Server error\"}");
        }
    }

    private boolean isAllowedReader(String role) {
        return "Student".equalsIgnoreCase(role)
                || "Tech_Officer".equalsIgnoreCase(role)
                || "Admin".equalsIgnoreCase(role)
                || "Dean".equalsIgnoreCase(role);
    }

    private String resolveStudentId(GetStudentMedicalRecordsRequestDTO request, ClientContext context) {
        if ("Student".equalsIgnoreCase(context.getRole())) {
            return context.getUserId();
        }
        if (request == null) {
            return null;
        }
        return request.getStudentId();
    }
}

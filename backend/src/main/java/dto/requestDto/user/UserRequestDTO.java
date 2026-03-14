package dto.requestDto.user;

public class UserRequestDTO {

    // common fields
    private String username;
    private String email;
    private String password;
    private String contactNumber;
    private String profilePicture;
    private String role;

    // student-specific fields
    private String regNo;
    private String batch;
    private Integer academicLevel;
    private String departmentId;

    // lecturer-specific fields
    private String specialization;
    private String designation;

    // tech officer-specific
    private String techDepartmentId;

    // getters and setters for all fields
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }

    public Integer getAcademicLevel() { return academicLevel; }
    public void setAcademicLevel(Integer academicLevel) { this.academicLevel = academicLevel; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getTechDepartmentId() { return techDepartmentId; }
    public void setTechDepartmentId(String techDepartmentId) { this.techDepartmentId = techDepartmentId; }
}
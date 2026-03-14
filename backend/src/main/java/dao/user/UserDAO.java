package dao.user;

import model.Lecturer;
import model.Student;
import model.TechOfficer;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // Find by username & password (login)
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT user_id, username, password, role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all users
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("contact_number"),
                        rs.getString("profile_picture"),
                        rs.getString("role")
                );
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // Create user (with role-specific tables if needed)
    public User createUser(User user) {
        String sqlUser = "INSERT INTO users " +
                "(user_id, username, email, password, contact_number, profile_picture, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false); // start transaction

            try (PreparedStatement ps = connection.prepareStatement(sqlUser)) {
                ps.setString(1, user.getUserId());
                ps.setString(2, user.getUsername().trim());
                ps.setString(3, user.getEmail() != null ? user.getEmail().trim() : null);
                ps.setString(4, user.getPassword().trim());
                ps.setString(5, user.getContactNumber() != null ? user.getContactNumber().trim() : null);
                if (user.getProfilePicture() != null)
                    ps.setString(6, user.getProfilePicture());
                else
                    ps.setNull(6, java.sql.Types.VARCHAR);
                ps.setString(7, user.getRole().trim());

                int rows = ps.executeUpdate();
                if (rows == 0) {
                    connection.rollback();
                    return null;
                }
            }

            // Role-specific insert (skip for Admin)
            insertRoleData(user);

            connection.commit();
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            try { connection.rollback(); } catch (Exception ignored) {}
            return null;
        } finally {
            try { connection.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }

    private void insertRoleData(User user) throws Exception {
        String role = user.getRole().trim();
        switch (role) {
            case "Student" -> insertStudent((Student) user);
            case "Lecturer" -> insertLecturer((Lecturer) user);
            case "Tech_Officer" -> insertTechOfficer((TechOfficer) user);
            case "Admin" -> {
                // Admin has no extra table; do nothing
            }
            default -> throw new Exception("Unknown role: " + role);
        }
    }

    private void insertStudent(Student student) throws Exception {
        String sql = "INSERT INTO students (user_id, reg_no, batch, academic_level, department_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, student.getUserId());
            ps.setString(2, student.getRegNo());
            ps.setString(3, student.getBatch());
            ps.setInt(4, student.getAcademicLevel());
            ps.setString(5, student.getDepartmentId());
            ps.executeUpdate();
        }
    }

    private void insertLecturer(Lecturer lecturer) throws Exception {
        String sql = "INSERT INTO lecturers (user_id, specialization, designation) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, lecturer.getUserId());
            ps.setString(2, lecturer.getSpecialization());
            ps.setString(3, lecturer.getDesignation());
            ps.executeUpdate();
        }
    }

    private void insertTechOfficer(TechOfficer officer) throws Exception {
        String sql = "INSERT INTO tech_officers (user_id, department_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, officer.getUserId());
            ps.setString(2, officer.getDepartmentId());
            ps.executeUpdate();
        }
    }

    // Get user by ID
    public User getUserById(String userId) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("contact_number"),
                        rs.getString("profile_picture"),
                        rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
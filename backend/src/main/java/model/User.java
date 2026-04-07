package model;

public class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String contactNumber;
    private String profilePicture;
    private String role; // Student, Lecturer, Dean, Tech_Officer, Admin

    // Default constructor
    public User() {}

    public User(String userId,String username,String password,String role){
        this.userId = userId;
        this.username =  username;
        this.password = password;
        this.role = role;
    }


    public User(String userId, String username, String email, String password,
                String contactNumber, String profilePicture, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.contactNumber = contactNumber;
        this.profilePicture = profilePicture;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

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


}
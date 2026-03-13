package dto.responseDto;

public class LoginResponseDTO {
    private final boolean success;
    private final String username;
    private final String message;
    private final String role;
    private final String token;  // new field for JWT

    // Private constructor to enforce the use of Builder
    private LoginResponseDTO(Builder builder) {
        this.success = builder.success;
        this.username = builder.username;
        this.message = builder.message;
        this.role = builder.role;
        this.token = builder.token; // assign token
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;  // getter for JWT
    }

    // Static nested Builder class
    public static class Builder {
        private boolean success;
        private String username;
        private String message;
        private String role;
        private String token; // builder field for token

        public Builder setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setRoll(String role) {
            this.role = role;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public LoginResponseDTO build() {
            return new LoginResponseDTO(this);
        }
    }
}
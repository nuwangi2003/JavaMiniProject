package command.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import dto.requestDto.user.UserRequestDTO;
import service.user.UserService;
import service.login.AuthService;

public class CreateUserCommand implements Command {

    private final UserService userService;
    private final AuthService authService; // added
    private final ObjectMapper mapper = new ObjectMapper();

    public CreateUserCommand(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            String token = context.getToken();
            System.out.println("token : "+ token);
            if (token == null || !authService.isTokenValid(token)) {
                context.getOutput().println("{\"success\":false,\"message\":\"Unauthorized\"}");
                return;
            }

            UserRequestDTO dto = mapper.convertValue(data, UserRequestDTO.class);

            boolean success = userService.createUser(dto);

            if (success) {
                context.getOutput().println("{\"success\":true,\"message\":\"User created successfully\"}");
            } else {
                context.getOutput().println("{\"success\":false,\"message\":\"User creation failed\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            context.getOutput().println("{\"success\":false,\"message\":\"Server error\"}");
        }
    }
}
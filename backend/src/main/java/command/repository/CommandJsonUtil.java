package command.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public final class CommandJsonUtil {

    private CommandJsonUtil() {
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> asMap(Object data) {
        if (data instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return new HashMap<>();
    }

    public static void writeError(ObjectMapper mapper, ClientContext context, String message) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", message);
            context.getOutput().println(mapper.writeValueAsString(response));
        } catch (Exception ex) {
            context.getOutput().println("{\"success\":false,\"message\":\"" + message + "\"}");
        }
    }
}

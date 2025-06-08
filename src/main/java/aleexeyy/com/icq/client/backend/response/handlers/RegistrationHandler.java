package aleexeyy.com.icq.client.backend.response.handlers;
import aleexeyy.com.icq.shared.messages.ServerResponse;

public class RegistrationHandler {

    public static void handleRegistration(ServerResponse response) {
        if (response.getCode() == 200) {

        } else if (response.getCode() == 400) {

        }
    }
}

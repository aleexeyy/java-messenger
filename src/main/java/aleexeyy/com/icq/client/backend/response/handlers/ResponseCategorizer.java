package aleexeyy.com.icq.client.backend.response.handlers;

import aleexeyy.com.icq.client.ui.controllers.MessengerController;
import aleexeyy.com.icq.client.ui.controllers.RegistrationController;
import aleexeyy.com.icq.shared.messages.ServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Objects;

public class ResponseCategorizer {

    public static void categorizeResponse(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ServerResponse response = objectMapper.readValue(message, ServerResponse.class);

            if (Objects.equals(response.getType(), "private_message")) {

                MessageStatusUpdater.updateMessageStatus(response);

            } else if (Objects.equals(response.getType(), "registration")) {

                Platform.runLater(() -> {
                    try {
                        RegistrationController.handleRegistration(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } else if (Objects.equals(response.getType(), "search")) {
                Platform.runLater(() -> {
                    try {
                        MessengerController.displayUsers(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        } catch (JsonProcessingException e) {
            System.err.println("Invalid JSON received from server: " + e.getMessage());
        }
    }
}

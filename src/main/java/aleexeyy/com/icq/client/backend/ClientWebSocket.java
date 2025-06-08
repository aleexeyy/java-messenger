package aleexeyy.com.icq.client.backend;

import aleexeyy.com.icq.client.backend.response.handlers.MessageStatusUpdater;
import aleexeyy.com.icq.client.ui.controllers.MessengerController;
import aleexeyy.com.icq.client.ui.controllers.RegistrationController;
import aleexeyy.com.icq.server.message.handlers.MessageDTO;
import aleexeyy.com.icq.shared.messages.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

public class ClientWebSocket extends WebSocketClient{


    private static String currentUsername;

    public static String getCurrentUsername() {
        return currentUsername;
    }
    public ClientWebSocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            System.err.println("Received empty JSON message.");
            return;
        }
        System.out.println("Client received: " + message);
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {

            ServerResponse response = objectMapper.readValue(message, ServerResponse.class);

            if (Objects.equals(response.getType(), "load")) {
                String messageJson = response.getMessage();

                List<MessageDTO> messages = objectMapper.readValue(messageJson, new TypeReference<List<MessageDTO>>() {});
                Platform.runLater(() -> {
                    try {
                        MessengerController.updateChat(messages);
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            else if (Objects.equals(response.getType(), "private_message")) {
                MessageStatusUpdater.updateMessageStatus(response);
                Platform.runLater(() -> {
                    try {
                        MessengerController.sendNotification(response);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (Objects.equals(response.getType(), "successful_delivery")) {
                Platform.runLater(() -> {
                    try {
                        MessengerController.showSentMessage(response);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            else if (Objects.equals(response.getType(), "registration")) {
                Platform.runLater(() -> {
                    try {
                        RegistrationController.handleRegistration(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }  else if (Objects.equals(response.getType(), "search")) {
                Platform.runLater(() -> {
                    try {
                        MessengerController.displayUsers(response);
                    } catch (IOException e) {
                        throw new RuntimeException("Error during search for Users: " + e);
                    }
                });
            }
        } catch (JsonProcessingException e) {
            System.err.println("Invalid JSON received from server: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.printf("Connection closed: Code=%d Reason=%s Remote=%b%n", code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error occurred: " + ex.getMessage());
    }

    public static void sendMessage(String message, String recipient) {
        ClientWebSocket client = WebSocketManager.getClient();
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setContent(message);
        privateMessage.setFrom(String.valueOf(client.getLocalSocketAddress()));
        privateMessage.setTo(recipient);

        client.send(String.valueOf(privateMessage));
    }

    public static void loadMessages(String selectedUsername) {
        ClientWebSocket client = WebSocketManager.getClient();
        LoadMessage message = new LoadMessage();
        message.setParticipantUsername(selectedUsername);
        message.setSenderUsername(currentUsername);
        message.setFrom(String.valueOf(client.getLocalSocketAddress()));
        client.send(String.valueOf(message));

    }


    public static void searchUser(String query) {
        ClientWebSocket client = WebSocketManager.getClient();
        SearchUserMessage message = new SearchUserMessage(String.valueOf(client.getLocalSocketAddress()), query);
        client.send(String.valueOf(message));
    }

    public static void registerNewUser(URI serverUri, String username, String password) {
        try {
            WebSocketManager.init(serverUri);
            ClientWebSocket client = WebSocketManager.getClient();
            String from = String.valueOf(client.getLocalSocketAddress());
            RegistrationMessage registrationMessage = new RegistrationMessage(from, username, password);
            currentUsername = username;
            client.send(String.valueOf(registrationMessage));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

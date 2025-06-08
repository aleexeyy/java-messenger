package aleexeyy.com.icq.client.ui.controllers;

import aleexeyy.com.icq.client.backend.ClientWebSocket;
import aleexeyy.com.icq.client.ui.SceneManager;
import aleexeyy.com.icq.server.message.handlers.ConversationService;
import aleexeyy.com.icq.server.message.handlers.MessageDTO;
import aleexeyy.com.icq.shared.messages.ServerResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MessengerController extends BaseController {


    public static void displayUsers(ServerResponse response) throws IOException {
        if (response.getCode() != 200) return;
        if (response.getMessage().isBlank() || response.getMessage() == null) return;
        String[] parts = response.getMessage().split(",");
        List<String> userNames = Arrays.stream(parts)
                .map(String::trim)
                .filter(s -> !s.isEmpty()) // filter out empty entries
                .collect(Collectors.toList());
        if (SceneManager.getCurrentController() instanceof MessengerController controller) {
            controller.suggestionsListView.getItems().setAll(userNames);
            controller.suggestionsListView.setVisible(!userNames.isEmpty());
        }

    }

    private final Set<String> highlightedChats = new HashSet<>();


    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> suggestionsListView;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private HBox messageInputBox;

    @FXML
    private VBox chatBox;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextField messageTextField;

    private final ObservableList<String> suggestions = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        suggestionsListView.setItems(suggestions);
        suggestionsListView.setVisible(false);

        suggestionsListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        openChatWith(newVal);
                        suggestions.clear();
                        suggestionsListView.setVisible(false);
                        searchField.clear();
                    }
                });
        chatListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                openChatWith(newVal);
                highlightedChats.remove(newVal);
                chatListView.refresh();
            }
        });

        chatListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (highlightedChats.contains(item)) {
                        setStyle("-fx-font-weight: bold; -fx-background-color: #4287f5;");
                    } else {
                        setStyle(""); // normal style
                    }
                }
            }
        });

        chatListView.getItems().addAll(ConversationService.getAllConversationIds(ClientWebSocket.getCurrentUsername()));

    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isBlank()) {
            suggestionsListView.setVisible(false);
            return;
        }

        ClientWebSocket.searchUser(query);
    }

    private void openChatWith(String username) {
        if (!chatListView.getItems().contains(username)) {
            chatListView.getItems().add(username);
            return;
        }
        chatListView.getSelectionModel().select(username);
        messageInputBox.setVisible(true);
        chatBox.getChildren().clear();

        loadMessages(username);
    }

    @FXML
    private void handleSendMessage(ActionEvent event) {
        String message = messageTextField.getText();
        String selectedUsername = chatListView.getSelectionModel().getSelectedItem();
        if (message == null || message.isBlank() || selectedUsername == null || selectedUsername.isBlank()) {
            return;
        }
        messageTextField.clear();
        ClientWebSocket.sendMessage(message, selectedUsername);


    }
    public static void showSentMessage(ServerResponse response) {
        if (response.getCode() != 200)  return;
        String[] parts = response.getData().split(",");
        List<String> userNames = Arrays.stream(parts)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        if (userNames.size() != 2) return;
        String targetUsername = userNames.get(0);
        if (SceneManager.getCurrentController() instanceof MessengerController controller) {
            controller.loadMessages(targetUsername);
        }
        return;
    }

    public static void sendNotification(ServerResponse response) {
        String fromUser = response.getMessage();
        if (SceneManager.getCurrentController() instanceof MessengerController controller) {
            controller.highlightChat(fromUser);
            controller.loadMessages(fromUser);
        }

        return;
    }

    public void highlightChat(String username) {
        if (!highlightedChats.contains(username)) {
            highlightedChats.add(username);
            if (!chatListView.getItems().contains(username)) {
                chatListView.getItems().add(username);
            }
            chatListView.refresh(); // Forces UI to re-render and apply style
        }
    }

    public static void updateChat(List<MessageDTO> messages) {
        if (!(SceneManager.getCurrentController() instanceof MessengerController controller)) return;

        Platform.runLater(() -> {
            controller.chatBox.getChildren().clear();

            for (MessageDTO message : messages) {
                HBox messageContainer = new HBox();
                Label messageLabel = new Label(message.content());
                Label senderLabel = new Label(message.senderUsername() + " • " + message.sentAt());

                VBox messageBubble = new VBox(senderLabel, messageLabel);
                messageBubble.setStyle("-fx-background-color: #a6a3a2; -fx-background-radius: 10; -fx-padding: 8;");
                senderLabel.setStyle("-fx-font-size: 10; -fx-text-fill: gray;");
                messageLabel.setStyle("-fx-font-size: 14;");

                // Different alignment if it's the current user
                if (message.senderUsername().equals(ClientWebSocket.getCurrentUsername())) {
                    messageContainer.setAlignment(Pos.CENTER_RIGHT);
                    messageBubble.setStyle("-fx-background-color: #c0ffc0; -fx-background-radius: 10; -fx-padding: 8;");
                } else {
                    messageContainer.setAlignment(Pos.CENTER_LEFT);
                }

                messageContainer.getChildren().add(messageBubble);
                controller.chatBox.getChildren().add(messageContainer);
            }

            // Scroll to bottom
            controller.chatScrollPane.layout();
            controller.chatScrollPane.setVvalue(1.0);
        });
    }


    public void loadMessages(String selectedUsername) {
        ClientWebSocket.loadMessages(selectedUsername);
    }
}

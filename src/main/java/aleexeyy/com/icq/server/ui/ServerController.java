package aleexeyy.com.icq.server.ui;

import aleexeyy.com.icq.server.ServerApp;
import aleexeyy.com.icq.server.message.handlers.ConversationService;
import aleexeyy.com.icq.server.message.handlers.LoadHandler;
import aleexeyy.com.icq.server.message.handlers.MessageDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.*;
import java.util.stream.Collectors;

public class ServerController {

    private static ServerController INSTANCE;

    public static ServerController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerController();
        }
        return INSTANCE;
    }

    @FXML private Button   startServerButton;
    @FXML private Button   stopServerButton;
    @FXML private Label    statusLabel;
    @FXML private ListView<String> conversationListView;
    @FXML private ListView<String> messageListView;

    private ServerApp serverApp;

    private static final ObservableList<String> conversations = FXCollections.observableArrayList();

    private static final ObservableList<String> messages = FXCollections.observableArrayList();

    private static final Map<String, Long> displayToId = new HashMap<>();

    @FXML
    private void initialize() {

        INSTANCE = this;

        conversationListView.setItems(conversations);
        messageListView.setItems(messages);

        conversationListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        Long convId = displayToId.get(newVal);
                        loadMessagesFor(convId);
                    }
                });

        stopServerButton.setDisable(true);
    }

    public static int getEnvPort(String varName, int defaultPort) {
        String value = System.getenv(varName);
        if (value == null || value.isBlank()) {
            return defaultPort;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // log a warning if needed
            System.err.println("Warning: invalid port '" + value +
                    "', using default " + defaultPort);
            return defaultPort;
        }
    }

    @FXML
    private void handleStartServer(ActionEvent e) {
        try {

            int port = getEnvPort("WEBSOCKET_PORT", 7080);
            serverApp = new ServerApp(port);
            serverApp.start();

            statusLabel.setText("Server running on port " + serverApp.getPort());
            startServerButton.setDisable(true);
            stopServerButton.setDisable(false);

            refreshConversationList();
        } catch (Exception ex) {
            statusLabel.setText("Error starting server");
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleStopServer(ActionEvent e) {
        try {
            if (serverApp != null) serverApp.stop();

            statusLabel.setText("Server stopped");
            stopServerButton.setDisable(true);
            startServerButton.setDisable(false);

            // clear UI
            conversations.clear();
            messages.clear();
            displayToId.clear();
        } catch (Exception ex) {
            statusLabel.setText("Error stopping server");
            ex.printStackTrace();
        }
    }

    public static void refreshConversationList() {
        // 1) all convo IDs
        List<Long> convIds = ConversationService.getAllConversationIds();

        // 2) batch‐fetch all participants for those IDs
        Map<Long, List<String>> participantsByConv =
                ConversationService.getAllConversationParticipants(convIds);

        // 3) build display strings and mapping
        Map<String, Long> newMap = new LinkedHashMap<>();
        List<String> displayNames = convIds.stream()
                .map(id -> {
                    List<String> names = participantsByConv.getOrDefault(id, List.of());
                    // join e.g. ["alice","bob"] → "alice ↔ bob"
                    String disp = String.join(" ↔ ", names);
                    newMap.put(disp, id);
                    return disp;
                })
                .collect(Collectors.toList());

        // 4) update UI on FX thread
        Platform.runLater(() -> {
            displayToId.clear();
            displayToId.putAll(newMap);
            conversations.setAll(displayNames);
        });
    }

    private String getSelectedConversationDisplay() {
        return conversationListView
                .getSelectionModel()
                .getSelectedItem();
    }

    private Long getSelectedConversationId() {
        String disp = getSelectedConversationDisplay();
        return (disp != null ? displayToId.get(disp) : null);
    }

    public void loadMessagesFor(Long conversationId) {

        if (conversationId == null || !Objects.equals(getSelectedConversationId(), conversationId)) return;

        List<MessageDTO> dtos = LoadHandler.loadMessages(conversationId);

        // map to simple "sender: content" strings
        List<String> display = dtos.stream()
                .map(m -> m.senderUsername() + ": " + m.content())
                .collect(Collectors.toList());

        Platform.runLater(() -> {
            messages.setAll(display);
            if (!display.isEmpty()) {
                messageListView.scrollTo(display.size() - 1);
            }
        });
    }
}

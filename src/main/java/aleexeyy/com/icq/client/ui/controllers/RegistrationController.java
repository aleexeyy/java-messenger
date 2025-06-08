package aleexeyy.com.icq.client.ui.controllers;

import aleexeyy.com.icq.client.backend.ClientWebSocket;
import aleexeyy.com.icq.client.ui.SceneManager;
import aleexeyy.com.icq.shared.messages.ServerResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class RegistrationController extends BaseController {
    public static void showNicknameExistsError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText("Nickname Already Exists");
        alert.setContentText("The nickname you entered is already taken or the password is wrong. Please try a different one.");
        alert.showAndWait();
    }

    public static void showRegistrationSuccess(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(msg);
        alert.setHeaderText(null);
        if (Objects.equals(msg, "Login successful")) {
            alert.setContentText("You have been Logged In successfully!");
        } else {
            alert.setContentText("You have been Registered successfully!");
        }
        alert.showAndWait();
    }

    public static void handleRegistration(ServerResponse response) throws IOException {
        if (response.getCode() == 200) {
            showRegistrationSuccess(response.getMessage());
            SceneManager.switchScene("main-menu.fxml");

        } else if (response.getCode() == 400) {
            showNicknameExistsError();
        }
    }

    @FXML
    private TextField serverAddressField;

    @FXML
    private TextField nicknameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;


    @FXML
    private void initialize() {
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: #5b6eae; -fx-text-fill: white; -fx-font-weight: bold;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: #7289da; -fx-text-fill: white; -fx-font-weight: bold;"));
        registerButton.setOnAction(event -> register());
    }

    private void register() {
        String serverAddress = serverAddressField.getText();
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        if (serverAddress.isEmpty() || nickname.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill all fields.");
            return;
        }

        URI serverUri = URI.create(serverAddress);
        ClientWebSocket.registerNewUser(serverUri, nickname, password);
        System.out.printf("Connecting to: %s\nNickname: %s\nPassword: %s\n",
                serverAddress, nickname, password);

    }
}

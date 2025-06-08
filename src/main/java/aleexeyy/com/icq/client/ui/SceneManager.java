package aleexeyy.com.icq.client.ui;

import aleexeyy.com.icq.client.ui.controllers.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage currentStage;
    private static BaseController currentController;

    public static BaseController getCurrentController() {
        return currentController;
    }

    public static void setCurrentController(BaseController controller) {
        currentController = controller;
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void setStage(Stage stage) {
        currentStage = stage;
    }
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
            Parent root = loader.load();
            currentStage.setScene(new Scene(root));
            currentController = loader.getController();
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

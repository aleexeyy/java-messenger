package aleexeyy.com.icq.server.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerAppUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1) Load your server_ui.fxml
        FXMLLoader loader = new FXMLLoader(ServerAppUI.class.getResource("/aleexeyy.com.icq.server.ui/server-ui.fxml"));

        Scene scene = new Scene(loader.load());

        // 3) Set up the stage
        primaryStage.setTitle("ICQ Server Control Panel");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // This will bootstrap JavaFX, call start(), etc.
        launch(args);
    }
}

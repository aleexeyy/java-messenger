package aleexeyy.com.icq.client.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("registration-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("ICQ");
        stage.setScene(scene);
        stage.show();
    }
}

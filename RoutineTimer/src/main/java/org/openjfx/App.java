package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage homeWindow;
    private static Scene primaryScene;

    @Override
    public void start(Stage stage) throws IOException {
        primaryScene = new Scene(loadFXML("/sample"));
        primaryScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(primaryScene);
        stage.setMinHeight(480);
        stage.setMinWidth(640);
        homeWindow = stage;
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        primaryScene.setRoot(loadFXML(fxml));
    }

    static void minimizeWindow(boolean flag){
        homeWindow.setIconified(flag);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch();
    }

}
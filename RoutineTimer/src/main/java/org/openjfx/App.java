package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage homeWindow;
    private static Scene primaryScene;
    public static Controller controllerHomeWindow;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/Home" + ".fxml"));
        primaryScene = new Scene(fxmlLoader.load());
        primaryScene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
        stage.setScene(primaryScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/timerIcon.png")));

        homeWindow = stage;
        // Nel momento in cui viene chiusa la finistra vengono salavate le impostazioni
        homeWindow.setOnCloseRequest(e -> {
            if (controllerHomeWindow.saveCheckBox.isSelected()) {
                controllerHomeWindow.saveSetting();
            }
        });
        controllerHomeWindow = fxmlLoader.getController();
        stage.setTitle("Timer");
        // Per settare la grandezza minima della finistra
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());

        stage.show();
    }


    static void setRoot(String fxml) throws IOException {
        primaryScene.setRoot(loadFXML(fxml));
    }

    static void minimizeWindow(boolean flag) {
        homeWindow.setIconified(flag);
    }



    private static Parent loadFXML(String fxml) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
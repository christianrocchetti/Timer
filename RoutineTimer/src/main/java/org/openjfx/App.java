package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.harawata.appdirs.AppDirsFactory;
import org.json.JSONObject;


import java.io.IOException;
import java.nio.file.Paths;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage homeWindow;
    private static Controller homeController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/Home" + ".fxml"));
        Parent sceneRoot = fxmlLoader.load();
        Scene primaryScene = new Scene(sceneRoot);
        primaryScene.setRoot(sceneRoot);
        homeController = fxmlLoader.getController();
        primaryScene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
        stage.setScene(primaryScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/timerIcon.png")));
        homeWindow = stage;
        // Nel momento in cui viene chiusa la finistra vengono salavate le impostazioni
        homeWindow.setOnCloseRequest(e -> {
            if (homeController.saveCheckBox.isSelected()) {
                if(getHomeController().saveCheckBox.isSelected()){
                    JSONObject settingJson = new JSONObject();
                    String appPahtSettingSting = AppDirsFactory.getInstance()
                            .getUserDataDir(null, null, "appTimerJava");

                    String inputHours = App.getHomeController().hoursTextFiled.getText();
                    String inputMinutes = App.getHomeController().minutesTextFiled.getText();
                    String inputSeconds = App.getHomeController().secondsTextFiled.getText();
                    // Nel caso in cui l'untente ha inserito uno spazio bianco o non inserito nulla,
                    // input viene sostituito con un 00
                    inputHours = inputHours.replaceAll("\\s{0}^(?!.)|^\\s+|^0+", "00");
                    inputMinutes = inputMinutes.replaceAll("\\s{0}^(?!.)|^\\s+|^0+", "00");
                    inputSeconds = inputSeconds.replaceAll("\\s{0}^(?!.)|^\\s+|^0+", "00");

                    settingJson.put("Hours", inputHours);
                    settingJson.put("Minutes", inputMinutes);
                    settingJson.put("Seconds", inputSeconds);
                    settingJson.put("Message", App.getHomeController().messageField.getText());
                    settingJson.put("Minimize", Boolean.toString(App
                            .getHomeController().minimizeCheckBox.isSelected()));
                    settingJson.put("Sound", App.getHomeController().soundComboBox.getValue());
                    settingJson.put("Save", Boolean.toString(App.getHomeController().saveCheckBox.isSelected()));

                    Utils.setSetting(Paths.get(appPahtSettingSting + "/setting.json"), settingJson);
                }

            }
        });

        stage.setTitle("Timer");


        stage.show();

        // Per settare la grandezza minima della finistra
        // !! deve essere settata dopo "stage.show" !!
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }


    static void minimizeWindow(boolean flag) {
        homeWindow.setIconified(flag);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Controller getHomeController(){
        return homeController;
    }


}
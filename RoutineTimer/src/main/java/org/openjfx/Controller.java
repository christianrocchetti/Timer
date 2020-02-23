package org.openjfx;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.util.StringConverter;
import net.harawata.appdirs.AppDirsFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.UnaryOperator;


public class Controller {

    @FXML
    public JFXCheckBox minimizeCheckBox;
    @FXML
    public JFXCheckBox saveCheckBox;
    @FXML
    public Pane root;
    @FXML
    public JFXButton reset;
    @FXML
    public TextField messageField;
    @FXML
    public JFXTextField hoursTextFiled;
    @FXML
    public JFXTextField minutesTextFiled;
    @FXML
    public JFXTextField secondsTextFiled;
    @FXML
    public Text hoursCounter;
    @FXML
    public Text minutesCounter;
    @FXML
    public Text secondsCounter;
    @FXML
    public JFXButton playStop;
    @FXML
    public JFXComboBox<String> soundComboBox;

    private HashMap<String, String> soundMap = new HashMap<>();

    public final String svgPlay =
            "M2.88235294,0.149128824 C1.29047347,0.149128824 0,1.43960353 0,3.03148176 L0,31.8550235" +
                    " C0,33.4469059 1.29047347,34.7373765 2.88235294,34.7373765 C4.47423241,34.7373765 27.4815351,20.00" +
                    "25824 28,19.5020824 C28.5430262,18.9779059 28.8235294,18.2576471 28.8235294,17.4432588 C28.8235294" +
                    ",16.6288706 28.5430262,15.9086076 28,15.3844312 C27.4815351,14.8839724 4.47423241,0.149128824 2.88" +
                    "235294,0.149128824 L2.88235294,0.149128824 Z";
    public final String svgStop = "M8,2 L8,18 C8,19.105 7.105,20 6,20 L2,20 C0.895,20 0,19.105 0,18 L0,2 C0,0.895 0.895," +
            "0 2,0 L6,0 C7.105,0 8,0.895 8,2 Z M18,0 L14,0 C12.895,0 12,0.895 12,2 L12,18 C12,19.105 12.895,2" +
            "0 14,20 L18,20 C19.105,20 20,19.105 20,18 L20,2 C20,0.895 19.105,0 18,0 Z";
    public final String svgRest = "M22,12 L25.406,8.594 C23.031,5.656 19.602,4 16,4 C9.371,4 4,9.375 4,16 C4,22" +
            ".629 9.371,28 16,28 C19.602,28 22.82,26.406 25.02,23.895 L28.028,26.528 C25.094,29.875 20.801" +
            ",32 16,32 C7.164,32 0,24.836 0,16 C0,7.164 7.164,0 16,0 C20.801,0 25.332,2.387 28.266,5.734 L" +
            "32,2 L32,12 L22,12 Z";

    private Timer timer = new Timer();


    public String getPathSoundMap(String key) {
        return soundMap.get(key);
    }

    /**
     *
     */
    private void setSettingFromFileGUI() {
        String appPahtSettingString = AppDirsFactory.getInstance().getUserDataDir(null, null, "appTimerJava");
        Path appPahtSettingDirctory = Paths.get(appPahtSettingString);
        Path appPathSettingFile = Paths.get(appPahtSettingString + "/setting.json");
        JSONObject settingJsonDefault = new JSONObject();

        settingJsonDefault.put("Hours", "00");
        settingJsonDefault.put("Minutes", "00");
        settingJsonDefault.put("Seconds", "00");
        settingJsonDefault.put("Message", "Default Message");
        settingJsonDefault.put("Minimize", "true");
        settingJsonDefault.put("Save", "false");
        settingJsonDefault.put("Sound", "No Sound");

        Utils.checkDirectorySetting(appPahtSettingDirctory);
        Utils.checkFileSettings(appPathSettingFile, settingJsonDefault);
        try {
            String setting = new String(Files.readAllBytes(appPathSettingFile));

            JSONObject settingJson = new JSONObject(setting);
            hoursTextFiled.setText(settingJson.getString("Hours"));
            minutesTextFiled.setText(settingJson.getString("Minutes"));
            secondsTextFiled.setText(settingJson.getString("Seconds"));
            messageField.setText(settingJson.getString("Message"));
            minimizeCheckBox.setSelected(settingJson.getBoolean("Minimize"));
            saveCheckBox.setSelected(settingJson.getBoolean("Save"));
            soundComboBox.setValue(settingJson.getString("Sound"));
        } catch (IOException e) {
            Error.errorWotSolution(e, "Fatal error");
        } catch (JSONException e) {
            Utils.setSetting(appPathSettingFile, settingJsonDefault);
        }
    }


    @FXML
    public void inputButtonPlayStop(ActionEvent mouseEvent) throws Exception {
        // E' nello stato "STARTED" quindi nel momento in cui viene richimata la funzione
        // passa allo stato "STOPPED"
        switch (timer.getState()) {
            // Se è nello stato di INITIALIZED il timer viene inizzializzato e le animazione avviate
            case INITIALIZED:
                if(timer.timerInitialization()){
                    timer.timerPlay();
                }
                break;
            // Se è nello stato di STARTED l'animazioni vengono stoppate
            // e lo fare entrare un stato di STOPPED
            case STARTED:
                timer.timerStop();
                break;
            // Se è nello stato di STOPPED l'animazioni vengono riavviate
            // e lo fare entrare un stato di STARTED
            case STOPPED:
                timer.timerPlay();
                break;
        }
    }


    @FXML
    public void resetCounter(ActionEvent event) {
        timer.timerReset();
    }


    @FXML
    void initialize() {
        setSettingFromFileGUI();
        reset.setGraphic(Utils.covertSvgToRegion(svgRest, 40));
        playStop.setGraphic(Utils.covertSvgToRegion(svgPlay, 40));

        soundMap.put("Bird", "/sounds/BIRD1-2.wav");
        soundMap.put("Vintage Alarm Clock", "/sounds/Old_Alarm_Clock.wav");
        soundMap.put("Digital Alarm Clock", "/sounds/digital_alarm_clock.wav");
        soundMap.put("Sweet Sound", "/sounds/sweet_sound.wav");
        soundComboBox.getItems().addAll("No Sound", "Bird"
                , "Vintage Alarm Clock", "Digital Alarm Clock", "Sweet Sound");
        soundComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null && !newval.equals("No Sound"))
                Utils.playAudio(getPathSoundMap(newval));
        });

        // Rifuta la modifica nel caso non rispetti il Pattern
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            // text vede solo le modiche e non l'intera stringa
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        // Modifica il testo di TextFiled ogni volta che si cambia focus da TextField
        StringConverter<String> converter = new StringConverter<String>() {
            @Override
            public String fromString(String s) {
                // per mantenre il "00" visualizzato
                if (s.equals("00")) return "00";
                s = s.replaceAll("^0+", "");
                try {
                    if (!s.isEmpty() && Integer.parseInt(s) > 59) {
                        s = "59";
                    }
                } catch (Exception e) {
                    Error.errorWotSolution(e, "Error");
                }
                return s;
            }

            @Override
            public String toString(String d) {
                return d;
            }
        };



        TextFormatter<String> textFormatter = new TextFormatter<>(converter, secondsTextFiled.getText(), filter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(converter, minutesTextFiled.getText(), filter);
        TextFormatter<String> textFormatter3 = new TextFormatter<>(converter, hoursCounter.getText(), filter);

        secondsTextFiled.setTextFormatter(textFormatter);
        minutesTextFiled.setTextFormatter(textFormatter2);
        hoursTextFiled.setTextFormatter(textFormatter3);
    }


    @FXML
    void messageEmpy(ActionEvent event) {
        if (messageField.getText().equals("Default Message")) messageField.setText("");
    }

    // Quando vingono cliccati i TextFiel vengiene azzerato il contenuto
    @FXML
    public void setZeroHours(MouseEvent event) {
        if (hoursTextFiled.getText().equals("00")) hoursTextFiled.setText("");
    }

    @FXML
    void setZeroMinutes(MouseEvent event) {
        if (minutesTextFiled.getText().equals("00")) minutesTextFiled.setText("");
    }

    @FXML
    void setZeroSeconds(MouseEvent event) {
        if (secondsTextFiled.getText().equals("00")) secondsTextFiled.setText("");
    }


}



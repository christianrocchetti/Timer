package org.openjfx;


import com.jfoenix.controls.*;
import com.jfoenix.transitions.JFXFillTransition;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.util.StringConverter;
import net.harawata.appdirs.AppDirsFactory;

import org.json.JSONException;
import org.json.JSONObject;


import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.UnaryOperator;


public class Controller {

    enum State {
        INITIALIZED,
        STARTED,
        STOPPED
    }

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


    private Integer animationTime;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
    private State state = State.INITIALIZED;
    private HashMap<String, String> soundMap = new HashMap<>();
    private final String svgPlay =
            "M2.88235294,0.149128824 C1.29047347,0.149128824 0,1.43960353 0,3.03148176 L0,31.8550235" +
                    " C0,33.4469059 1.29047347,34.7373765 2.88235294,34.7373765 C4.47423241,34.7373765 27.4815351,20.00" +
                    "25824 28,19.5020824 C28.5430262,18.9779059 28.8235294,18.2576471 28.8235294,17.4432588 C28.8235294" +
                    ",16.6288706 28.5430262,15.9086076 28,15.3844312 C27.4815351,14.8839724 4.47423241,0.149128824 2.88" +
                    "235294,0.149128824 L2.88235294,0.149128824 Z";
    private final String svgStop = "M8,2 L8,18 C8,19.105 7.105,20 6,20 L2,20 C0.895,20 0,19.105 0,18 L0,2 C0,0.895 0.895," +
            "0 2,0 L6,0 C7.105,0 8,0.895 8,2 Z M18,0 L14,0 C12.895,0 12,0.895 12,2 L12,18 C12,19.105 12.895,2" +
            "0 14,20 L18,20 C19.105,20 20,19.105 20,18 L20,2 C20,0.895 19.105,0 18,0 Z";

    private void setTime() {
        int hours = animationTime / 3600;
        int minutes = (animationTime / 60) - (hours * 60);
        int seconds = animationTime - ((hours * 60) + (minutes * 60));

        hoursCounter.setText(Integer.toString(hours));
        minutesCounter.setText(Integer.toString(minutes));
        secondsCounter.setText(Integer.toString(seconds));
    }


    private void shakeAnimation(Node icon) {
        TranslateTransition step1 = new TranslateTransition(Duration.seconds(0.2), icon);
        step1.setToX(-5);
        step1.setCycleCount(1);
        TranslateTransition step2 = new TranslateTransition(Duration.seconds(0.2), icon);
        step2.setCycleCount(1);
        step2.setToX(5);
        TranslateTransition step3 = new TranslateTransition(Duration.seconds(0.2), icon);
        step3.setCycleCount(1);
        step3.setToX(0);


        SequentialTransition seqTran = new SequentialTransition(step1, step2, step3);
        seqTran.play();
    }

    private void updateTime() {
        animationTime -= 1;
        setTime();
    }


    public void saveSetting() {
        try {
            if (saveCheckBox.isSelected()) {
                String appPahtSettingSting = AppDirsFactory.getInstance().getUserDataDir(null, null, "appTimerJava");
                Path appPathSettingFile = Paths.get(appPahtSettingSting + "/setting.json");

                JSONObject settingJson = new JSONObject();

                String inputHours = hoursTextFiled.getText();
                String inputMinutes = minutesTextFiled.getText();
                String inputSeconds = secondsTextFiled.getText();
                // nel caso in cui l'untente ha inserito uno spazio bianco o non inserito nulla,
                // input viene sostituito con un 00
                inputHours = inputHours.replaceAll("\\s{0}^(?!.)|^\\s+|^0+", "00");
                inputMinutes = inputMinutes.replaceAll("\\s{0}^(?!.)|^\\s+|^0+", "00");
                inputSeconds = inputSeconds.replaceAll("\\s{0}^(?!.)|^\\s+|^0+", "00");

                settingJson.put("Hours", inputHours);
                settingJson.put("Minutes", inputMinutes);
                settingJson.put("Seconds", inputSeconds);
                settingJson.put("Message", messageField.getText());
                settingJson.put("Minimize", Boolean.toString(minimizeCheckBox.isSelected()));
                settingJson.put("Sound", soundComboBox.getValue());
                settingJson.put("Save", Boolean.toString(saveCheckBox.isSelected()));

                Files.write(appPathSettingFile, settingJson.toString().getBytes());
            }

        } catch (Exception e) {
            windowError(e);
        }


    }

    private void setDefaultSetting() {
        String appPahtSettingSting = AppDirsFactory.getInstance().getUserDataDir(null, null, "appTimerJava");
        Path appPathSettingFile = Paths.get(appPahtSettingSting + "/setting.json");
        JSONObject settingJson = new JSONObject();

        settingJson.put("Hours", "00");
        settingJson.put("Minutes", "00");
        settingJson.put("Seconds", "00");
        settingJson.put("Message", "Default Message");
        settingJson.put("Minimize", "true");
        settingJson.put("Save", "false");
        settingJson.put("Sound", "No Sound");

        try {
            Files.write(appPathSettingFile, settingJson.toString().getBytes());
        } catch (IOException e) {
            windowError(e);
           }
    }

    private void checkDirectorySetting() {
        String appPahtSettingSting = AppDirsFactory.getInstance().getUserDataDir(null, null, "appTimerJava");
        Path appPahtSettingDirctory = Paths.get(appPahtSettingSting);
        Path appPathSettingFile = Paths.get(appPahtSettingSting + "/setting.json");
        try {
            if (!Files.exists(appPahtSettingDirctory)) {
                Files.createDirectory(appPahtSettingDirctory);
            }
            if (!Files.exists(appPathSettingFile)) {
                Files.createFile(appPathSettingFile);
                setDefaultSetting();
            }
        }
        // TODO: morificare errore con errore "cartella non trovata"
        // TODO: morificare errore con errore "file non trovata"
        // TODO laciare l'eccezione al stack supeiore
        catch (Exception e) {
            windowError(e);
        }

    }

    private void setSettingFromFileGUI() {
        checkDirectorySetting();

        try {
            String appPahtSettingSting = AppDirsFactory.getInstance().getUserDataDir(null, null, "appTimerJava");
            Path appPathSettingFile = Paths.get(appPahtSettingSting + "/setting.json");
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
            String mexError = "STACK_TRACE_ERROR:" + "\n";
            for (StackTraceElement element : e.getStackTrace()) {
                mexError = mexError.concat(element.toString() + "\n");
            }
            System.out.println(mexError);
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            setDefaultSetting();
            setSettingFromFileGUI();
        }
    }

    private static void windowError(Exception error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText(error.getLocalizedMessage());
        alert.setContentText(error.getLocalizedMessage());

        String mexError = "STACK_TRACE_ERROR:" + "\n";
        for (StackTraceElement element : error.getStackTrace()) {
            mexError = mexError.concat(element.toString() + "\n");
        }

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(mexError);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.show();

        System.out.println(mexError);
        System.out.println(error.getMessage());
    }

    /**
     * Disabilita il pulasante start e avvia un timer di n minuti,
     * alla fine del timer ci sarò una finistra di dialogo con avviso
     */
    private void alertTimer(String inputHours, String inputMinutes, String inputSeconds) throws Exception {
        // Tolgo la visibilita' alle caselle di input(TextFieled) e do visibilita ai Counter(Text)
        hoursTextFiled.setVisible(false);
        minutesTextFiled.setVisible(false);
        secondsTextFiled.setVisible(false);

        hoursCounter.setVisible(true);
        minutesCounter.setVisible(true);
        secondsCounter.setVisible(true);

        if (minimizeCheckBox.isSelected()) App.minimizeWindow(true);

        setTime();
        timeline.setCycleCount(animationTime);
        timeline.play();

        // animazione in cui alla fine ci sarà l'avviso
        timeline.setOnFinished((e) -> {
            String track = soundComboBox.getValue();
            if (!track.equals("No Sound")){
                playAudio(track);
            }
            splashAnimation();

            // setto il vari Counter(Text) a con le impostazioni del timer precedente
            hoursTextFiled.setText(inputHours);
            minutesTextFiled.setText(inputMinutes);
            secondsTextFiled.setText(inputSeconds);

            // Tolgo la visibilita' alle ai Counter(Text) e do visibilita' agli input(TextFieled)
            hoursTextFiled.setVisible(true);
            minutesTextFiled.setVisible(true);
            secondsTextFiled.setVisible(true);

            hoursCounter.setVisible(false);
            minutesCounter.setVisible(false);
            secondsCounter.setVisible(false);

            playStop.setGraphic(iconSvg(svgPlay));
            state = State.INITIALIZED;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();

            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/dark-theme.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-button");

            alert.setTitle("Alert");

            alert.setHeaderText(messageField.getText());

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/timerIcon.png")));
            stage.setAlwaysOnTop(true);
            stage.show();

            App.minimizeWindow(false);
        });
    }

    private void playAudio(String trackAudio) {
        try {
            String pathTrackAudio = soundMap.get(trackAudio);
            Clip clipNameClip = AudioSystem.getClip();

            InputStream audioSrc = getClass().getResourceAsStream(pathTrackAudio);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(bufferedIn);

            clipNameClip.open(audioStream2);
            clipNameClip.start();

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            windowError(ex);
        }

    }

    private void splashAnimation() {
        JFXFillTransition transition = new JFXFillTransition();
        transition.setDuration(Duration.millis(1500));
        transition.setRegion(root);
        transition.setFromValue(Color.web("#B2B2B2"));
        transition.setToValue(Color.web("#2A2E37"));
        transition.play();
    }


    private Region iconSvg(String svg) {
        SVGPath svgPlay = new SVGPath();
        svgPlay.setContent(svg);
        final Region region = new Region();
        region.setShape(svgPlay);
        int size = 40;
        region.setMinSize(size, size);
        region.setPrefSize(size, size);
        region.setMaxSize(size, size);
        region.setStyle("-fx-background-color:  #FFFF8D;");
        return region;

    }

    @FXML
    public void inputButtonPlayStop(ActionEvent mouseEvent) throws Exception {
        // E' nello stato "STARTED" quindi nel momento in cui viene richimata la funzione
        // passa allo stato "STOPPED"
        switch (state) {
            // Se è nello stato di INITIALIZED il timer viene inizzializzato e le animazione avviate
            case INITIALIZED:
                String inputHours = hoursTextFiled.getText();
                String inputMinutes = minutesTextFiled.getText();
                String inputSeconds = secondsTextFiled.getText();
                // nel caso in cui l'untente ha inserito uno spazio bianco o non inserito nulla,
                // input viene sostituito con un 0
                inputHours = inputHours.equals("00") || inputHours.isEmpty() ? "0" : inputHours;
                inputMinutes = inputMinutes.equals("00") || inputMinutes.isEmpty() ? "0" : inputMinutes;
                inputSeconds = inputSeconds.equals("00") || inputSeconds.isEmpty() ? "0" : inputSeconds;


                animationTime = Integer.parseInt(inputHours) * 3600
                        + Integer.parseInt(inputMinutes) * 60
                        + Integer.parseInt(inputSeconds);
                // TODO: forse dovrei cambiare questo if
                // Il tempo è uguale a zero
                if (animationTime == 0) {
                    shakeAnimation(playStop);
                    return;
                }

                //----------------
                splashAnimation();
                //----------------

                // Ripristini i valori a "00" per una questione estetica per quando i TextField torneranno visibili
                inputHours = inputHours.equals("0") ? "00" : inputHours;
                inputMinutes = inputMinutes.equals("0") ? "00" : inputMinutes;
                inputSeconds = inputSeconds.equals("0") ? "00" : inputSeconds;
                alertTimer(inputHours, inputMinutes, inputSeconds);
                // Nel caso un era stato inserito un numero non intero  viene cancellato il messaggio di errore
                // textError.setText("");
                playStop.setGraphic(iconSvg(svgStop));
                state = State.STARTED;

                break;
            // Se è nello stato di STARTED l'animazioni vengono stoppate
            // e lo fare entrare un stato di STOPPED
            case STARTED:
                state = State.STOPPED;
                timeline.pause();
                playStop.setGraphic(iconSvg(svgPlay));
                break;
            // Se è nello stato di STOPPED l'animazioni vengono riavviate
            // e lo fare entrare un stato di STARTED
            case STOPPED:
                timeline.play();
                state = State.STARTED;
                playStop.setGraphic(iconSvg(svgStop));
                break;
        }

    }


    @FXML
    public void resetCounter(ActionEvent event) {
        if ((hoursTextFiled.getText().equals("00") || hoursTextFiled.getText().isEmpty())
                && (minutesTextFiled.getText().equals("00") || minutesTextFiled.getText().isEmpty())
                && (secondsTextFiled.getText().equals("00") || secondsTextFiled.getText().isEmpty())) {
            shakeAnimation(reset);
            return;
        }

        if (timeline.getStatus() == Animation.Status.RUNNING
                || timeline.getStatus() == Animation.Status.PAUSED) {
            timeline.stop();
        }

        // setto il vari Counter(Text) a 00
        hoursTextFiled.setText("00");
        minutesTextFiled.setText("00");
        secondsTextFiled.setText("00");

        // Tolgo la visibilita' alle caselle ai Counter(Text) e do visibilita' agli input(TextFieled)
        hoursTextFiled.setVisible(true);
        minutesTextFiled.setVisible(true);
        secondsTextFiled.setVisible(true);

        hoursCounter.setVisible(false);
        minutesCounter.setVisible(false);
        secondsCounter.setVisible(false);

        splashAnimation();
        playStop.setGraphic(iconSvg(svgPlay));

        state = State.INITIALIZED;
    }


    @FXML
    void initialize() {
        setSettingFromFileGUI();
        soundComboBox.getItems().addAll("No Sound", "Bird", "Vintage Alarm Clock", "Digital Alarm Clock", "Sweet Sound");

        reset.setGraphic(iconSvg("M22,12 L25.406,8.594 C23.031,5.656 19.602,4 16,4 C9.371,4 4,9.375 4,16 C4,22" +
                ".629 9.371,28 16,28 C19.602,28 22.82,26.406 25.02,23.895 L28.028,26.528 C25.094,29.875 20.801" +
                ",32 16,32 C7.164,32 0,24.836 0,16 C0,7.164 7.164,0 16,0 C20.801,0 25.332,2.387 28.266,5.734 L" +
                "32,2 L32,12 L22,12 Z"));
        playStop.setGraphic(iconSvg(svgPlay));

        soundMap.put("Bird", "/sounds/BIRD1-2.wav");
        soundMap.put("Vintage Alarm Clock", "/sounds/Old_Alarm_Clock.wav");
        soundMap.put("Digital Alarm Clock", "/sounds/digital_alarm_clock.wav");
        soundMap.put("Sweet Sound", "/sounds/sweet_sound.wav");


        soundComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null && !newval.equals("No Sound"))
                playAudio(newval);
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
                    windowError(e);
                }
                return s;
            }

            @Override
            public String toString(String d) {
                return d;
            }
        };


        // Il defoult vi
        TextFormatter<String> textFormatter = new TextFormatter<>(converter, secondsTextFiled.getText(), filter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(converter, minutesTextFiled.getText(), filter);
        TextFormatter<String> textFormatter3 = new TextFormatter<>(converter, hoursCounter.getText(), filter);

        secondsTextFiled.setTextFormatter(textFormatter);
        minutesTextFiled.setTextFormatter(textFormatter2);
        hoursTextFiled.setTextFormatter(textFormatter3);

        // minutesTextFiled.setText("00");
    }


    @FXML
    void messageEmpy(ActionEvent event) {
        if (messageField.getText().equals("Default Message")) messageField.setText("");
    }

    // Quando vingono cliccati i TextFiel vengiene azzerato il contenuto
    @FXML
    public void setZeroHours(MouseEvent event) throws Exception {
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



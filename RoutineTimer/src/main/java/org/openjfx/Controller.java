package org.openjfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Controller {

    // FXML
    public TextField textfieldTimeRoot;
    public Button buttontStartRoot;
    public Text textError;
    public Text textTime;

    private Integer animationTime;

    public void inputButtonStartRoot(ActionEvent mouseEvent) throws Exception {
        String inputText = textfieldTimeRoot.getText();
        try {
            animationTime = Integer.parseInt(inputText) * 60;
            alertTimerLegna();
            // nel caso un era stato inserito un numero non intero  viene cancellato il messaggio di errore
            textError.setText("");
        }
        // se non viene inserito un intero viene segnalato all'utente
        catch (NumberFormatException e) {
            textError.setText("Non hai inserito un intero!");
        }
    }

    private void setTime() {
        Integer minutes = animationTime / 60;
        Integer seconds = animationTime / (minutes + 1);
        textTime.setText("Minutes: " + minutes.toString() + " Seconds: " + seconds.toString());
    }

    private void updateTime() {
        animationTime -= 1;
        setTime();
    }

    /**
     * Disabilita il pulasante start e avvia un timer di n minuti,
     * alla fine del timer ci sarò una finistra di dialogo con avviso
     */
    private void alertTimerLegna() throws Exception {
        buttontStartRoot.setDisable(true);
        App.minimizeWindow(true);

        Timeline timeline;


        timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        setTime();
        timeline.setCycleCount(animationTime);
        timeline.play();

        // animazione in cui alla fine ci sarà l'avviso
        timeline.setOnFinished((e) -> {
            try {
                Clip clipNameClip = AudioSystem.getClip();
                InputStream audioSrc = getClass().getResourceAsStream("/sounds/BIRD1-2.wav");

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                clipNameClip.open(audioStream);
                clipNameClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                windowError(ex);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

            alert.setTitle("LEGNA");
            alert.setHeaderText("METTI LA LEGNA");
            alert.setContentText("Dopo aver messo la legna premi ok e riavvia il timer");

            stage.setAlwaysOnTop(true);
            stage.show();

            buttontStartRoot.setDisable(false);
            App.minimizeWindow(false);
        });
    }

    private static void windowError(Exception error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText(error.getLocalizedMessage());
        alert.setContentText(error.getLocalizedMessage());

        // Create expandable Exception.
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
    }

}

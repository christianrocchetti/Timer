package org.openjfx;

import javafx.animation.*;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Timer {

    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
    private Integer animationTime;
    private State state = State.INITIALIZED;

    Timer(){
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        state = State.INITIALIZED;
        animationTime = 0;
    }

    private void updateTime() {
        animationTime -= 1;
        setTime();
    }

    private void setTime() {
        int hours = animationTime / 3600;
        int minutes = (animationTime / 60) - (hours * 60);
        int seconds = animationTime - (((hours * 60) * 60) + (minutes * 60));

        App.getHomeController().hoursCounter.setText(Integer.toString(hours));
        App.getHomeController().minutesCounter.setText(Integer.toString(minutes));
        App.getHomeController().secondsCounter.setText(Integer.toString(seconds));
    }

    /**
     * Disabilita il pulasante start e avvia un timer di h(ore) + m(minuti) + s(secondi) ,
     * alla fine del timer ci sarò una finistra di dialogo con avviso
     *
     * @param inputHours   h - ore
     * @param inputMinutes m - minuti
     * @param inputSeconds s - secondi
     */
    private void timerStart(String inputHours, String inputMinutes, String inputSeconds) {
        // Tolgo la visibilita' alle caselle di input(TextFieled) e do visibilita ai Counter(Text)
        App.getHomeController().hoursTextFiled.setVisible(false);
        App.getHomeController().minutesTextFiled.setVisible(false);
        App.getHomeController().secondsTextFiled.setVisible(false);

        App.getHomeController().hoursCounter.setVisible(true);
        App.getHomeController().minutesCounter.setVisible(true);
        App.getHomeController().secondsCounter.setVisible(true);

        if (App.getHomeController().minimizeCheckBox.isSelected()) App.minimizeWindow(true);

        // animazione in cui alla fine ci sarà l'avviso
        timeline.setOnFinished((e) -> {
            String track = App.getHomeController().soundComboBox.getValue();
            if (!track.equals("No Sound")) {
                Utils.playAudio(App.getHomeController().getPathSoundMap(track));
            }
            MyAnimation.splashAnimation(App.getHomeController().root);

            // Setto il vari Counter(Text) a con le impostazioni del timer precedente
            App.getHomeController().hoursTextFiled.setText(inputHours);
            App.getHomeController().minutesTextFiled.setText(inputMinutes);
            App.getHomeController().secondsTextFiled.setText(inputSeconds);

            // Tolgo la visibilita' alle ai Counter(Text) e do visibilita' agli input(TextFieled)
            App.getHomeController().hoursTextFiled.setVisible(true);
            App.getHomeController().minutesTextFiled.setVisible(true);
            App.getHomeController().secondsTextFiled.setVisible(true);

            App.getHomeController().hoursCounter.setVisible(false);
            App.getHomeController().minutesCounter.setVisible(false);
            App.getHomeController().secondsCounter.setVisible(false);

            App.getHomeController().playStop.setGraphic(Utils.covertSvgToRegion(App.getHomeController().svgPlay, 40));
            state = State.INITIALIZED;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();

            dialogPane.getStylesheets().add(
                    App.class.getResource("/css/dark-theme.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-button");

            alert.setTitle("Alert");

            alert.setHeaderText(App.getHomeController().messageField.getText());

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/icon/timerIcon.png")));
            stage.setAlwaysOnTop(true);
            stage.show();

            App.minimizeWindow(false);
        });

        setTime();
        timeline.setCycleCount(animationTime);
        timerPlay();
    }

    public void timerStop() {
        state = State.STOPPED;
        timeline.pause();
        App.getHomeController().playStop.setGraphic(Utils.covertSvgToRegion(App.getHomeController().svgPlay, 40));
    }

    public void timerPlay() {
        timeline.play();
        state = State.STARTED;
        App.getHomeController().playStop.setGraphic(Utils.covertSvgToRegion(App.getHomeController().svgStop, 40));
    }

    /*
     * Funzione che inizzializza il timer e in caso posivo ivoaca splashAnimation e timeStart
     * in caso negativo ivoca shakekeAnimation e non ritorna niente
     * */
    public void timerInitialization() {
        String inputHours = App.getHomeController().hoursTextFiled.getText();
        String inputMinutes = App.getHomeController().minutesTextFiled.getText();
        String inputSeconds = App.getHomeController().secondsTextFiled.getText();
        // Nel caso in cui l'untente ha inserito uno spazio bianco o non inserito nulla,
        // input viene sostituito con un 0
        inputHours = inputHours.equals("00") || inputHours.isEmpty() ? "0" : inputHours;
        inputMinutes = inputMinutes.equals("00") || inputMinutes.isEmpty() ? "0" : inputMinutes;
        inputSeconds = inputSeconds.equals("00") || inputSeconds.isEmpty() ? "0" : inputSeconds;

        animationTime = Integer.parseInt(inputHours) * 3600
                + Integer.parseInt(inputMinutes) * 60
                + Integer.parseInt(inputSeconds);

        // Il tempo è uguale a zero
        if (animationTime == 0) {
            MyAnimation.shakeAnimation(App.getHomeController().playStop);
            return;
        }

        MyAnimation.splashAnimation(App.getHomeController().root);

        // Ripristini i valori a "00" per una questione estetica per quando i TextField torneranno visibili
        inputHours = inputHours.equals("0") ? "00" : inputHours;
        inputMinutes = inputMinutes.equals("0") ? "00" : inputMinutes;
        inputSeconds = inputSeconds.equals("0") ? "00" : inputSeconds;
        timerStart(inputHours, inputMinutes, inputSeconds);
    }

    public void timerReset() {
        // Nel caso in cui non è stato inserito un input valido la funzione si interrompe
        if ((App.getHomeController().hoursTextFiled.getText().equals("00")
                || App.getHomeController().hoursTextFiled.getText().isEmpty())
                && (App.getHomeController().minutesTextFiled.getText().equals("00")
                || App.getHomeController().minutesTextFiled.getText().isEmpty())
                && (App.getHomeController().secondsTextFiled.getText().equals("00")
                || App.getHomeController().secondsTextFiled.getText().isEmpty())) {
            MyAnimation.shakeAnimation(App.getHomeController().reset);
            return;
        }

        // Nel caso in cui  l'animazione è attiva (quindi il timer è attivo)
        // animazione viene eliminata
        if (timeline.getStatus() == Animation.Status.RUNNING
                || timeline.getStatus() == Animation.Status.PAUSED) {
            timeline.stop();
        }

        // Setto il vari Counter(Text) a 00
        App.getHomeController().hoursTextFiled.setText("00");
        App.getHomeController().minutesTextFiled.setText("00");
        App.getHomeController().secondsTextFiled.setText("00");

        // Tolgo la visibilita' alle caselle ai Counter(Text) e do visibilita' agli input(TextFieled)
        App.getHomeController().hoursTextFiled.setVisible(true);
        App.getHomeController().minutesTextFiled.setVisible(true);
        App.getHomeController().secondsTextFiled.setVisible(true);

        App.getHomeController().hoursCounter.setVisible(false);
        App.getHomeController().minutesCounter.setVisible(false);
        App.getHomeController().secondsCounter.setVisible(false);

        MyAnimation.splashAnimation(App.getHomeController().root);
        App.getHomeController().playStop
                .setGraphic(Utils.covertSvgToRegion(App.getHomeController().svgPlay, 40));

        state = State.INITIALIZED;
    }


    public State getState() {
        return state;
    }

}

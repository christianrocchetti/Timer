package org.openjfx;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Error {

    public static void errorWotSolution(Exception error, String myMessageError){
        windowError(error, myMessageError, "Not solution ");
    }

    public static void errorWithSolution(Exception error, String myMessageError, String soultion){
        windowError(error, myMessageError, "Solution: " + soultion);
    }

    private static void windowError(Exception error, String myMessageError, String solution) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String mexError = error.toString() + "\n";
        alert.setTitle("Error");
        alert.setHeaderText(myMessageError);
        alert.setContentText(solution);

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

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/icon/timerIcon.png")));
        stage.setAlwaysOnTop(true);
        stage.show();

        System.out.println("My message error: " + myMessageError);
        System.out.println("Soulution for user: " + solution);
        System.out.println(mexError);
    }

}

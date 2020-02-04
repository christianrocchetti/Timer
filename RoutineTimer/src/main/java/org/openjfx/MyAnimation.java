package org.openjfx;

import com.jfoenix.transitions.JFXFillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MyAnimation {

    public static void splashAnimation(Region pane) {
        JFXFillTransition transition = new JFXFillTransition();
        transition.setDuration(Duration.millis(1500));
        transition.setRegion(pane);
        transition.setFromValue(Color.web("#B2B2B2"));
        transition.setToValue(Color.web("#2A2E37"));
        transition.play();
    }

    public static void shakeAnimation(Node icon) {
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

}

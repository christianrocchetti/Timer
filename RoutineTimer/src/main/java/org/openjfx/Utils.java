package org.openjfx;


import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import org.json.JSONObject;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class Utils {

    public static Region covertSvgToRegion(String svg, int size) {
        SVGPath svgPlay = new SVGPath();
        svgPlay.setContent(svg);
        final Region region = new Region();
        region.setShape(svgPlay);
        region.setMinSize(size, size);
        region.setPrefSize(size, size);
        region.setMaxSize(size, size);
        region.setStyle("-fx-background-color:  #FFFF8D;");
        return region;
    }

    /**
     * Scrive il file di default
     * @param file percorso del file
     * @param settingJson JSON con il contenuto da salvare
     */
    public static void setSetting(Path file, JSONObject settingJson) {
        try {
            Files.write(file, settingJson.toString().getBytes());
        } catch (IOException e) {
            Error.errorWotSolution(e, "Fatal Error");
        }
    }

    /**
     * Verifica se la file di default esiste, in caso contrario crea il file di default e
     * scrive il file di default
     * @param file percorso del file
     * @param settingJson JSON con il contenuto da salvare
     */
    public static void checkFileSettings(Path file, JSONObject settingJson) {
        try {
            if (!Files.exists(file)) {
                Files.createFile(file);
                setSetting(file, settingJson);
            }
        }
        catch (AccessDeniedException e) {
            Error.errorWithSolution(e, "Impossible to save the settings",
                    "try to start the application how administrator/root user");
        } catch (NoSuchFileException e) {
            Error.errorWotSolution(e, "Impossible to save the settings. File "
                    + e.getLocalizedMessage() + " not found"
            );
        } catch (IOException e) {
            Error.errorWotSolution(e, "Fatal Error");
        }

    }

    /**
     * Verifica se la cartella di default esiste, in caso contrario crea il cartella di default
     * @param directory percorso della direcotory
     */
    public static void checkDirectorySetting(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectory(directory);
            }
        } catch (AccessDeniedException e) {
            Error.errorWithSolution(e, "Impossible to save the settings",
                    "try to start the application how administrator/root user");
        } catch (NoSuchFileException e) {
            Error.errorWotSolution(e, "Impossible to save the settings. Directory " + e.getLocalizedMessage()
                    + " not found");
        } catch (IOException e) {
            Error.errorWotSolution(e, "Fatal Error");
        }
    }

    /**
     * Avvia una traccia audio dalle risorse
     *
     * @param pathAudioTrack la traccia udio che dovrà essere avviata
     */
    public static void playAudio(String pathAudioTrack) {
        try {
            Clip clipNameClip = AudioSystem.getClip();

            InputStream audioSrc = App.class.getResourceAsStream(pathAudioTrack);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(bufferedIn);

            clipNameClip.open(audioStream2);
            clipNameClip.start();
        } catch (IOException e) {
            Error.errorWithSolution(e, "The audio track cannot be played",
                    "try to reinstall the program");
        } catch (LineUnavailableException | UnsupportedAudioFileException e) {
            Error.errorWotSolution(e, "Fatal Error");
        }
    }

}

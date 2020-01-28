module RoutineTimer {
    // Usiamo la direttiva "requires" richiede un certo package
    // per esempio può richiedere i package "javafx.controls" e "javafx.fxml"
    // se il file del package cerca di importare una package che qui
    // non è stato richiesto verra' laciato un errore
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires com.jfoenix;
    requires org.json;
    requires net.harawata.appdirs;

    // Usiamo la direttiva "opens" per conentire la reflection su un determinato package
    opens org.openjfx to javafx.fxml;

    // Usiamo la  direttiva "export" per esporre tutti i membri pubblici di un determinato package
    exports org.openjfx;
}
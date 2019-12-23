module RoutineTimer {
    // il modulo richiede le librerie "javafx.controls" e "javafx.fxml"
    // se il file del package cerca di importare una libreria che qui
    // non è stata richiesta verra' laciato un errore
    requires javafx.controls;
    requires javafx.fxml;

    // Le direttive opens, opens .. to ed open sono utili invece per
    // l’accessibilità dei package verso altri moduli. Utilizzando opens,
    // seguita dal nome di un package del modulo, stiamo dicendo che le
    // classi public (ed i loro campi e metodi public e protected)
    // all’interno del package sono accessibili in altri moduli soltanto
    // a runtime, consentendo l’accesso con reflection a tutti i tipi
    // all’interno delle classi del package.
    //
    // opens..to consente di specificare i moduli verso i quali
    // quanto detto precedentemente è possibile.
    opens org.openjfx to javafx.fxml;

    // In questo caso il modulo esporta il package it.html.spi contenente
    // l’interfaccia IProblemProvider e la sua classe Problem. Ciò abilita
    // moduli che implementano IProblemProvider ad accedere
    // all’implementazione fornita dal modulo
    exports org.openjfx;
}
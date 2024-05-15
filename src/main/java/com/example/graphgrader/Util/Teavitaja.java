package com.example.graphgrader.Util;

import javafx.scene.control.Alert;

public class Teavitaja {

    public static void teavita(String s, Alert.AlertType at) {
        Alert a = new Alert(at);
        a.setContentText(s);
        a.show();
    }

    public static Alert teeTeavitus(String s, Alert.AlertType at) {
        return new Alert(at, s);
    }
}

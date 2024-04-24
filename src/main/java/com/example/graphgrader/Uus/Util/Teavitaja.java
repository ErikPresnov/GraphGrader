package com.example.graphgrader.Uus.Util;

import javafx.scene.control.Alert;

public class Teavitaja {
    public static void teavita(String s, Alert.AlertType at) {
        Alert a = new Alert(at);
        a.setContentText(s);
        a.show();
    }
}

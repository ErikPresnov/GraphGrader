package com.example.graphgrader.Util;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;

public class Teavitaja {

    public static void teavita(String s, Alert.AlertType at) {
        Alert a = new Alert(at);
        a.setContentText(s);
        a.show();
    }

    public static void teavita(String s, String h) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setContentText(s);
        dialog.setTitle(h);
        dialog.getDialogPane()
              .getScene()
              .getWindow()
              .setOnCloseRequest(e -> {
            dialog.hide();
        });
        dialog.showAndWait();
    }

    public static Alert teeTeavitus(String s, Alert.AlertType at) {
        return new Alert(at, s);
    }
}

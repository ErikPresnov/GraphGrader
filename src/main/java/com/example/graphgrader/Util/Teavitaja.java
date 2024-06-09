package com.example.graphgrader.Util;

import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Teavitaja {

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
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                h.equals("Viga") ?
                        "err.jpg" :
                        "info.jpg"));
        dialog.showAndWait();
    }
}

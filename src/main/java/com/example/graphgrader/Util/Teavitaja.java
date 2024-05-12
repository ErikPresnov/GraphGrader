package com.example.graphgrader.Util;

import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class Teavitaja {
    public static Popup teavita1(String s, Alert.AlertType at) {
        Popup p = new Popup();
        p.getContent().add(new Text(s));
        return p;
        /*Alert a = new Alert(at);
        a.setContentText(s);
        a.show();*/
    }

    public static void teavita(String s, Alert.AlertType at) {
        Alert a = new Alert(at);
        a.setContentText(s);
        a.show();
    }

    public static Alert teeTeavitus(String s, Alert.AlertType at) {
        return new Alert(at, s);
    }
}

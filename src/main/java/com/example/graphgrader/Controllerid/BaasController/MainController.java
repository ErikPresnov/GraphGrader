package com.example.graphgrader.Controllerid.BaasController;

import com.example.graphgrader.Util.Teavitaja;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Tab Laiuti;
    public Tab Sygavuti1;
    public Tab Sygavuti2;
    public Tab Prim;
    public Tab Kruskal;
    public Tab Dijkstra;
    public Tab FW;
    public Tab Kahn;
    public Tab Eeldus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Laiuti.setOnSelectionChanged(e -> {
            if (Laiuti.isSelected()) Teavitaja.teavita("laiuti ", Alert.AlertType.INFORMATION);
        });
        Sygavuti1.setOnSelectionChanged(e -> {
            if (Sygavuti1.isSelected()) Teavitaja.teavita("syg ees", Alert.AlertType.INFORMATION);
        });
        Sygavuti2.setOnSelectionChanged(e -> {
            if (Sygavuti2.isSelected()) Teavitaja.teavita("syg lopp", Alert.AlertType.INFORMATION);
        });
        Prim.setOnSelectionChanged(e -> {
            if (Prim.isSelected()) Teavitaja.teavita("prim", Alert.AlertType.INFORMATION);
        });
        Kruskal.setOnSelectionChanged(e -> {
            if (Kruskal.isSelected()) Teavitaja.teavita("Kruskal", Alert.AlertType.INFORMATION);
        });
        Dijkstra.setOnSelectionChanged(e -> {
            if (Dijkstra.isSelected()) Teavitaja.teavita("Dijkstra", Alert.AlertType.INFORMATION);
        });
        FW.setOnSelectionChanged(e -> {
            if (FW.isSelected()) Teavitaja.teavita("FW", Alert.AlertType.INFORMATION);
        });
        Kahn.setOnSelectionChanged(e -> {
            if (Kahn.isSelected()) Teavitaja.teavita("Kahn", Alert.AlertType.INFORMATION);
        });
        Eeldus.setOnSelectionChanged(e -> {
            if (Eeldus.isSelected()) Teavitaja.teavita("Eeldus", Alert.AlertType.INFORMATION);
        });
    }
}

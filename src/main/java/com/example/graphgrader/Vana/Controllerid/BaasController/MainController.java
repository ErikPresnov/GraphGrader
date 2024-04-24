package com.example.graphgrader.Vana.Controllerid.BaasController;

import com.example.graphgrader.Vana.Util.Teavitaja;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Tab Laiuti;
    public String laiutitekst = "Graafi laiuti läbimine \nAndmestruktuur: magasin \nKasutamine: Punane tipp on praegune töödeldav, töötle kõik sellest väljuvad kaared mingis järjekorras, seejärel vajuta praegusele tipule, et see \"töödelduks\" märkida ning siis saab magasinist uue võtta";
    public Tab Sygavuti1;
    public String sygEesTekst = "Graafi sügavuti eesjärjestuses läbimine \nAndmestruktuur: magasin \nKasutamine: Punane tipp on praegu töödeldav, märgi tipp \"töödelduks\" vajutades sellele ning seejärel töötle kõik praegusest tipust väljuvad kaared mingis järjekorras.";
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
            if (Laiuti.isSelected()) Teavitaja.teavita(laiutitekst, Alert.AlertType.INFORMATION);
        });
        Sygavuti1.setOnSelectionChanged(e -> {
            if (Sygavuti1.isSelected()) Teavitaja.teavita(sygEesTekst, Alert.AlertType.INFORMATION);
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

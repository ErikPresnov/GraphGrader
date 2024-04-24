package com.example.graphgrader.Uus.Kontrollerid;

import com.example.graphgrader.Uus.Graaf.Graaf;
import com.example.graphgrader.Uus.Graaf.Kaar;
import com.example.graphgrader.Uus.Graaf.Tipp;
import com.example.graphgrader.Uus.Graaf.TippGraafil;
import com.example.graphgrader.Uus.Util.Teavitaja;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LaiutiKontroller {
    public List<Tipp> jarjekord = new ArrayList<>();
    public Graaf g;
    public String failitee = "Graafid\\test2.txt";
    public Button lae;
    public Label andmestruktuuriNimi;
    public Button andmestruktuur;

    public HBox pseudoStruktuur;
    public HBox pseudoToodeldud;

    public Tipp praegune;

    public Button lock;
    public boolean color = false;

    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, true);
        naitaGraafi();
        lae.setVisible(false);
    }

    public void naitaGraafi() {
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            TippGraafil tippEkraanil = new TippGraafil(40, 40, 30, praeguneTipp);
            lisaTipuKasitleja(tippEkraanil);

        }
    }

    public void lisaKaareKasitleja(Kaar kaar) {

    }

    public void lisaTipuKasitleja(TippGraafil tipp) {
        tipp.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            String tulemus = kontrolli(tipp);
            if (!tulemus.equals("")) Teavitaja.teavita(tulemus, Alert.AlertType.ERROR);
            else {
                // Muuda tipu seis
            }
        });
    }

    public void kuvaStruktuurid(List<Tipp> jarjekord, List<Tipp> toodeldud) {

    }

    public void lukustaGraaf(MouseEvent ignored) {

    }

    public void votaAndmestruktuurist(MouseEvent ignored) {

    }

    public void uuenda() {

    }

    public String kontrolli(TippGraafil t) {
        return null;
    }
}

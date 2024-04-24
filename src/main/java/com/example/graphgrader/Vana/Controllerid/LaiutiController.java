package com.example.graphgrader.Vana.Controllerid;

import com.example.graphgrader.Vana.Controllerid.BaasController.Controller;
import com.example.graphgrader.Uus.Graaf.Kaar;
import com.example.graphgrader.Uus.Graaf.Tipp;
import com.example.graphgrader.Vana.Util.TipuSobivus;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// Korras
public class LaiutiController extends Controller implements Initializable {

    public List<Tipp> jarjekord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jarjekord = new ArrayList<>();
        TippController.controller = this;
        TippController.andmestruktuuriNimi.setText("Järjekord");
        TippController.andmestruktuur.setDisable(true);
    }

    @Override
    public void lisa(Kaar k) {
        System.out.println("Lisan " + k.algus.tähis + " -> " + k.lopp.tähis);
        jarjekord.add(k.lopp);
        k.lopp.seis = TipuSeis.ANDMESTRUKTUURIS;
        TippController.kuvaAndmestruktuur(jarjekord);
        TippController.andmestruktuur.setDisable(false);
    }

    @Override
    public void vota() {
        if (jarjekord.isEmpty()) return;
        Tipp q = jarjekord.remove(0);
        if (q.seis == TipuSeis.TÖÖDELDUD) return;
        q.tippGraafil.setPraegune();
        q.seis = TipuSeis.PRAEGUNE;
        TippController.praegune = q;
        TippController.andmestruktuur.setDisable(true);
        TippController.kuvaAndmestruktuur(jarjekord);
    }

    @Override
    public TipuSobivus kontrolli(Tipp t) {
        if (t.seis == TipuSeis.TÖÖDELDUD) return new TipuSobivus(false, t);
        if (t.seis != TipuSeis.PRAEGUNE) return new TipuSobivus(false, null);
        for (Tipp tipp : t.alluvad) {
            if (tipp.seis != TipuSeis.ANDMESTRUKTUURIS && tipp.seis != TipuSeis.TÖÖDELDUD) return new TipuSobivus(false, tipp);
        }
        TippController.andmestruktuur.setDisable(false);
        return new TipuSobivus(true, null);
    }

    @Override
    public void joonistaTabel() {}

    @Override
    public void tee(Tipp lopp) {}
}

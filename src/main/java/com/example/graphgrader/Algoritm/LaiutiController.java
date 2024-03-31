package com.example.graphgrader.Algoritm;

import com.example.graphgrader.Algoritm.BaasController.Controller;
import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;
import com.example.graphgrader.Graaf.TipuSeis;
import com.example.graphgrader.Util.TipuSobivus;
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
        PäiseController.testController = this;
        PäiseController.andmestruktuuriNimi.setText("Magasin");
        PäiseController.andmestruktuur.setDisable(true);
    }

    @Override
    public void lisa(Tipp t) {
        jarjekord.add(t);
        t.seis = TipuSeis.ANDMESTRUKTUURIS;
        PäiseController.andmestruktuur.setDisable(false);
    }

    @Override
    public void lisa(Kaar k) {throw new RuntimeException();}

    @Override
    public void vota() {
        if (jarjekord.isEmpty()) return;
        Tipp q = jarjekord.remove(0);
        PäiseController.pseudoStruktuur.getChildren().remove(0);
        if (q.seis == TipuSeis.TÖÖDELDUD) return;
        q.tippGraafil.setPraegune();
        q.seis = TipuSeis.PRAEGUNE;
        PäiseController.praegune = q;
        PäiseController.andmestruktuur.setDisable(true);
    }

    @Override
    public TipuSobivus kontrolli(Tipp t) {
        if (t.seis == TipuSeis.TÖÖDELDUD) return new TipuSobivus(false, t);
        if (t.seis != TipuSeis.PRAEGUNE) return new TipuSobivus(false, null);
        for (Tipp tipp : t.alluvad) {
            if (tipp.seis != TipuSeis.ANDMESTRUKTUURIS && tipp.seis != TipuSeis.TÖÖDELDUD) return new TipuSobivus(false, tipp);
        }
        PäiseController.andmestruktuur.setDisable(false);
        return new TipuSobivus(true, null);
    }
}

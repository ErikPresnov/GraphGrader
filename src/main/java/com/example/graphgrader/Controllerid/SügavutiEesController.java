package com.example.graphgrader.Controllerid;

import com.example.graphgrader.Controllerid.BaasController.Controller;
import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;
import com.example.graphgrader.Graaf.TipuSeis;
import com.example.graphgrader.Util.TipuSobivus;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;

public class SügavutiEesController extends Controller implements Initializable {

    public Deque<Tipp> magasin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        magasin = new ArrayDeque<>();
        TippController.controller = this;
        TippController.andmestruktuuriNimi.setText("Magasin");
        TippController.andmestruktuur.setDisable(true);
    }

    @Override
    public void lisa(Kaar k) {
        magasin.add(k.lopp);
        k.lopp.muudaSeisu(TipuSeis.ANDMESTRUKTUURIS);
        TippController.andmestruktuur.setDisable(false);
    }

    @Override
    public void vota() {
        if (magasin.isEmpty()) return;
        Tipp q = magasin.removeLast();
        TippController.pseudoStruktuur.getChildren().remove(TippController.pseudoStruktuur.getChildren().size() - 1);
        if (q.seis == TipuSeis.TÖÖDELDUD) return;
        q.muudaSeisu(TipuSeis.PRAEGUNE);
        TippController.praegune = q;
        TippController.andmestruktuur.setDisable(true);
    }

    @Override
    public TipuSobivus kontrolli(Tipp t) {
        if (t.seis == TipuSeis.TÖÖDELDUD) return new TipuSobivus(false, t);
        if (t.seis != TipuSeis.PRAEGUNE) return new TipuSobivus(false, null);
        for (Tipp tipp : t.alluvad)
            if (tipp.seis != TipuSeis.TÖÖDELDUD && tipp.seis != TipuSeis.ANDMESTRUKTUURIS)
                return new TipuSobivus(false, tipp);

        TippController.andmestruktuur.setDisable(false);
        return new TipuSobivus(true, null);
    }

    @Override
    public void joonistaTabel() {}

    @Override
    public void tee(Tipp lopp) {}
}

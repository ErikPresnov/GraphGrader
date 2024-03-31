package com.example.graphgrader.Algoritm;

import com.example.graphgrader.Algoritm.BaasController.Controller;
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
        PäiseController.testController = this;
        PäiseController.andmestruktuuriNimi.setText("Magasin");
        PäiseController.andmestruktuur.setDisable(true);
    }

    @Override
    public void lisa(Tipp t) {
        magasin.add(t);
        t.muudaSeisu(TipuSeis.ANDMESTRUKTUURIS);
        PäiseController.andmestruktuur.setDisable(false);
    }

    @Override
    public void lisa(Kaar k) {throw new RuntimeException();}

    @Override
    public void vota() {
        if (magasin.isEmpty()) return;
        Tipp q = magasin.removeLast();
        PäiseController.pseudoStruktuur.getChildren().remove(PäiseController.pseudoStruktuur.getChildren().size() - 1);
        if (q.seis == TipuSeis.TÖÖDELDUD) return;
        q.muudaSeisu(TipuSeis.PRAEGUNE);
        PäiseController.praegune = q;
        PäiseController.andmestruktuur.setDisable(true);
    }

    @Override
    public TipuSobivus kontrolli(Tipp t) {
        if (t.seis == TipuSeis.TÖÖDELDUD) return new TipuSobivus(false, t);
        if (t.seis != TipuSeis.PRAEGUNE) return new TipuSobivus(false, null);
        for (Tipp tipp : t.alluvad)
            if (tipp.seis != TipuSeis.TÖÖDELDUD && tipp.seis != TipuSeis.ANDMESTRUKTUURIS) return new TipuSobivus(false, tipp);

        PäiseController.andmestruktuur.setDisable(false);
        return new TipuSobivus(true, null);
    }
}

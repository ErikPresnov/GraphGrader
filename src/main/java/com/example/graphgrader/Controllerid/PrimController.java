package com.example.graphgrader.Controllerid;

import com.example.graphgrader.Controllerid.BaasController.Controller;
import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;
import com.example.graphgrader.Graaf.TipuSeis;
import com.example.graphgrader.Util.Kuhjad.MinHeapKaared;
import com.example.graphgrader.Util.TipuSobivus;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class PrimController extends Controller implements Initializable {

    public MinHeapKaared kuhi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kuhi = new MinHeapKaared();
        KaarController.controller = this;
        KaarController.andmestruktuuriNimi.setText("Eelistus järjekord");
        KaarController.andmestruktuur.setDisable(true);
        KaarController.suunatud = true;
    }

    @Override
    public void lisa(Kaar k) {
        kuhi.lisa(k);
        k.arrow.setFill(Color.ORANGE);
        k.lopp.muudaSeisu(TipuSeis.ANDMESTRUKTUURIS);
        KaarController.uuendaAndmed(kuhi);
    }

    @Override
    public void vota() {
        Kaar k = kuhi.min();
        k.lopp.muudaSeisu(TipuSeis.PRAEGUNE);
        KaarController.praegune = k.lopp;
        KaarController.uuendaAndmed(kuhi);
        k.arrow.setFill(Color.GREEN);
    }

    @Override
    public TipuSobivus kontrolli(Tipp t) {
        if (t.seis == TipuSeis.TÖÖDELDUD) return new TipuSobivus(false, t);
        if (t.seis != TipuSeis.PRAEGUNE) return new TipuSobivus(false, null);
        for (Tipp tipp : t.alluvad) {
            //Kaar k = null;
            //for (Kaar kaar : t.kaared) if (kaar.lopp == tipp) k = kaar;
            if (tipp.seis != TipuSeis.ANDMESTRUKTUURIS && tipp.seis != TipuSeis.TÖÖDELDUD)
                return new TipuSobivus(false, tipp);
        }
        KaarController.andmestruktuur.setDisable(false);
        return new TipuSobivus(true, null);
    }

    @Override
    public void joonistaTabel() {}

    @Override
    public void tee(Tipp lopp) {}
}

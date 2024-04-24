package com.example.graphgrader.Vana.Controllerid;

import com.example.graphgrader.Vana.Controllerid.BaasController.Controller;
import com.example.graphgrader.Uus.Graaf.Kaar;
import com.example.graphgrader.Uus.Graaf.Tipp;
import com.example.graphgrader.Vana.Graaf.TipuSeis;
import com.example.graphgrader.Vana.Util.Kuhjad.MinHeapTipud;
import com.example.graphgrader.Vana.Util.TipuSobivus;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DijkstraController extends Controller implements Initializable {

    public MinHeapTipud kuhi;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kuhi = new MinHeapTipud();
        TippController.controller = this;
        TippController.andmestruktuuriNimi.setText("Eelistus järjekord");
        TippController.andmestruktuur.setDisable(true);
    }

    @Override
    public void lisa(Kaar k) {
        Tipp t = k.lopp;
        if (kuhi.olemas(t)) {
            if (t.kaal > k.algus.kaal + k.kaal) {
                t.kaal = k.algus.kaal + k.kaal;
                kuhi.yles(kuhi.heap.indexOf(t));
            }
        } else {
            t.kaal = k.algus.kaal + k.kaal;
            kuhi.lisa(t);
        }
        t.muudaSeisu(TipuSeis.ANDMESTRUKTUURIS);
        TippController.andmestruktuur.setDisable(true);
        TippController.pseudoStruktuur.getChildren().add(new Text(t.tähis + ":" + t.kaal + "\t"));
    }

    @Override
    public void vota() {
        if (kuhi.tühi()) return;
        Tipp t = kuhi.min();
        TippController.pseudoStruktuur.getChildren().remove(TippController.pseudoStruktuur.getChildren().size() - 1);
        if (t.seis == TipuSeis.TÖÖDELDUD) return;
        t.muudaSeisu(TipuSeis.PRAEGUNE);
        TippController.praegune = t;
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

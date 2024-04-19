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

public class KruskalController extends Controller implements Initializable {

    public MinHeapKaared kuhi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kuhi = new MinHeapKaared();
        KaarController.controller = this;
        KaarController.andmestruktuuriNimi.setText("Eelistus järjekord");
        KaarController.andmestruktuur.setDisable(true);
        KaarController.suunatud = true;
        KaarController.kruskal = true;
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

    }

    @Override
    public TipuSobivus kontrolli(Tipp t) {
        return null;
    }

    @Override
    public void joonistaTabel() {throw new RuntimeException();}

    @Override
    public void tee(Tipp lopp) {

    }
}

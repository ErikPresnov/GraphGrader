package com.example.graphgrader.Vana.Controllerid;

import com.example.graphgrader.Vana.Controllerid.BaasController.Controller;
import com.example.graphgrader.Uus.Graaf.Kaar;
import com.example.graphgrader.Uus.Graaf.Tipp;
import com.example.graphgrader.Vana.Graaf.TipuSeis;
import com.example.graphgrader.Vana.Util.TipuSobivus;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class KahnController extends Controller implements Initializable {

    public GridPane tabel;
    List<Tipp> jarjekord;
    int[] sisendastmedKorrektne;
    int[] sisendastmedOlemas;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jarjekord = new ArrayList<>();
        TippController.controller = this;
        TippController.andmestruktuuriNimi.setText("Järjekord");
        TippController.andmestruktuur.setDisable(true);
        TippController.color = true;
        TippController.kahn = true;
    }

    @Override
    public void joonistaTabel() {
        sisendastmedOlemas = new int[g.tipud.size()];
        Button btn = new Button("Kontrolli");
        btn.setOnMouseClicked(mouseEvent -> kontrolliMassiive());
        tabel.add(btn, 0, 0, 2, 1);
        tabel.setHgap(5);
        tabel.setVgap(5);
        tabel.add(new Text("Tähis"), 0, 1);
        tabel.add(new Text("Sisendaste"), 1, 1);
        for (int i = 0; i < g.tipud.size(); i++) {
            tabel.add(new Text(g.tipud.get(i).tähis), 0, i + 2);
            Text luger = new Text("0");
            tabel.add(luger, 1, i + 2);
            Button alla = new Button("-");
            final int finalI = i;
            alla.setOnMouseClicked(mouseEvent -> {
                sisendastmedOlemas[finalI]--;
                luger.setText(String.valueOf(sisendastmedOlemas[finalI]));
            });
            Button yles = new Button("+");
            yles.setOnMouseClicked(mouseEvent -> {
                sisendastmedOlemas[finalI]++;
                luger.setText(String.valueOf(sisendastmedOlemas[finalI]));
            });
            tabel.add(alla, 2, i + 2);
            tabel.add(yles, 3, i + 2);
        }
        taastaAlgus();
    }

    @Override
    public void tee(Tipp lopp) {
        sisendastmedKorrektne[Integer.parseInt(lopp.tähis) - 1]--;
    }

    public void kontrolliMassiive() {
        for (int i = 0; i < sisendastmedKorrektne.length; i++)
            if (sisendastmedKorrektne[i] != sisendastmedOlemas[i]) throw new RuntimeException();
        for (Tipp tipp : g.tipud) {
            if (sisendastmedOlemas[Integer.parseInt(tipp.tähis) - 1] == 0 && tipp.seis != TipuSeis.TÖÖDELDUD && tipp.seis != TipuSeis.ANDMESTRUKTUURIS) {
                jarjekord.add(tipp);
                tipp.muudaSeisu(TipuSeis.ANDMESTRUKTUURIS);
                TippController.pseudoStruktuur.getChildren().add(new Text(tipp.tähis + "\t"));
                TippController.andmestruktuur.setDisable(false);
            }
        }
    }

    public void taastaAlgus() {
        sisendastmedKorrektne = new int[g.tipud.size()];
        for (Tipp t : g.tipud) {
            TippController.praegune = null;
            t.seis = TipuSeis.AVASTAMATA;
            t.tippGraafil.setFill(Color.WHITE);
            for (Kaar kaar : t.kaared) sisendastmedKorrektne[Integer.parseInt(kaar.lopp.tähis) - 1]++;
        }
    }

    @Override
    public void lisa(Kaar k) {
        TippController.pseudoStruktuur.getChildren().remove(TippController.pseudoStruktuur.getChildren().size() - 1);
        k.lopp.muudaSeisu(TipuSeis.AVASTAMATA);
    }

    @Override
    public void vota() {
        if (jarjekord.isEmpty()) return;
        Tipp q = jarjekord.remove(0);
        TippController.pseudoStruktuur.getChildren().remove(0);
        q.muudaSeisu(TipuSeis.PRAEGUNE);
        TippController.praegune = q;
        TippController.andmestruktuur.setDisable(true);
    }

    @Override
    public TipuSobivus kontrolli(Tipp t) {
        t.muudaSeisu(TipuSeis.TÖÖDELDUD);
        try {
            kontrolliMassiive();
        } catch (RuntimeException e) {
            t.muudaSeisu(TipuSeis.PRAEGUNE);
            return new TipuSobivus(false, t);
        }
        return new TipuSobivus(true, null);
    }
}

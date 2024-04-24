package com.example.graphgrader.Vana.Controllerid.BaasController;

import com.example.graphgrader.Uus.Graaf.Kaar;
import com.example.graphgrader.Uus.Graaf.Tipp;
import com.example.graphgrader.Uus.Graaf.TippGraafil;
import com.example.graphgrader.Vana.Graaf.*;
import com.example.graphgrader.Vana.Util.TipuSobivus;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TippController {
    public Controller controller;

    public Label andmestruktuuriNimi;
    public Button andmestruktuur;

    public HBox pseudoStruktuur;
    public HBox pseudoToodeldud;

    public Tipp praegune;

    public Button lock;
    public Button lae;
    public boolean color = false;
    public boolean kahn = false;

    public String failitee = "Graafid\\test2.txt";

    public void laeGraaf(MouseEvent ignored) throws IOException {
        controller.g = new Graaf(failitee, true);
        showGraph();
        lae.setDisable(true);
        controller.joonistaTabel();
    }

    public void reload(Graaf g) {
        controller.graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        controller.graafiElement.getChildren().removeIf(e -> e instanceof Text);
        List<Arrow> nooled = new ArrayList<>();
        List<Text> kaalud = new ArrayList<>();
        for (Tipp t: g.tipud) {
            for (Kaar kaar : t.kaared) {
                Tipp algus = kaar.algus;
                Tipp lopp = kaar.lopp;
                Arrow arrow = new Arrow(
                        algus.tippGraafil.getCenterX(), algus.tippGraafil.getCenterY(),
                        lopp.tippGraafil.getCenterX(), lopp.tippGraafil.getCenterY(),
                        true, false
                );
                if (g.kaalutud)
                    kaalud.add(new Text(arrow.midX, arrow.midY, String.valueOf(kaar.kaal)));

                nooled.add(addArrowHander(arrow, algus, lopp, kaar));
            }
        }
        controller.graafiElement.getChildren().addAll(nooled);
        controller.graafiElement.getChildren().addAll(kaalud);
    }

    public Arrow addArrowHander(Arrow arrow, Tipp algus, Tipp lopp, Kaar kaar) {
        arrow.setOnMouseClicked(e -> {
            if (algus == praegune && lopp.seis != TipuSeis.TÖÖDELDUD && lopp.seis != TipuSeis.PRAEGUNE) {
                controller.lisa(kaar);
                if (color) arrow.setFill(Color.YELLOW);
                if (kahn) controller.tee(lopp);
            }
        });
        return arrow;
    }

    public void showGraph() {
        for (int i = 0; i < controller.g.tipud.size(); i++) {
            Tipp tipp = controller.g.tipud.get(i);
            TippGraafil tippGraafil = new TippGraafil(40,40, 30, tipp);
            tippGraafil = addTippGraafilHander(tippGraafil, tipp);
            tipp.tippGraafil = tippGraafil;
            if (i == 0) {
                praegune = tipp;
                tipp.muudaSeisu(TipuSeis.PRAEGUNE);
            } else
                tippGraafil.setFill(Color.WHITE);
            Group grupp = MakeGroup(tippGraafil, new Text(tipp.tähis + ""));
            controller.graafiElement.getChildren().add(grupp);
        }

        reload(controller.g);
    }



    public Group MakeGroup(TippGraafil tipp, Text text) {
        tipp.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getX() < controller.graafiElement.getLayoutX() + 35 ||
                e.getX() > controller.graafiElement.getLayoutX() + controller.graafiElement.getWidth() - 35) return;
            if (e.getY() < 35 || e.getY() > controller.graafiElement.getHeight() - 35) return;
            tipp.setCenterX(e.getX());
            tipp.setCenterY(e.getY());
            text.setX(e.getX() - 3);
            text.setY(e.getY() + 3);
            reload(controller.g);
        });
        return new Group(tipp, text);
    }
}

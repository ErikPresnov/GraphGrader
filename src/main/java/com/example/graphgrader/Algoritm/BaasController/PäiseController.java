package com.example.graphgrader.Algoritm.BaasController;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.TipuSobivus;
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

public class PäiseController {
    public Controller testController;

    public Label andmestruktuuriNimi;
    public Button andmestruktuur;

    public HBox pseudoStruktuur;
    public HBox pseudoToodeldud;

    public Tipp praegune;

    public Button lock;
    public Button lae;

    public void laeGraaf(MouseEvent ignored) throws IOException {
        try {
            testController.g = new Graaf("Graafid\\test1.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showGraph();
        lae.setDisable(true);
    }

    public void reload(Graaf g) {
        testController.graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        testController.graafiElement.getChildren().removeIf(e -> e instanceof Text);
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
                if (g.kaalutud) {
                    double midX = arrow.midX;
                    double midY = arrow.midY;
                    Text kaaluText = new Text(String.valueOf(kaar.kaal));
                    kaaluText.setX(midX);
                    kaaluText.setY(midY);
                    kaalud.add(kaaluText);
                }

                Text text = new Text(lopp.tähis + "\t");
                arrow = addArrowHander(arrow, text, algus, lopp);
                nooled.add(arrow);
            }
        }
        testController.graafiElement.getChildren().addAll(nooled);
        testController.graafiElement.getChildren().addAll(kaalud);
    }

    public Arrow addArrowHander(Arrow arrow, Text text, Tipp algus, Tipp lopp) {
        arrow.setOnMouseClicked(e -> {
            if (algus == praegune && lopp.seis != TipuSeis.TÖÖDELDUD && lopp.seis != TipuSeis.PRAEGUNE) {
                lopp.muudaSeisu(TipuSeis.ANDMESTRUKTUURIS);
                pseudoStruktuur.getChildren().add(text);
                testController.lisa(lopp);
            }
        });
        return arrow;
    }

    public void showGraph() throws IOException {
        for (int i = 0; i < testController.g.tipud.size(); i++) {
            Tipp tipp = testController.g.tipud.get(i);
            TippGraafil tippGraafil = new TippGraafil(40,40, 30, tipp);
            tippGraafil = addTippGraafilHander(tippGraafil, tipp);
            tipp.tippGraafil = tippGraafil;
            if (i == 0) {
                praegune = tipp;
                tippGraafil.setPraegune();
                tipp.seis = TipuSeis.PRAEGUNE;
            } else
                tippGraafil.setFill(Color.WHITE);
            Text text1 = new Text(tipp.tähis + "");
            Group grupp1 = MakeGroup(tippGraafil, text1);
            testController.graafiElement.getChildren().add(grupp1);
        }

        reload(testController.g);
    }


    public TippGraafil addTippGraafilHander(TippGraafil t, Tipp tipp) {
        t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            TipuSobivus tagastus = testController.kontrolli(tipp);
            if (!tagastus.korras()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                Tipp viga = tagastus.viga();
                if (viga == null)
                    alert.setContentText("Tipp {%s} ei ole praegu töödeldav.".formatted(tipp.tähis));
                else if (viga == tipp)
                    alert.setContentText("Tipp {%s} on juba töödeldud.".formatted(viga.tähis));
                else
                    alert.setContentText("Tipust {%s} on järglane {%s} vaatlemata.".formatted(tipp.tähis, viga.tähis));

                alert.show();
                return;
            }
            t.setToodeldud();
            tipp.seis = TipuSeis.TÖÖDELDUD;
            pseudoToodeldud.getChildren().add(new Text(tipp.tähis + "\t"));
        });
        return t;
    }

    public Group MakeGroup(TippGraafil tipp, Text text) {
        tipp.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getX() < testController.graafiElement.getLayoutX() + 35 ||
                e.getX() > testController.graafiElement.getLayoutX() + testController.graafiElement.getWidth() - 35) return;
            if (e.getY() < 35 || e.getY() > testController.graafiElement.getHeight() - 35) return;
            tipp.setCenterX(e.getX());
            tipp.setCenterY(e.getY());
            text.setX(e.getX() - 3);
            text.setY(e.getY() + 3);
            reload(testController.g);
        });
        return new Group(tipp, text);
    }

    public void VotaAndmestruktuurist(MouseEvent ignored) {testController.vota();}

    public void LockGraph(MouseEvent ignored) {
        Graaf g = testController.g;
        if (g == null) return;
        for (Tipp tipp : g.tipud) 
            tipp.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
        
        lock.setDisable(true);
    }
}

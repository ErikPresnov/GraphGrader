package com.example.graphgrader;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Hindaja.Algoritm;
import com.example.graphgrader.Hindaja.Hindaja;
import com.example.graphgrader.Hindaja.Tegevus;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class LaiutiLäbimine {

    public Pane graafiElement;
    public HBox jarjekord;
    public HBox toodeldud;
    private final double tipuSuurus = 30;
    public Label tulemusVal = new Label("0");
    private List<Tegevus> tegevused = new ArrayList<>();
    private Graaf graaf;
    private HashMap<TippGraafil, Group> map = new HashMap<>();
    private boolean graafOlemas = false;

    public void showGraph(MouseEvent mouseEvent) throws IOException {
        taastaAlgus();
        String failitee = "test1.txt";
        this.graaf = new Graaf(failitee);
        this.graafOlemas = true;

        for (int i = 0; i < graaf.tipud.size(); i++) {
            Tipp tipp = graaf.tipud.get(i);
            TippGraafil tippGraafil = new TippGraafil(40,40, tipuSuurus, tipp);
            tippGraafil = addTippGraafilHander(tippGraafil, tipp);
            tipp.tippGraafil = tippGraafil;
            if (i == 0)
                tippGraafil.setPraegune();
            else
                tippGraafil.setFill(Color.WHITE);
            Text text1 = new Text(tipp.tähis + "");
            Group grupp1 = makeGroup(tippGraafil, text1);
            map.put(tippGraafil, grupp1);
            graafiElement.getChildren().add(grupp1);
        }

        reload(graaf);
    }

    public void taastaAlgus() {
        graafiElement.getChildren().remove(0, graafiElement.getChildren().size());
        tegevused.clear();
        toodeldud.getChildren().remove(0, toodeldud.getChildren().size());
        jarjekord.getChildren().remove(0, jarjekord.getChildren().size());
        tulemusVal.setText("0%");
    }

    public void CheckSolution(MouseEvent event) {
        Hindaja hindaja = new Hindaja(Algoritm.LAIUTI_LÄBIMINE, tegevused, graaf);
        double tulemus = hindaja.hinda();
        this.tulemusVal.setText(tulemus * 100 + "%");
    }

    public void reload(Graaf g) {
        graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        graafiElement.getChildren().removeIf(e -> e instanceof Text);
        List<Arrow> nooled = new ArrayList<>();
        List<Text> kaalud = new ArrayList<>();
        for (Tipp t: g.tipud) {
            for (Kaar kaar : t.kaared) {
                Tipp algus = kaar.algus;
                Tipp lopp = kaar.lopp;
                Arrow arrow = new Arrow(
                        algus.tippGraafil.getCenterX(), algus.tippGraafil.getCenterY(),
                        lopp.tippGraafil.getCenterX(), lopp.tippGraafil.getCenterY(),
                        true
                );
                double midX = arrow.midX;
                double midY = arrow.midY;
                Text kaaluText = new Text(String.valueOf(kaar.kaal));
                kaaluText.setX(midX);
                kaaluText.setY(midY);
                kaalud.add(kaaluText);

                Text text = new Text("  { " + algus.tähis + " -> " + lopp.tähis + " }  ");
                text = addTextHander(text, g, lopp);
                arrow = addArrowHander(arrow, text, lopp);
                nooled.add(arrow);
            }
        }
        graafiElement.getChildren().addAll(nooled);
        graafiElement.getChildren().addAll(kaalud);
    }

    public void removeStep() {
        Tegevus viimane = tegevused.remove(tegevused.size() - 1);
        switch (viimane.v) {
            case LISA_JARJEKORDA -> {
                viimane.t.tippGraafil.setTagasi();
                //tipud.get(viimane.t).setTagasi();
                jarjekord.getChildren().remove(jarjekord.getChildren().size() - 1);
            }

            case EEMALDA_JARJEKORRAST -> {
                Text text = new Text("  { " + "x" + " -> " + viimane.t.tähis + " }  ");
                text.setOnMouseClicked(e -> {
                    Tipp otsitav = null;
                    String s = text.getText().replace("{", "").replace("}", "").replace(" ", "");
                    for (Tipp tipp : graaf.tipud) {
                        if (String.valueOf(tipp.tähis).equals(s.split("->")[1])) {
                            otsitav = tipp;
                            break;
                        }
                    }

                    //TippGraafil t = tipud.get(otsitav);
                    otsitav.tippGraafil.setTagasi();
                    jarjekord.getChildren().remove(e.getSource());
                });
                jarjekord.getChildren().add(0, text);
            }
            case TOODELDUD -> {
                viimane.t.tippGraafil.setTagasi();
                //tipud.get(viimane.t).setTagasi();
                toodeldud.getChildren().remove(toodeldud.getChildren().size() - 1);
            }
        }
    }

    public Text addTextHander(Text text, Graaf g, Tipp lopp) {
        text.setOnMouseClicked(e -> {
            Tipp otsitav = null;
            String s = text.getText().replace("{", "").replace("}", "").replace(" ", "");
            for (Tipp tipp : g.tipud) {
                if (String.valueOf(tipp.tähis).equals(s.split("->")[1])) {
                    otsitav = tipp;
                    break;
                }
            }
            //TippGraafil t = tipud.get(otsitav);
            TippGraafil t = otsitav.tippGraafil;
            if (t.getFill() != Color.GREEN)
                t.setPraegune();
            jarjekord.getChildren().remove(e.getSource());
            tegevused.add(new Tegevus(Tegevus.Voimalus.EEMALDA_JARJEKORRAST, lopp));
        });
        return text;
    }

    public Arrow addArrowHander(Arrow arrow, Text text, Tipp lopp) {
        arrow.setOnMouseClicked(e -> {
            tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_JARJEKORDA, lopp));
            //tipud.get(lopp).setJarjekorras();
            lopp.tippGraafil.setJarjekorras();
            jarjekord.getChildren().add(text);
        });
        return arrow;
    }

    public TippGraafil addTippGraafilHander(TippGraafil t, Tipp tipp) {
        t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            t.setToodeldud();
            tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, tipp));
            toodeldud.getChildren().add(new Text(tipp.tähis + " "));
        });
        return t;
    }

    public Group makeGroup(TippGraafil tipp, Text text) {
        tipp.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getX() < graafiElement.getLayoutX() + 25 ||
                e.getX() > graafiElement.getLayoutX() + graafiElement.getWidth() - 35) return;
            if (e.getY() < 30 || e.getY() > graafiElement.getHeight() - 30) return;
            tipp.setCenterX(e.getX());
            tipp.setCenterY(e.getY());
            text.setX(e.getX() - 3);
            text.setY(e.getY() + 3);
            reload(graaf);
        });
        return new Group(tipp, text);
    }

    public void lock(MouseEvent event) {
        if (!graafOlemas) return;
        for (Tipp tipp : graaf.tipud) {
            tipp.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
        }
    }
}

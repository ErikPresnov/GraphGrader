package com.example.graphgrader;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Hindaja.Algoritm;
import com.example.graphgrader.Hindaja.Hindaja;
import com.example.graphgrader.Hindaja.Tegevus;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class LaiutiLäbimine {

    public Pane graaf;
    public HBox jarjekord;
    public HBox toodeldud;
    private final double tipuSuurus = 30;
    public Label tulemusVal = new Label("0");
    public Pane kollaneV;
    public Pane rohelineV;
    public Pane punaneV;
    public Pane valgeV;
    private List<Tegevus> tegevused = new ArrayList<>();
    private Map<Tipp, TippGraafil> tipud = new HashMap<>();
    private Graaf graaf1;

    public void showGraph(MouseEvent mouseEvent) throws IOException {
        taastaAlgus();

        String failitee = "test1.txt";
        this.graaf1 = new Graaf(failitee);

        for (int i = 0; i < graaf1.tipud.size(); i++) {
            Tipp tipp = graaf1.tipud.get(i);
            TippGraafil tippGraafil = new TippGraafil(40,40, tipuSuurus, tipp);
            tipud.put(tipp, tippGraafil);
            tippGraafil = addTippGraafilHander(tippGraafil, tipp);
            tipp.tippGraafil = tippGraafil;
            if (i == 0)
                tippGraafil.setFill(Color.RED);
            else
                tippGraafil.setFill(Color.WHITE);
            Text text1 = new Text(tipp.tähis + "");
            Group grupp1 = makeGroup(tippGraafil, text1);
            graaf.getChildren().add(grupp1);
        }

        reload(graaf1);
    }

    public void taastaAlgus() {
        graaf.getChildren().remove(0, graaf.getChildren().size());
        tegevused.clear();
        toodeldud.getChildren().remove(0, toodeldud.getChildren().size());
        jarjekord.getChildren().remove(0, jarjekord.getChildren().size());
        tulemusVal.setText("0");
        graaf.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));

        Rectangle kollane = new Rectangle(30, 30, Color.YELLOW);
        kollaneV.getChildren().add(kollane);

        Rectangle roheline = new Rectangle(30, 30, Color.GREEN);
        rohelineV.getChildren().add(roheline);

        Rectangle punane = new Rectangle(30, 30, Color.RED);
        punaneV.getChildren().add(punane);

        Rectangle valge = new Rectangle(30, 30, Color.WHITE);
        valgeV.getChildren().add(valge);
    }

    public void CheckSolution(MouseEvent event) {
        Hindaja hindaja = new Hindaja(Algoritm.LAIUTI_LÄBIMINE, tegevused, graaf1);
        double tulemus = hindaja.hinda();
        this.tulemusVal.setText(tulemus * 100 + "%");
    }

    public void reload(Graaf g) {
        graaf.getChildren().removeIf(e -> e instanceof Arrow);
        List<Arrow> nooled = new ArrayList<>();
        for (Kaar kaar : g.kaared) {
            Tipp algus = kaar.algus;
            Tipp lopp = kaar.lopp;
            Arrow arrow = new Arrow(
                    algus.tippGraafil.getCenterX(), algus.tippGraafil.getCenterY(),
                    lopp.tippGraafil.getCenterX(), lopp.tippGraafil.getCenterY(),
                    20
            );

            Text text = new Text("  { " + algus.tähis + " -> " + lopp.tähis + " }  ");
            text = addTextHander(text, g, lopp);
            arrow = addArrowHander(arrow, text, lopp);
            nooled.add(arrow);
        }
        graaf.getChildren().addAll(nooled);
    }

    public void removeStep() {
        Tegevus viimane = tegevused.remove(tegevused.size() - 1);
        switch (viimane.v) {
            case LISA_JARJEKORDA -> {
                tipud.get(viimane.t).setTagasi();
                jarjekord.getChildren().remove(jarjekord.getChildren().size() - 1);
            }

            case EEMALDA_JARJEKORRAST -> {
                Text text = new Text("  { " + "x" + " -> " + viimane.t.tähis + " }  ");
                text.setOnMouseClicked(e -> {
                    Tipp otsitav = null;
                    String s = text.getText().replace("{", "").replace("}", "").replace(" ", "");
                    for (Tipp tipp : graaf1.tipud) {
                        if (String.valueOf(tipp.tähis).equals(s.split("->")[1])) {
                            otsitav = tipp;
                            break;
                        }
                    }
                    TippGraafil t = tipud.get(otsitav);
                    t.setTagasi();
                    jarjekord.getChildren().remove(e.getSource());
                });
                jarjekord.getChildren().add(0, text);
            }
            case TOODELDUD -> {
                tipud.get(viimane.t).setTagasi();
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
            TippGraafil t = tipud.get(otsitav);
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
            tipud.get(lopp).setJarjekorras();
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
        Group g = new Group(tipp, text);
        g.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getX() < graaf.getLayoutX() + 25 || e.getX() > graaf.getLayoutX() + graaf.getWidth() - 35) return;
            if (e.getY() < 30 || e.getY() > graaf.getHeight() - 30) return;
            tipp.setCenterX(e.getX());
            tipp.setCenterY(e.getY());
            text.setX(e.getX() - 3);
            text.setY(e.getY() + 3);
            reload(graaf1);
        });
        return g;
    }
}

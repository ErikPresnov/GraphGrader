package com.example.graphgrader.Algoritm;

import com.example.graphgrader.Graaf.*;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class SügavutiLäbimineLopp {

    public Button andmestruktuur;
    public Pane graafiElement;
    public HBox pseudoMagasin;
    private Deque<Tipp> magasin;
    public HBox pseudoToodeldud;
    private boolean[] toodeldud;
    private boolean[] tootlemisel;
    private Tipp praegune;
    private Graaf graaf;

    public void algVaartusta() throws IOException {
        taastaAlgus();
        String failitee = "test1.txt";
        this.graaf = new Graaf(failitee, true);
        this.magasin = new ArrayDeque<>();
        this.toodeldud = new boolean[graaf.tipud.size()];
        this.tootlemisel = new boolean[graaf.tipud.size()];
        andmestruktuur.setDisable(true);
    }

    public void showGraph(MouseEvent mouseEvent) throws IOException {
        algVaartusta();
        for (int i = 0; i < graaf.tipud.size(); i++) {
            Tipp tipp = graaf.tipud.get(i);
            TippGraafil tippGraafil = new TippGraafil(40,40, 30, tipp);
            tippGraafil = addTippGraafilHander(tippGraafil, tipp);
            tipp.tippGraafil = tippGraafil;
            if (i == 0) {
                praegune = tipp;
                tippGraafil.setPraegune();
            } else
                tippGraafil.setFill(Color.WHITE);
            Text text1 = new Text(tipp.tähis + "");
            Group grupp1 = makeGroup(tippGraafil, text1);
            graafiElement.getChildren().add(grupp1);
        }

        reload(graaf);
    }

    public void taastaAlgus() {
        graafiElement.getChildren().clear();
        pseudoToodeldud.getChildren().clear();
        pseudoMagasin.getChildren().clear();
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
                        true, false
                );
                if (graaf.kaalutud) {
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
        graafiElement.getChildren().addAll(nooled);
        graafiElement.getChildren().addAll(kaalud);
    }

    public Arrow addArrowHander(Arrow arrow, Text text, Tipp algus, Tipp lopp) {
        arrow.setOnMouseClicked(e -> {
            if (algus == praegune && lopp.tippGraafil.getFill() != Color.GREEN && lopp.tippGraafil.getFill() != Color.RED) {
                lopp.tippGraafil.setJarjekorras();
                pseudoMagasin.getChildren().add(text);
                magasin.add(lopp);
            }
        });
        return arrow;
    }

    // true -> korras
    // false -> viga
    // viga -> tipp ise -> kontrollib töödeldud tippu
    // viga -> alluv -> alluv on kontrollimata
    // viga -> null -> ei ole töödeldav
    public record TipuSobivus(boolean korras, Tipp viga) {}

    public TippGraafil addTippGraafilHander(TippGraafil t, Tipp tipp) {
        t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            TipuSobivus tagastus = kontrolli(tipp);
            if (!tagastus.korras) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                Tipp viga = tagastus.viga;
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
            pseudoToodeldud.getChildren().add(new Text(tipp.tähis + "\t"));
            toodeldud[Integer.parseInt(tipp.tähis) - 1] = true;
        });
        return t;
    }

    public TipuSobivus kontrolli(Tipp t) {
        if (t.tippGraafil.getFill() == Color.GREEN) return new TipuSobivus(false, t);
        if (t.tippGraafil.getFill() != Color.RED) return new TipuSobivus(false, null);
        for (Tipp tipp : t.alluvad) {
            if (!(toodeldud[Integer.parseInt(tipp.tähis) - 1] || magasin.contains(tipp))) return new TipuSobivus(false, tipp);
        }
        andmestruktuur.setDisable(false);
        return new TipuSobivus(true, null);
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
        if (graaf == null) return;
        for (Tipp tipp : graaf.tipud) {
            tipp.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
        }
    }

    public void takeElem(MouseEvent ignored) {
        if (magasin.isEmpty()) return;
        Tipp q = magasin.pollLast();
        pseudoMagasin.getChildren().remove(pseudoMagasin.getChildren().size() - 1);
        if (toodeldud[Integer.parseInt(q.tähis) - 1]) return;
        q.tippGraafil.setPraegune();
        praegune = q;
        andmestruktuur.setDisable(true);
    }
}

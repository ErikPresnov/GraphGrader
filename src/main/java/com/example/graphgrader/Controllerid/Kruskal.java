package com.example.graphgrader.Controllerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.Kuhjad.MinHeapKaared;
import com.example.graphgrader.Util.Teavitaja;
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

public class Kruskal {

    public Button andmestruktuur;
    public Pane graafiElement;
    public HBox pseudoJarjekord;
    private MinHeapKaared jarjekord;
    public HBox pseudoToodeldud;
    private boolean[] toodeldud;
    private Tipp praegune;
    private Graaf graaf;
    private int tootlemata;
    private int vigu;

    public void algVaartusta() throws IOException {
        taastaAlgus();
        String failitee = "Graafid\\test2.txt";
        this.graaf = new Graaf(failitee, false);
        this.jarjekord = new MinHeapKaared();
        this.toodeldud = new boolean[graaf.tipud.size()];
        this.tootlemata = graaf.tipud.size();
        andmestruktuur.setDisable(true);
    }

    public void showGraph() throws IOException {
        algVaartusta();
        for (int i = 0; i < graaf.tipud.size(); i++) {
            Tipp tipp = graaf.tipud.get(i);
            TippGraafil tippGraafil = new TippGraafil(40,40, 30, tipp);
            tippGraafil = addTippGraafilHander(tippGraafil, tipp);
            tipp.tippGraafil = tippGraafil;
            tippGraafil.setFill(Color.GREEN);
            Text text1 = new Text(tipp.tähis + "");
            Group grupp1 = makeGroup(tippGraafil, text1);
            graafiElement.getChildren().add(grupp1);
        }

        reload(graaf);
    }

    public void taastaAlgus() {
        graafiElement.getChildren().clear();
        pseudoToodeldud.getChildren().clear();
        pseudoJarjekord.getChildren().clear();
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
                        true, true
                );
                if (graaf.kaalutud) {
                    double midX = arrow.midX;
                    double midY = arrow.midY;
                    Text kaaluText = new Text(String.valueOf(kaar.kaal));
                    kaaluText.setX(midX);
                    kaaluText.setY(midY);
                    kaalud.add(kaaluText);
                }

                arrow = addArrowHander(arrow, kaar);
                kaar.arrow = arrow;
                nooled.add(arrow);
            }
        }
        graafiElement.getChildren().addAll(nooled);
        graafiElement.getChildren().addAll(kaalud);
    }

    public Arrow addArrowHander(Arrow arrow, Kaar k) {
        arrow.setOnMouseClicked(e -> {
            if (k.algus == praegune && k.lopp.tippGraafil.getFill() != Color.GREEN && k.lopp.tippGraafil.getFill() != Color.RED) {
                k.lopp.tippGraafil.setJarjekorras();

                for (Kaar kaar : k.algus.kaared) {
                    if (kaar.lopp == k.lopp) {
                        k.lopp.kaal = kaar.kaal;
                        break;
                    }
                }

                jarjekord.lisa(k);
                arrow.setFill(Color.YELLOW);
                teeKaarVarviliseks(k, Color.YELLOW);

                pseudoJarjekord.getChildren().clear();
                for (Kaar kaar : jarjekord.heap)
                    pseudoJarjekord.getChildren().add(new Text("{(%s%s):%d}\t".formatted(kaar.algus.tähis, kaar.lopp.tähis, kaar.kaal)));

            }
        });
        return arrow;
    }

    public record TipuSobivus(boolean korras, Tipp viga) {}

    public TippGraafil addTippGraafilHander(TippGraafil t, Tipp tipp) {
        t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (praegune == null) {
                t.setPraegune();
                praegune = tipp;
                return;
            }
            TipuSobivus tagastus = kontrolli(tipp);
            if (!tagastus.korras) {
                vigu++;
                String err;
                Tipp viga = tagastus.viga;
                if (viga == null)
                    err = "Tipp {%s} ei ole praegu töödeldav.".formatted(tipp.tähis);
                else if (viga == tipp)
                    err = "Tipp {%s} on juba töödeldud.".formatted(viga.tähis);
                else
                    err = "Tipust {%s} on järglane {%s} vaatlemata.".formatted(tipp.tähis, viga.tähis);

                Teavitaja.teavita(err, Alert.AlertType.ERROR);
                return;
            }
            t.setToodeldud();
            tootlemata--;
            pseudoToodeldud.getChildren().add(new Text(tipp.tähis + "\t"));
            toodeldud[Integer.parseInt(tipp.tähis) - 1] = true;
            if (tootlemata == 0) {
                String teavitus;
                if (vigu == 0)
                    teavitus = "Kõik tipud töödeldud. Läbimäng oli korrektne.";
                else
                    teavitus = "Kõik tipud töödeldud. Läbimängus oli %d viganeTipp".formatted(vigu);
                Teavitaja.teavita(teavitus, Alert.AlertType.INFORMATION);
            }
        });
        return t;
    }

    // true -> korras
    // false -> viganeTipp
    // viganeTipp -> tipp ise -> kontrollib töödeldud tippu
    // viganeTipp -> alluv -> alluv on kontrollimata
    // viganeTipp -> null -> ei ole töödeldav
    public TipuSobivus kontrolli(Tipp t) {
        if (t.tippGraafil.getFill() == Color.GREEN) return new TipuSobivus(false, t);
        if (t.tippGraafil.getFill() != Color.RED) return new TipuSobivus(false, null);
        for (Tipp tipp : t.alluvad) {
            Kaar vastavKaar = null;
            for (Kaar kaar : t.kaared) {
                if (kaar.lopp == tipp) vastavKaar = kaar;
            }
            if (!(toodeldud[Integer.parseInt(tipp.tähis) - 1] || jarjekord.olemas(vastavKaar))) return new TipuSobivus(false, tipp);
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

    public void lock() {
        if (graaf == null) return;
        for (Tipp tipp : graaf.tipud) tipp.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
        teeServad();
    }

    public void teeServad() {
        for (Tipp tipp : graaf.tipud) {
            for (Kaar kaar : tipp.kaared) {
                if (!jarjekord.olemas(kaar) && !jarjekord.olemas(new Kaar(kaar.lopp, kaar.algus))) jarjekord.lisa(kaar);
            }
        }
        pseudoJarjekord.getChildren().clear();
        for (Kaar kaar : jarjekord.heap)
            pseudoJarjekord.getChildren().add(new Text("{(%s%s):%d}\t".formatted(kaar.algus.tähis, kaar.lopp.tähis, kaar.kaal)));
    }

    public void takeElem() {
        if (jarjekord.tühi()) return;
        Kaar q = jarjekord.min();
        pseudoJarjekord.getChildren().remove(0);
        if (toodeldud[Integer.parseInt(q.lopp.tähis) - 1]) return;
        q.lopp.tippGraafil.setPraegune();
        praegune = q.lopp;
        q.arrow.setFill(Color.GREEN);
        teeKaarVarviliseks(q, Color.GREEN);
        andmestruktuur.setDisable(true);
    }

    public void teeKaarVarviliseks(Kaar k1, Color c) {
        for (Kaar kaar : k1.lopp.kaared) {
            if (kaar.lopp == k1.algus) {
                kaar.arrow.setFill(c);
                break;
            }
        }
    }
}

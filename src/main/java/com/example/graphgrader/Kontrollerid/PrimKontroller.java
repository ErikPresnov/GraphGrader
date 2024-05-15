package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.KaarteKuhi;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrimKontroller {

    public KaarteKuhi kuhi = new KaarteKuhi();
    public List<Tipp> toodeldud = new ArrayList<>();
    public List<String> sammud = new ArrayList<>(), vead = new ArrayList<>();
    public List<Kaar> ootel = new ArrayList<>(), kasutatud = new ArrayList<>();

    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test2.txt";
    public Button andmestruktuur, laeNupp, lukustaNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;
    public int samm = 1;


    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, false);
        naitaGraafi();
        laeNupp.setVisible(false);
        andmestruktuur.setDisable(true);
        lukustaNupp.setVisible(true);
    }

    public void naitaGraafi() {
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            TippGraafil tippEkraanil = new TippGraafil(40, 40, 30, praeguneTipp);
            tippEkraanil.setFill(Color.WHITE);
            praeguneTipp.tippGraafil = tippEkraanil;
            if (i == 0) praeguneTipp.setPraegune();

            graafiElement.getChildren().add(lisaTipuKasitleja(tippEkraanil));
        }
        uuenda();
    }

    public void lisaKaareKasitleja(Arrow kaar) {
        Kaar k = kaar.kaar;
        kaar.setOnMouseClicked(e -> {
            if (k.algus.seis == Tipp.TipuSeis.PRAEGUNE && (k.lopp.seis == Tipp.TipuSeis.ANDMESTRUKTUURIS || k.lopp.seis == Tipp.TipuSeis.AVASTAMATA)) {
                if (kuhi.sisaldab(k)) {
                    sammud.add(samm + "\t: Lisan serva " + k + " järjekorda. VIGA");
                    vead.add(samm++ + "\t: Serv on järjekorras juba olemas.");
                    Teavitaja.teavita("Serva topelt lisamine", Alert.AlertType.ERROR);
                    return;
                }
                sammud.add(samm++ + "\t: Lisan serva " + k + " järjekorda. KORRAS");
                kuhi.lisa(k);
                k.lopp.setAndmestruktuuris();
                kaar.setFill(Color.ORANGE);
                ootel.add(k);
                for (Kaar k1 : k.lopp.kaared)
                    if (k1.lopp == k.algus) {
                        k1.arrow.setFill(Color.ORANGE);
                        ootel.add(k1);
                        break;
                    }
                kuvaStruktuurid();
            } else if (k.algus.seis == Tipp.TipuSeis.PRAEGUNE && k.lopp.seis == Tipp.TipuSeis.TÖÖDELDUD) {
                sammud.add(samm + "\t: Lisan serva " + kaar + " järjekorda. VIGA");
                vead.add(samm++ + "\t: Serva lõpptipp " + k.lopp.tähis + " on juba töödeldud.");
                Teavitaja.teavita("Serva lõpptipp on juba töödeldud.", Alert.AlertType.ERROR);
            }
        });
    }

    public Group lisaTipuKasitleja(TippGraafil tipp) {
        Text tekst = new Text(tipp.tipp.tähis);
        tipp.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getX() < graafiElement.getLayoutX() + 35) return;
            if (e.getX() > graafiElement.getLayoutX() + graafiElement.getWidth() - 35) return;
            if (e.getY() < 35) return;
            if (e.getY() > graafiElement.getHeight() - 35) return;
            tipp.setCenterX(e.getX());
            tipp.setCenterY(e.getY());
            tekst.setX(e.getX() - 3);
            tekst.setY(e.getY() + 3);
            uuenda();
        });

        Group g = new Group(tipp, tekst);
        tekst.toFront();
        return g;
    }

    public void kuvaStruktuurid() {
        int pikkus = 0;
        pseudoStruktuur.getChildren().clear();
        for (Kaar t : kuhi.kuhi) {
            if (pikkus > 60) {
                pseudoStruktuur.getChildren().add(new Text("\t ..."));
                break;
            } else {
                pseudoStruktuur.getChildren().add(new Text("\t %s%s:%d".formatted(t.algus.tähis, t.lopp.tähis, t.kaal)));
                pikkus += 6 + t.algus.tähis.length() + t.lopp.tähis.length() + String.valueOf(t.kaal).length();
            }
        }

        pseudoToodeldud.getChildren().clear();
        for (Tipp t : toodeldud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis));
    }

    public void lukustaGraaf(MouseEvent ignored) {
        lukustaNupp.setVisible(false);
        for (Tipp t : g.tipud) {
            t.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, MouseEvent::consume);
            lisaKontrollija(t.tippGraafil);
        }
    }

    public void lisaKontrollija(TippGraafil tipp) {
        tipp.setOnMouseClicked(e -> { // Klikk ehk kontrollimine
            String kontrolliTulemus = kontrolli(tipp);
            if (kontrolliTulemus.equals("")) {
                sammud.add(samm++ + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". KORRAS");
                tipp.tipp.setToodeldud();
                toodeldud.add(tipp.tipp);
                kuvaStruktuurid();
                andmestruktuur.setDisable(false);
                return;
            }
            sammud.add(samm + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". VIGA");
            vead.add(samm++ + "\t: " +  kontrolliTulemus);
            Teavitaja.teavita(kontrolliTulemus, Alert.AlertType.ERROR);
        });
    }

    public void uuenda() {
        graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        graafiElement.getChildren().removeIf(e -> e instanceof Text);
        List<Text> kaalud = new ArrayList<>();
        List<Arrow> kaared = new ArrayList<>();

        for (Tipp t : g.tipud) {
            for (Kaar k : t.kaared) {
                Arrow kaar = new Arrow(
                        k.algus.tippGraafil.getCenterX(), k.algus.tippGraafil.getCenterY(),
                        k.lopp.tippGraafil.getCenterX(), k.lopp.tippGraafil.getCenterY(),
                        true, true, k
                );
                k.arrow = kaar;
                lisaKaareKasitleja(kaar);
                kaared.add(kaar);

                if (g.kaalutud)
                    kaalud.add(new Text(kaar.midX, kaar.midY, String.valueOf(kaar.kaar.kaal)));
            }
        }

        graafiElement.getChildren().addAll(kaared);
        graafiElement.getChildren().addAll(kaalud);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (kuhi.onTyhi()) {
            if (toodeldud.size() == g.tipud.size()) {
                Logija.logi(vead, g, sammud, "Prim", true, false);
                Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()), Alert.AlertType.INFORMATION);
            }
            andmestruktuur.setDisable(true);
            return;
        }
        Kaar min = kuhi.min();
        sammud.add(samm++ + "\t: Võtsin järjekorrast järgmise kaare " + min + ". KORRAS");
        kuvaStruktuurid();
        if (min.lopp.seis == Tipp.TipuSeis.TÖÖDELDUD) return;
        min.arrow.setFill(Color.GREEN);
        min.lopp.setPraegune();
        kasutatud.add(min);
        for (Kaar k : min.lopp.kaared) {
            if (k.lopp == min.algus) {
                k.arrow.setFill(Color.GREEN);
                kasutatud.add(k);
                break;
            }
        }
        andmestruktuur.setDisable(true);
    }

    public String kontrolli(TippGraafil t) {
        Tipp tipp = t.tipp;
        if (tipp.seis == Tipp.TipuSeis.TÖÖDELDUD) return "Tipp {%s} on töödeldud".formatted(tipp.tähis);
        if (tipp.seis != Tipp.TipuSeis.PRAEGUNE) return "Tipp {%s} ei ole praegu töödeldav".formatted(tipp.tähis);
        for (Kaar kaar : tipp.kaared)
            if (!ootel.contains(kaar) && !kasutatud.contains(kaar))
                return "Järglane {%s} ei ole töödeldud ega andmestruktuuris".formatted(kaar.lopp.tähis);

        return "";
    }
}

package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Graaf.Tipp.TipuSeis;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.fxml.FXML;
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


public class LaiutiKontroller {

    public List<Tipp> jarjekord = new ArrayList<>(), toodeldud = new ArrayList<>();
    public List<String> sammud = new ArrayList<>(), vead = new ArrayList<>();

    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test2.txt";
    public Button andmestruktuur, laeNupp, lukustaNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;
    public int samm = 1;


    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, true);
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
            if (k.algus.seis == TipuSeis.PRAEGUNE && k.lopp.seis == TipuSeis.AVASTAMATA) {
                jarjekord.add(k.lopp);
                sammud.add(samm++ + ": Lisan tipu " + k.lopp.tähis + " järjekorda. KORRAS");
                k.lopp.setAndmestruktuuris();
                kuvaStruktuurid();
            } else if (k.algus.seis == TipuSeis.PRAEGUNE && (k.lopp.seis == TipuSeis.ANDMESTRUKTUURIS || k.lopp.seis == TipuSeis.TÖÖDELDUD)) {
                sammud.add(samm++ + " : Lisan tipu " + k.lopp.tähis + " järjekorda. VIGA");
                String error = samm + " : Lõpptipp " + k.lopp.tähis + " on juba töödeldud või andmestruktuuris.";
                vead.add(error);
                Teavitaja.teavita(error, Alert.AlertType.ERROR);
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

        return new Group(tipp, tekst);
    }

    public void kuvaStruktuurid() {
        pseudoStruktuur.getChildren().clear();
        for (Tipp t : jarjekord) pseudoStruktuur.getChildren().add(new Text("\t" + t.tähis));

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
                sammud.add(samm++ + ": Kontrollin tippu " + tipp.tipp.tähis + ". KORRAS");
                tipp.tipp.setToodeldud();
                if (toodeldud.contains(tipp.tipp)) return;
                toodeldud.add(tipp.tipp);
                kuvaStruktuurid();
                andmestruktuur.setDisable(false);
                return;
            }
            sammud.add(samm++ + " : Kontrollin tippu " + tipp.tipp.tähis + ". VIGA");
            vead.add(samm + " : " + kontrolliTulemus);
            Teavitaja.teavita(kontrolliTulemus, Alert.AlertType.ERROR);
        });
    }

    public void uuenda() {
        graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        List<Arrow> kaared = new ArrayList<>();

        for (Tipp t : g.tipud) {
            for (Kaar k : t.kaared) {
                Arrow kaar = new Arrow(
                        k.algus.tippGraafil.getCenterX(), k.algus.tippGraafil.getCenterY(),
                        k.lopp.tippGraafil.getCenterX(), k.lopp.tippGraafil.getCenterY(),
                        true, false, k
                );
                lisaKaareKasitleja(kaar);
                kaared.add(kaar);
            }
        }

        graafiElement.getChildren().addAll(kaared);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (jarjekord.isEmpty()) {
            if (toodeldud.size() == g.tipud.size()) {
                Logija.logi(vead, g, sammud);
                Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi kirjutatud faili \"out.txt\"".formatted(vead.size()), Alert.AlertType.INFORMATION);
            }
            andmestruktuur.setDisable(true);
            return;
        }
        Tipp t = jarjekord.remove(0);
        sammud.add(samm++ + " : Võtsin järjekorrast järgmise tipu " + t.tähis);
        if (t.seis != TipuSeis.TÖÖDELDUD) t.setPraegune();
        if (t.seis != TipuSeis.TÖÖDELDUD) andmestruktuur.setDisable(true);
        kuvaStruktuurid();
    }

    public String kontrolli(TippGraafil t) {
        Tipp tipp = t.tipp;
        if (tipp.seis != TipuSeis.PRAEGUNE) return "Tipp %s ei ole praegu töödeldav".formatted(tipp.tähis);
        for (Tipp alluv : tipp.alluvad)
            if (alluv.seis != TipuSeis.ANDMESTRUKTUURIS && alluv.seis != TipuSeis.TÖÖDELDUD)
                return "Kõik järglased ei ole töödeldud.";

        return "";
    }
}

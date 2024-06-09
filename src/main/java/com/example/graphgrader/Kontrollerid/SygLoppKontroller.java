package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SygLoppKontroller {
    public List<Tipp> magasin = new ArrayList<>(), toodeldud = new ArrayList<>(), ootel = new ArrayList<>(), jarglased = new ArrayList<>();
    public List<String> vead = new ArrayList<>(), sammud = new ArrayList<>();

    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test2.txt";
    public Button andmestruktuur, laeNupp, lukustaNupp;
    public HBox pseudoStruktuur, pseudoToodeldud, pseudoOotel;
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

            graafiElement.getChildren().add(lisaTipuLiigutaja(tippEkraanil));
        }
        uuenda();
    }

    public Group lisaTipuLiigutaja(TippGraafil tipp) {
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
        for (Tipp t : magasin) {
            if (pikkus > 60) {
                pseudoStruktuur.getChildren().add(new Text("\t ..."));
                break;
            } else {
                pseudoStruktuur.getChildren().add(new Text("\t" + t.tähis));
                pikkus += 4 + t.tähis.length();
            }
        }

        pseudoToodeldud.getChildren().clear();
        for (Tipp t : toodeldud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis));

        pseudoOotel.getChildren().clear();
        for (Tipp t : ootel) pseudoOotel.getChildren().add(new Text("\t" + t.tähis));
    }

    public void lukustaGraaf(MouseEvent ignored) {
        lukustaNupp.setVisible(false);
        for (Tipp t : g.tipud) {
            t.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, MouseEvent::consume);
            lisaKontrollija(t.tippGraafil);
        }
    }

    public void lisaKontrollija(TippGraafil tipp) {
        tipp.setOnMouseClicked(e -> {
            if (tipp.tipp.seis == Tipp.TipuSeis.PRAEGUNE || tipp.tipp.seis == Tipp.TipuSeis.OOTEL) {
                String kontrolliTulemus = kontrolli(tipp);
                if (kontrolliTulemus.equals("")) {
                    if (tipp.tipp.seis == Tipp.TipuSeis.OOTEL) {
                        sammud.add(samm++ + "\t: Kontrollin tippu(2) " + tipp.tipp.tähis + ". KORRAS");
                        tipp.tipp.setToodeldud();
                        toodeldud.add(tipp.tipp);
                        ootel.remove(tipp.tipp);
                    } else {
                        sammud.add(samm++ + "\t: Kontrollin tippu(1) " + tipp.tipp.tähis + ". KORRAS");
                        ootel.add(tipp.tipp);
                        tipp.tipp.setOotel();
                    }
                    kuvaStruktuurid();
                    andmestruktuur.setDisable(false);
                    jarglased.clear();
                    return;
                }
                sammud.add(samm + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". VIGA");
                vead.add(samm++ + "\t: " + kontrolliTulemus);
                Teavitaja.teavita(kontrolliTulemus, "Viga");
            } else {
                Tipp praegune = leiaPraegune();
                Tipp jarglane = null;
                if (praegune == null) return;
                for (Tipp t : praegune.alluvad) if (t == tipp.tipp) {jarglane = t;break;}
                if (jarglane == null) {
                    sammud.add(samm + "\t: Lisan tipu " + tipp.tipp.tähis + " magasini. VIGA");
                    vead.add(samm++ + "\t: Lõpptipp " + tipp.tipp.tähis + " ei ole praeguse tipu järglane.");
                    Teavitaja.teavita("Lõpptipp " + tipp.tipp.tähis + " ei ole praeguse tipu järglane.", "Viga");
                    return;
                }
                if (jarglane.seis == Tipp.TipuSeis.AVASTAMATA) {
                    magasin.add(jarglane);
                    sammud.add(samm++ + "\t: Lisan tipu " + jarglane.tähis + " magasini. KORRAS");
                    jarglased.add(jarglane);
                    jarglane.setAndmestruktuuris();
                    kuvaStruktuurid();
                } else if (jarglane.seis == Tipp.TipuSeis.ANDMESTRUKTUURIS || jarglane.seis == Tipp.TipuSeis.TÖÖDELDUD) {
                    sammud.add(samm + "\t: Lisan tipu " + jarglane.tähis + " magasini. VIGA");
                    vead.add(samm++ + "\t: Lõpptipp " + jarglane.tähis + " on juba töödeldud või andmestruktuuris.");
                    Teavitaja.teavita("Lõpptipp " + jarglane.tähis + " on juba töödeldud või andmestruktuuris.", "Viga");
                }
            }
        });
    }

    private Tipp leiaPraegune() {
        for (Tipp tipp : g.tipud)
            if (tipp.seis == Tipp.TipuSeis.PRAEGUNE)
                return tipp;
        return null;
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
                kaared.add(kaar);
            }
        }

        graafiElement.getChildren().addAll(kaared);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (magasin.isEmpty()) {
            if (toodeldud.size() == g.tipud.size()) {
                Logija.logi(vead, g, sammud, "SügavutiLõpp", false, false);
                Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()), "Info");
            }
            andmestruktuur.setDisable(true);
            return;
        }
        Tipp t = pidiTootlema();
        if (t != null) {
            sammud.add(samm + "\t: Mingit tippu pidi praegu töötlema. VIGA");
            vead.add(samm++ + "\t: Tippu " + t.tähis + " pidi praegu töötlema.");
            Teavitaja.teavita("Mingit tippu pidi praegu töötlema.", "Viga");
            return;
        }
        t = magasin.remove(magasin.size() - 1);
        sammud.add(samm++ + "\t: Võtsin magasinist järgmise tipu " + t.tähis + ". KORRAS");
        kuvaStruktuurid();
        if (t.seis == Tipp.TipuSeis.TÖÖDELDUD || t.seis == Tipp.TipuSeis.OOTEL) return;
        andmestruktuur.setDisable(true);
        t.setPraegune();
    }

    public String kontrolli(TippGraafil t) {
        Tipp tipp = t.tipp;
        Tipp.TipuSeis s = tipp.seis;
        if (s == Tipp.TipuSeis.TÖÖDELDUD) return "Tipp {%s} on töödeldud".formatted(tipp.tähis);
        if (s == Tipp.TipuSeis.AVASTAMATA || s == Tipp.TipuSeis.ANDMESTRUKTUURIS) return "Tipp ei ole praegu töödeldav.";
        if (s == Tipp.TipuSeis.PRAEGUNE) {
            // Esimest korda --> kas tipp jääb ootele?
            // Ükski alluv ei tohi olla avastamata

            for (Tipp alluv : tipp.alluvad) {
                if (alluv.seis == Tipp.TipuSeis.AVASTAMATA)
                    return "Järglane {%s} on töötlemata".formatted(alluv.tähis);
                else if (alluv.seis == Tipp.TipuSeis.TÖÖDELDUD || alluv.seis == Tipp.TipuSeis.OOTEL)
                    return "Mingi alluv tipp on töötlemata.";
            }
        } else if (s == Tipp.TipuSeis.OOTEL) {
            // Teist korda --> kontrollime kas kõik järglased on töödeldud
            Tipp jargmineOotel = ootel.get(ootel.size() - 1);
            if (jargmineOotel != tipp) return "Praegu ei tohiks tippu " +  tipp.tähis + " töödelda.";
            for (Tipp alluv : tipp.alluvad)
                if (alluv.seis != Tipp.TipuSeis.TÖÖDELDUD && alluv.seis != Tipp.TipuSeis.OOTEL)
                    return "Järglane {%s} ei ole töödeldud ega ootel".formatted(alluv.tähis);
        }

        return "";
    }

    public Tipp pidiTootlema() { // tagastab tipu, kui teda pidi töötlema
        if (ootel.size() == 0) return null;
        Tipp jargmine = ootel.get(ootel.size() - 1);

        for (Tipp tipp : jargmine.alluvad)
            if (tipp.seis != Tipp.TipuSeis.TÖÖDELDUD && tipp.seis != Tipp.TipuSeis.OOTEL)
                return null;

        return jargmine;
    }
}

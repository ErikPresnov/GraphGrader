package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BFKontroller {

    public List<Kaar> kaarteJarjekord = new ArrayList<>();
    public List<String> sammud = new ArrayList<>(), vead = new ArrayList<>();

    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test2.txt";
    public Button andmestruktuur, laeNupp, lukustaNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;
    public int samm = 1;
    public boolean vahetus = false;


    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, true);
        for (Tipp tipp : g.tipud)
            kaarteJarjekord.addAll(tipp.kaared);

        kaarteJarjekord.add(null);
        naitaGraafi();
        laeNupp.setVisible(false);
        andmestruktuur.setDisable(true);
        lukustaNupp.setVisible(true);
    }

    public void naitaGraafi() {
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            praeguneTipp.kaal = Integer.MAX_VALUE;
            if (i == 0) praeguneTipp.kaal = 0;
            TippGraafil tippEkraanil = new TippGraafil(40, 40, 30, praeguneTipp);
            tippEkraanil.setFill(Color.WHITE);
            praeguneTipp.tippGraafil = tippEkraanil;

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

        return new Group(tipp, tekst);
    }

    public void kuvaStruktuurid() {
        int pikkus = 0;
        pseudoStruktuur.getChildren().clear();
        for (Kaar k : kaarteJarjekord) {
            if (pikkus > 60) {
                pseudoStruktuur.getChildren().add(new Text("\t ..."));
                break;
            }
            if (k == null) {
                pseudoStruktuur.getChildren().add(new Text("\t |"));
                pikkus += 6;
            } else {
                pseudoStruktuur.getChildren().add(new Text("\t" + k.algus.tähis + k.lopp.tähis + ":" + k.kaal));
                pikkus += 5 + k.lopp.tähis.length() + k.algus.tähis.length() + String.valueOf(k.kaal).length();
            }
        }

        pseudoToodeldud.getChildren().clear();
        for (Tipp t : g.tipud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis + " : " + (t.kaal == Integer.MAX_VALUE ? "INF" : t.kaal)));
    }

    public void lukustaGraaf(MouseEvent ignored) {
        lukustaNupp.setVisible(false);
        andmestruktuur.setDisable(false);
        kuvaStruktuurid();

        for (Tipp t : g.tipud) t.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, MouseEvent::consume);
    }

    public void kysiSisendit(Kaar k, int oodatud) {
        boolean korras = false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Kaal?");
        Optional<String> sisend = dialog.showAndWait();
        while (!korras) {
            while (sisend.isEmpty()) sisend = dialog.showAndWait();
            String sisendiSisu = sisend.get();
            try {
                if (Integer.parseInt(sisendiSisu) != oodatud) {
                    String kontrolliTulemus = "Tipu %s kaal peaks olema %d aga on %d".formatted(k.lopp.tähis, oodatud, Integer.parseInt(sisend.get()));
                    sammud.add(samm + "\t: Küsisin kaalu tipu " + k.lopp.tähis + " kohta. VIGA");
                    vead.add(samm++ + "\t: " + kontrolliTulemus);
                    Teavitaja.teavita(kontrolliTulemus, "Viga");
                    sisend = Optional.empty();
                    continue;
                }
                sammud.add(samm++ + "\t: Küsisin kaalu tipu " + k.lopp.tähis + " kohta. KORRAS");
                korras = true;
            } catch (NumberFormatException exception) {
                Teavitaja.teavita("Sisesta number", "Info");
                sisend = Optional.empty();
            }
        }
    }

    public boolean kysiSisendit() {
        List<String> pos = List.of("jah", "ja", "j", "1"), neg = List.of("ei", "e", "0");
        boolean korras = false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Kas lõpetada?");
        Optional<String> sisend = dialog.showAndWait();
        boolean tulemus = false;
        while (!korras) {
            while (sisend.isEmpty()) sisend = dialog.showAndWait();
            String sisendiSisu = sisend.get().toLowerCase();
            if (pos.contains(sisendiSisu) || neg.contains(sisendiSisu)) {
                korras = true;
                tulemus = pos.contains(sisendiSisu);
            } else {
                Teavitaja.teavita("Sisesta 'jah', 'ja', 'j', '1' või 'ei', 'e', '0'", "Info");
                sisend = Optional.empty();
            }
        }
        return tulemus;
    }

    public void uuenda() {
        graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        graafiElement.getChildren().removeIf(e -> e instanceof Text);
        List<Arrow> kaared = new ArrayList<>();
        List<Text> kaalud = new ArrayList<>();

        for (Tipp t : g.tipud) {
            for (Kaar k : t.kaared) {
                Arrow kaar = new Arrow(
                        k.algus.tippGraafil.getCenterX(), k.algus.tippGraafil.getCenterY(),
                        k.lopp.tippGraafil.getCenterX(), k.lopp.tippGraafil.getCenterY(),
                        true, false, k
                );

                k.arrow = kaar;
                kaared.add(kaar);

                if (g.kaalutud)
                    kaalud.add(new Text(kaar.midX, kaar.midY, String.valueOf(kaar.kaar.kaal)));
            }
        }

        graafiElement.getChildren().addAll(kaalud);
        graafiElement.getChildren().addAll(kaared);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        Kaar k = kaarteJarjekord.remove(0);
        if (k == null) {
            boolean kasutaja = !kysiSisendit();
            if (vahetus == kasutaja)
                sammud.add(samm++ + "\t: Küsin lõpetamise kohta. KORRAS");
            else {
                sammud.add(samm + "\t: Küsin lõpetamise kohta. VIGA");
                vead.add(samm++ + "\t: Ootasin " + vahetus + " aga sain " + kasutaja);
            }
            while (vahetus != kasutaja) {
                Teavitaja.teavita("Vale vastus", "Viga");
                sammud.add(samm + "\t: Küsin lõpetamise kohta. VIGA");
                vead.add(samm++ + "\t: Ootasin " + vahetus + " aga sain " + kasutaja);
                kasutaja = !kysiSisendit();
            }
            if (!vahetus) {
                Logija.logi(vead, g, sammud, "BF", true, false);
                Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()), "Info");
                andmestruktuur.setDisable(true);
                return;
            }
            kaarteJarjekord.add(null);
            kuvaStruktuurid();
            vahetus = false;
            return;
        }
        sammud.add(samm++ + "\t: Võtsin järgmise kaare " + k);
        k.arrow.setFill(Color.RED);
        kaarteJarjekord.add(k);
        int jargmineOodatud = Math.min(k.algus.kaal + k.kaal, k.lopp.kaal);
        kysiSisendit(k, jargmineOodatud);
        if (k.lopp.kaal != jargmineOodatud) vahetus = true;
        k.lopp.kaal = jargmineOodatud;
        k.arrow.setFill(Color.DARKGREY);
        kuvaStruktuurid();
    }
}

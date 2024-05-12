package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.KaarteKuhi;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.scene.Group;
import javafx.scene.control.Alert;
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

public class KruskalKontroller {

    public KaarteKuhi kuhi = new KaarteKuhi();
    public List<Tipp> toodeldud = new ArrayList<>();
    public List<String> vead = new ArrayList<>(), sammud = new ArrayList<>();
    public Tipp[] esindajad;
    public int samm = 1;
    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test2.txt";
    public Button andmestruktuur, lukustaNupp, laeNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;

    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, false);
        naitaGraafi();
        laeNupp.setVisible(false);
        esindajad = new Tipp[g.tipud.size()];
        for (int i = 0; i < g.tipud.size(); i++) esindajad[i] = g.tipud.get(i);
        lukustaNupp.setVisible(true);
        andmestruktuur.setDisable(true);
    }

    public void naitaGraafi() {
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            TippGraafil tippEkraanil = new TippGraafil(40, 40, 30, praeguneTipp);
            praeguneTipp.tippGraafil = tippEkraanil;
            praeguneTipp.setToodeldud();
            graafiElement.getChildren().add(lisaTipuKasitleja(tippEkraanil));
            for (Kaar k : praeguneTipp.kaared) if (k.algus.tähis.compareTo(k.lopp.tähis) < 0) kuhi.lisa(k);
        }
        uuenda();
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
            }
            pseudoStruktuur.getChildren().add(new Text("\t %s%s:%d".formatted(t.algus.tähis, t.lopp.tähis, t.kaal)));
            pikkus += 6 + t.algus.tähis.length() + t.lopp.tähis.length() + String.valueOf(t.kaal).length();
        }

        pseudoToodeldud.getChildren().clear();
        for (Tipp t : toodeldud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis));
    }

    public void lukustaGraaf(MouseEvent ignored) {
        lukustaNupp.setVisible(false);
        andmestruktuur.setDisable(false);
        for (Tipp t : g.tipud) t.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, MouseEvent::consume);
        kuvaStruktuurid();
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
            Logija.logi(vead, g, sammud);
            Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi kirjutatud faili \"out.txt\"".formatted(vead.size()), Alert.AlertType.INFORMATION);
            andmestruktuur.setDisable(true);
            return;
        }
        Kaar min = kuhi.min();
        kuvaStruktuurid();

        teeKaaredVarviliseks(min, Color.ORANGE);
        boolean kasKuulub = leiaKuuluvus(min);
        boolean vastus = kysiSisendit();
        while (vastus != kasKuulub) {
            Teavitaja.teeTeavitus("Sain vale vastuse", Alert.AlertType.ERROR).showAndWait();
            vastus = kysiSisendit();
        }
        if (kasKuulub) teeKaaredVarviliseks(min, Color.GREEN);
        else teeKaaredVarviliseks(min, Color.RED);
    }

    public void teeKaaredVarviliseks(Kaar k1, Color c) {
        k1.arrow.setFill(c);
        for (Kaar k : k1.lopp.kaared) {
            if (k.lopp == k1.algus) {
                k.arrow.setFill(c);
                return;
            }
        }
    }

    private boolean leiaKuuluvus(Kaar k) {
        Tipp a = leiaEsindaja(k.algus), b = leiaEsindaja(k.lopp);

        if (a != b) esindajad[a.tähis.charAt(0) - 'A'] = b;
        return a != b;
    }

    public Tipp leiaEsindaja(Tipp t) {
        int idx = t.tähis.charAt(0) - 'A';
        while (esindajad[idx] != t) {
            t = esindajad[idx];
            idx = t.tähis.charAt(0) - 'A';
        }
        return t;
    }

    public boolean kysiSisendit() {
        List<String> pos = List.of("jah", "ja", "j", "1"), neg = List.of("ei", "e", "0");
        boolean korras = false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Kas kuulub?");
        Optional<String> sisend = dialog.showAndWait();
        boolean tulemus = false;
        while (!korras) {
            while (sisend.isEmpty()) sisend = dialog.showAndWait();
            String sisendiSisu = sisend.get().toLowerCase();
            if (pos.contains(sisendiSisu) || neg.contains(sisendiSisu)) {
                korras = true;
                tulemus = pos.contains(sisendiSisu);
            } else {
                Teavitaja.teeTeavitus("Sisesta 'jah', 'ja', 'j', '1' või 'ei', 'e', '0'", Alert.AlertType.INFORMATION).showAndWait();
                sisend = Optional.empty();
            }
        }
        return tulemus;
    }

}

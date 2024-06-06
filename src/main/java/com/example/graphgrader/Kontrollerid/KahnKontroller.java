package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KahnKontroller {

    public List<Tipp> jarjekord = new ArrayList<>(), toodeldud = new ArrayList<>();
    public List<String> vead = new ArrayList<>(), sammud = new ArrayList<>();
    public int[] paris, olemas;

    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test3.txt";
    public Button laeNupp, andmestruktuur, lukustaNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;
    public int samm = 1;
    public GridPane tabel;

    public void joonistaTabel() {
        Button kontrollNupp = new Button("Kontrolli");
        kontrollNupp.setOnMouseClicked(mouseEvent -> {
            try{
                kontrolliMassiive();
                sammud.add(samm++ + "\t: Kontrollin algseid sisendastmeid. KORRAS");
                kontrollNupp.setVisible(false);
                andmestruktuur.setDisable(false);
            } catch (RuntimeException e) {
                sammud.add(samm + "\t: Kontrollin algseid sisendastmeid. VIGA");
                vead.add(samm++ + "\t: Ootasin: " + Arrays.toString(paris) + " , sain: " + Arrays.toString(olemas));
                Teavitaja.teavita("Sisendastmed on vigased", Alert.AlertType.INFORMATION);
            }
        });
        tabel.add(kontrollNupp, 0, 0, 2, 1);
        tabel.setHgap(5);
        tabel.setVgap(5);
        tabel.add(new Text("Tähis"), 0, 1);
        tabel.add(new Text("Sisendaste"), 1, 1, 2,1);
        for (int i = 0; i < g.tipud.size(); i++) {
            tabel.add(new Text(g.tipud.get(i).tähis), 0, i + 2);
            Text luger = new Text("0");
            tabel.add(luger, 1, i + 2);
            final int finalI = i;

            Button alla = new Button("-");
            alla.setOnMouseClicked(e -> {
                if (olemas[finalI] == 0) {
                    Teavitaja.teavita("Tipu sisendaste ei saa olla negatiivne", Alert.AlertType.ERROR);
                    return;
                }
                olemas[finalI]--;
                luger.setText(String.valueOf(olemas[finalI]));
            });
            alla.setPrefWidth(25);

            Button yles = new Button("+");
            yles.setOnMouseClicked(e -> {
                olemas[finalI]++;
                luger.setText(String.valueOf(olemas[finalI]));
            });
            yles.setPrefWidth(25);

            Button lisa = new Button("Lisa");
            lisa.setOnMouseClicked(e -> {
                if (kontrollNupp.isVisible()) return;
                if (olemas[g.tipud.get(finalI).tähis.charAt(0) - 'A'] == 0) {
                    sammud.add(samm++ + "\t: Lisan tipu " + g.tipud.get(finalI).tähis + " järjekorda. KORRAS");
                    g.tipud.get(finalI).setAndmestruktuuris();
                    jarjekord.add(g.tipud.get(finalI));
                    lisa.setVisible(false);
                    alla.setVisible(false);
                    yles.setVisible(false);
                    kuvaStruktuurid();
                } else {
                    sammud.add(samm + "\t: Lisan tipu " + g.tipud.get(finalI).tähis + " järjekorda. VIGA");
                    vead.add(samm++ + "\t: Järjekorda lisatava tipu sisendaste peaks olema 0 mitte " + olemas[g.tipud.get(finalI).tähis.charAt(0) - 'A']);
                    Teavitaja.teavita("Tipu %s sisendaste ei ole 0!".formatted(g.tipud.get(finalI).tähis), Alert.AlertType.INFORMATION);
                }
            });
            lisa.setPrefWidth(40);

            tabel.add(alla, 2, i + 2);
            tabel.add(yles, 3, i + 2);
            tabel.add(lisa, 4, i + 2);
        }
    }

    private void kontrolliMassiive() {
        for (int i = 0; i < paris.length; i++)
            if (paris[i] != olemas[i]) throw new RuntimeException();
    }

    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, true);
        olemas = new int[g.tipud.size()];
        paris = new int[g.tipud.size()];
        for (Tipp tipp : g.tipud) for (Tipp t : tipp.alluvad) paris[t.tähis.charAt(0) - 'A']++;
        
        naitaGraafi();
        laeNupp.setVisible(false);
        lukustaNupp.setVisible(true);
        andmestruktuur.setDisable(true);
        joonistaTabel();
    }

    public void naitaGraafi() {
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            TippGraafil tippEkraanil = new TippGraafil(40, 40, 30, praeguneTipp);
            tippEkraanil.setFill(Color.WHITE);
            praeguneTipp.tippGraafil = tippEkraanil;

            graafiElement.getChildren().add(lisaTipuKasitleja(tippEkraanil));
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
        for (Tipp t : jarjekord) {
            if (pikkus > 60) {
                pseudoStruktuur.getChildren().add(new Text("\t ..."));
            } else {
                pseudoStruktuur.getChildren().add(new Text("\t" + t.tähis));
                pikkus += 4 + t.tähis.length();
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
            vead.add(samm++ + "\t: " + kontrolliTulemus.replaceAll("\n", " "));
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
                k.arrow = kaar;
                kaared.add(kaar);
            }
        }

        graafiElement.getChildren().addAll(kaared);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (jarjekord.isEmpty()) {
            if (toodeldud.size() == g.tipud.size()) {
                Logija.logi(vead, g, sammud, "Kahn", false, false);
                Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()), Alert.AlertType.INFORMATION);
                andmestruktuur.setDisable(true);
            }
            return;
        }
        Tipp t = jarjekord.remove(0);
        sammud.add(samm++ + "\t: Võtsin järjekorrast järgmise tipu " + t.tähis + ". KORRAS");
        kuvaStruktuurid();
        t.setPraegune();
        andmestruktuur.setDisable(true);
        for (Tipp tipp : t.alluvad) paris[tipp.tähis.charAt(0) - 'A']--;
    }

    public String kontrolli(TippGraafil t) {
        if (t.tipp.seis != Tipp.TipuSeis.PRAEGUNE) return "Tipp " + t.tipp.tähis + " ei ole praegu töödeldav.";
        try {
            t.tipp.setToodeldud();
            kontrolliMassiive();
        } catch (RuntimeException e) {
            t.tipp.setPraegune();
            return "Sisendastmetes viga.%nOodatud %s.%nSaadud %s".formatted(Arrays.toString(paris), Arrays.toString(olemas));
        }
        return "";
    }
}

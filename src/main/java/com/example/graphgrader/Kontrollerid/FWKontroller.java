package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Graaf.Tipp.TipuSeis;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class FWKontroller {

    public List<Tipp> toodeldud = new ArrayList<>();
    public List<String> vead = new ArrayList<>(), sammud = new ArrayList<>();
     int[][] seis;

    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test2.txt";
    public HBox pseudoToodeldud;

    public Button lukustaNupp, laeNupp;
    public GridPane tabel;
    public int fikseeritud = 0, samm = 1, tehtud = 0;

    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, true);
        naitaGraafi();
        laeNupp.setVisible(false);
        lukustaNupp.setVisible(true);
        teeTabel();
    }

    public void teeTabel() {
        seis = new int[g.tipud.size()][g.tipud.size()];
        for (int i = 0; i < g.tipud.size(); i++)
            for (Kaar k : g.tipud.get(i).kaared)
                seis[i][k.lopp.tähis.charAt(0) - 'A'] = k.kaal;

        for (int i = 0; i < seis.length; i++)
            for (int i1 = 0; i1 < seis[i].length; i1++)
                if (i != i1 && seis[i][i1] == 0) seis[i][i1]--;

        kuvaStruktuurid();
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

        return new Group(tipp, tekst);
    }

    public void kuvaStruktuurid() {
        pseudoToodeldud.getChildren().clear();
        for (Tipp t : toodeldud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis));

        tabel.getChildren().clear();
        tabel.setPadding(new Insets(2));
        for (int i = 0; i < seis.length; i++) { // esimene rida
            Text t1 = new Text(g.tipud.get(i).tähis + "\t");
            t1.setFont(new Font(14));
            StackPane p1 = new StackPane(t1);
            if (i == fikseeritud) p1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            tabel.add(p1, i + 1, 0);
        }

        for (int i = 0; i < seis.length; i++) {
            Text t1 = new Text(g.tipud.get(i).tähis + "\t");
            t1.setFont(new Font(14));
            StackPane p1 = new StackPane(t1);
            if (i == fikseeritud) p1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            tabel.add(p1, 0, i + 1);
            for (int i1 = 0; i1 < seis.length; i1++) {
                Button b3 = new Button(seis[i][i1] + "\t");
                lisaKontroll(b3, i, i1);
                b3.setPadding(new Insets(1, 2, 1, 2));
                if (i == i1 && i1 != fikseeritud) {
                    b3.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
                    b3.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                if (i1 == fikseeritud || i == fikseeritud) b3.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                tabel.add(b3, i1 + 1, i + 1);
            }
        }
    }

    private void lisaKontroll(Button b, int i, int j) {
        b.setOnMouseClicked(e -> {
            if (i == fikseeritud || j == fikseeritud) return;
            Paint iV = g.tipud.get(i).tippGraafil.getFill(), jV = g.tipud.get(j).tippGraafil.getFill();
            g.tipud.get(i).tippGraafil.setFill(Color.YELLOW);
            g.tipud.get(j).tippGraafil.setFill(Color.ORANGE);
            int otse = seis[i][j], oodatud;
            if (seis[i][fikseeritud] != -1 && seis[fikseeritud][j] != - 1 &&
               (seis[i][fikseeritud] + seis[fikseeritud][j] < otse || otse == -1))
                oodatud = seis[i][fikseeritud] + seis[fikseeritud][j];
            else oodatud = otse;
            b.setText(kysiSisendit(g.tipud.get(j), oodatud) + "\t");
            seis[i][j] = oodatud;
            tehtud++;
            b.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
            b.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            g.tipud.get(i).tippGraafil.setFill(iV);
            g.tipud.get(j).tippGraafil.setFill(jV);
        });
    }

    public int kysiSisendit(Tipp t, int oodatud) {
        boolean korras = false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Kaal?");
        Optional<String> sisend = dialog.showAndWait();
        while (!korras) {
            while (sisend.isEmpty()) sisend = dialog.showAndWait();
            String sisendiSisu = sisend.get();
            try {
                if (Integer.parseInt(sisendiSisu) != oodatud) {
                    String kontrolliTulemus = "Tipu %s kaal peaks olema %d aga on %d".formatted(t.tähis, oodatud, Integer.parseInt(sisend.get()));
                    sammud.add(samm + "\t: Küsin tipu " + t.tähis + " kaalu. VIGA");
                    vead.add(samm++ + "\t: " + kontrolliTulemus);
                    Teavitaja.teeTeavitus(kontrolliTulemus, Alert.AlertType.ERROR).showAndWait();
                    sisend = Optional.empty();
                    continue;
                }
                sammud.add(samm++ + "\t: Küsin tipu " + t.tähis + " kaalu. KORRAS");
                korras = true;
            } catch (NumberFormatException exception) {
                Teavitaja.teeTeavitus("Sisesta number", Alert.AlertType.INFORMATION).showAndWait();
                sisend = Optional.empty();
            }
        }
        return Integer.parseInt(sisend.get());
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
                sammud.add(samm++ + "\t: Töötlen tippu " + tipp.tipp.tähis + ". KORRAS");
                toodeldud.add(tipp.tipp);
                tipp.tipp.setToodeldud();

                if (toodeldud.size() == g.tipud.size()) {
                    Logija.logi(vead, g, sammud, "FW", true, false);
                    Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()), Alert.AlertType.INFORMATION);
                    return;
                }

                g.tipud.get(++fikseeritud).setPraegune();
                kuvaStruktuurid();
                return;
            }
            sammud.add(samm + "\t: Töötlen tippu " + tipp.tipp.tähis + ". VIGA");
            vead.add(samm++ + "\t: " + kontrolliTulemus);
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
                        true, false, k
                );
                kaared.add(kaar);

                if (g.kaalutud)
                    kaalud.add(new Text(kaar.midX, kaar.midY, String.valueOf(kaar.kaar.kaal)));
            }
        }

        graafiElement.getChildren().addAll(kaared);
        graafiElement.getChildren().addAll(kaalud);
    }

    public String kontrolli(TippGraafil t) {
        if (t.tipp.seis != TipuSeis.PRAEGUNE) return "Ei ole praegu töödeldav tipp";
        if (tehtud != (g.tipud.size() - 2)*(g.tipud.size() - 1))
                return "Midagi puudu";

        tehtud = 0;
        return "";
    }
}

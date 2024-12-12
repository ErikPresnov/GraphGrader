package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Util.GraafiValija;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import com.example.graphgrader.Util.TippudeKuhi;
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

public class DijkstraKontroller {

    public TippudeKuhi kuhi = new TippudeKuhi();
    public List<Tipp> toodeldud = new ArrayList<>();
    public List<String> vead = new ArrayList<>(),sammud = new ArrayList<>();
    public int samm = 1;

    public Pane graafiElement;
    public Graaf g;
    public String failitee = GraafiValija.valiSuvaline("graafid/suunatud_kaalutud");
    public Button andmestruktuuriNupp, lukustaNupp, laeNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;


    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, true);
        naitaGraafi();
        laeNupp.setVisible(false);
        lukustaNupp.setVisible(true);
        andmestruktuuriNupp.setDisable(true);
    }

    public void naitaGraafi() {
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            TippGraafil tippEkraanil = new TippGraafil(40, 40, 30, praeguneTipp);
            tippEkraanil.setFill(Color.WHITE);
            praeguneTipp.tippGraafil = tippEkraanil;
            if (i == 0) praeguneTipp.setPraegune();
            praeguneTipp.kaal = 0;
            graafiElement.getChildren().add(lisaTipuLiigutaja(tippEkraanil));
        }
        uuenda();
    }

    public int kysiSisendit(Kaar k, int oodatud) {
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
        return Integer.parseInt(sisend.get());
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
        for (Tipp t : kuhi.kuhi) {
            if (pikkus > 60) {
                pseudoStruktuur.getChildren().add(new Text("\t ..."));
                break;
            }
            pseudoStruktuur.getChildren().add(new Text("\t %s:%d".formatted(t.tähis, t.kaal)));
            pikkus += 6 + t.tähis.length() + String.valueOf(t.kaal).length();
        }

        pseudoToodeldud.getChildren().clear();
        for (Tipp t : toodeldud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis + ":" + t.kaal));
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
            if (tipp.tipp.seis == TipuSeis.PRAEGUNE) {
                String kontrolliTulemus = kontrolli(tipp);
                if (kontrolliTulemus.equals("")) {
                    sammud.add(samm++ + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". KORRAS");
                    toodeldud.add(tipp.tipp);
                    tipp.tipp.setToodeldud();
                    kuvaStruktuurid();
                    andmestruktuuriNupp.setDisable(false);
                    return;
                }
                sammud.add(samm + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". VIGA");
                vead.add(samm++ + "\t: " + kontrolliTulemus);
                Teavitaja.teavita(kontrolliTulemus, "Viga");
            } else {
                Tipp praegune = leiaPraegune();
                if (praegune == null) return;
                Kaar k = praegune.kaared.stream().filter(x -> x.lopp == tipp.tipp).findFirst().get();
                if (tipp.tipp.seis == TipuSeis.AVASTAMATA) { // Uus tipp seega kuhja parandust ei saa olla
                    tipp.tipp.kaal = kysiSisendit(k, praegune.kaal + k.kaal);
                    kuhi.lisa(tipp.tipp);
                    tipp.tipp.setAndmestruktuuris();
                }
                else if (k.lopp.seis == TipuSeis.ANDMESTRUKTUURIS) { // Mingi tipp teist korda, potentsiaalne kuhjaparandus
                    // Kaks juhtu, kui praegune kaal on vaiksem kui uus --> ei tee midagi
                    if (k.lopp.kaal < k.algus.kaal + k.kaal) { // sellisel juhul ootame sisendiks vana kaalu
                        k.lopp.kaal = kysiSisendit(k, k.lopp.kaal);
                    } else if (k.algus.kaal + k.kaal < k.lopp.kaal) { // kui uus tee on parem kui vana siis tahame uuendada kaalu
                        k.lopp.kaal = kysiSisendit(k, k.algus.kaal + k.kaal);
                        kuhi.kuhjasta(); // Võtme parandus
                    }
                }

                kuvaStruktuurid();
            }
        });
    }

    private Tipp leiaPraegune() {
        for (Tipp tipp : g.tipud)
            if (tipp.seis == TipuSeis.PRAEGUNE)
                return tipp;
        return null;
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

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (kuhi.onTyhi()) {
            if (toodeldud.size() == g.tipud.size()) {
                Logija.logi(vead, g, sammud, "Dijkstra", true, false);
                Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi kirjutatud faili \"out.txt\"".formatted(vead.size()), "Info");
            }
            andmestruktuuriNupp.setDisable(true);
            return;
        }
        Tipp t = kuhi.min();
        sammud.add(samm++ + "\t: Võtsin järjekorrast järgmise tipu " + t.tähis + ". KORRAS");
        t.setPraegune();
        andmestruktuuriNupp.setDisable(true);
        kuvaStruktuurid();
    }

    public String kontrolli(TippGraafil t) {
        Tipp tipp = t.tipp;
        if (tipp.seis == TipuSeis.TÖÖDELDUD) return "Tipp {%s} on töödeldud".formatted(tipp.tähis);
        if (tipp.seis != TipuSeis.PRAEGUNE) return "Tipp {%s} ei ole praegu töödeldav".formatted(tipp.tähis);
        for (Tipp alluv : tipp.alluvad)
            if (alluv.seis != TipuSeis.ANDMESTRUKTUURIS && alluv.seis != TipuSeis.TÖÖDELDUD)
                return "Järglane {%s} ei ole töödeldud ega andmestruktuuris".formatted(alluv.tähis);

        for (Kaar kaar : tipp.kaared)
            if (kaar.lopp.kaal > kaar.kaal + tipp.kaal)
                return "Tipu {%s} kaugus on suurem kui oodatud".formatted(kaar.lopp.tähis);

        return "";
    }
}

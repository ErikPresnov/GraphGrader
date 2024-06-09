package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Graaf.Tipp.TipuSeis;
import com.example.graphgrader.Util.Logija;
import com.example.graphgrader.Util.Teavitaja;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class EeldusGraafKontroller {
    public List<Tipp> topoloogilineJarjestus = new ArrayList<>(), sisestatud = new ArrayList<>();
    public boolean[] varaseimLoppOlemas, hilisemAlgusOlemas;
    public int koguAeg, samm = 1;
    public List<String> vead = new ArrayList<>(), sammud = new ArrayList<>();
    public boolean edasi = true;
    public Rectangle algus, lopp;

    public GridPane tabel;
    public Pane graafiElement;
    public Graaf g;
    public String failitee = "Graafid\\test3.txt";
    public Button laeNupp, lukustaNupp, andmestruktuur;

    public HBox pseudoStruktuur, pseudoToodeldud;
    public Label topsort;


    public void laeGraaf(MouseEvent ignored) throws IOException {
        g = new Graaf(failitee, true, true);
        varaseimLoppOlemas = new boolean[g.tipud.size()];
        hilisemAlgusOlemas = new boolean[g.tipud.size()];
        naitaGraafi();
        lopp.setFill(Color.RED);
        laeNupp.setVisible(false);
        lukustaNupp.setVisible(true);
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
        Text tekst1 = new Text(tipp.tipp.tähis);
        Text tekst2 = new Text(String.valueOf(tipp.tipp.kaal));
        Text tekst3 = new Text("0");
        Text tekst4 = new Text("0");
        tipp.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getX() < graafiElement.getLayoutX() + 35) return;
            if (e.getX() > graafiElement.getLayoutX() + graafiElement.getWidth() - 35) return;
            if (e.getY() < 35) return;
            if (e.getY() > graafiElement.getHeight() - 35) return;
            tipp.setCenterX(e.getX());
            tipp.setCenterY(e.getY());
            tekst1.setX(e.getX() - 13);tekst1.setY(e.getY() - 7);
            tekst2.setX(e.getX() + 7);tekst2.setY(e.getY() - 7);
            tekst3.setX(e.getX() - 13);tekst3.setY(e.getY() + 13);
            tekst4.setX(e.getX() + 7);tekst4.setY(e.getY() + 13);
            uuenda();
        });

        tekst3.setOnMouseClicked(e -> {
            if (tipp.tipp.seis != TipuSeis.PRAEGUNE) return;
            if (!edasi) return;
            int tulemus = kysiSisendit(tipp.tipp, tipp.tipp.kaal + maxEelastest(tipp.tipp), "Varaseim lõpuaeg?");
            tipp.tipp.varaseimLopp = tulemus;
            tekst3.setText(String.valueOf(tulemus));
            varaseimLoppOlemas[g.tipud.indexOf(tipp.tipp)] = true;
            jargmine();
            koguAeg = Math.max(koguAeg, tulemus);
        });

        tekst4.setOnMouseClicked(e -> {
            if (tipp.tipp.seis != TipuSeis.PRAEGUNE) return;
            if (edasi) return;
            int tulemus = kysiSisendit(tipp.tipp, minJarglastest(tipp.tipp) - tipp.tipp.kaal, "Hiliseim algusaeg?");
            tipp.tipp.hiliseimAlgus = tulemus;
            tekst4.setText(String.valueOf(tulemus));
            hilisemAlgusOlemas[g.tipud.indexOf(tipp.tipp)] = true;
            jargmine();
        });

        return new Group(tipp, tekst1 ,tekst2, tekst3, tekst4);
    }

    private void jargmine() {
        if (edasi) {
            if (g.tipud.get(g.tipud.size() - 1).seis == TipuSeis.PRAEGUNE) {
                lopp.setFill(Color.WHITE);
                algus.setFill(Color.RED);
                kysiLoppu();
                edasi = false;
            } else {
                int index = leiaPraegune();
                topoloogilineJarjestus.get(index).setAvastamata();
                topoloogilineJarjestus.get(index + 1).setPraegune();
                sammud.add(samm++ + "\t: Liigun järgmise tipu " + topoloogilineJarjestus.get(index + 1).tähis + " juurde. KORRAS");
            }
        } else {
            if (g.tipud.get(0).seis == TipuSeis.PRAEGUNE) {
                g.tipud.get(0).setToodeldud();
                algus.setFill(Color.WHITE);
                kysiKriitilised();
            } else {
                int index = leiaPraegune();
                topoloogilineJarjestus.get(index).setToodeldud();
                topoloogilineJarjestus.get(index - 1).setPraegune();
                sammud.add(samm++ + "\t: Liigun eelmise tipu " + topoloogilineJarjestus.get(index - 1).tähis + " juurde. KORRAS");
            }
        }
        uuenda();
    }

    private int leiaPraegune() {
        for (int i = 0; i < topoloogilineJarjestus.size(); i++)
            if (topoloogilineJarjestus.get(i).seis == TipuSeis.PRAEGUNE)
                return i;
        return -1;
    }

    private void kysiKriitilised() {
        List<Tipp> kriitilised = new ArrayList<>();
        for (Tipp t : topoloogilineJarjestus) if (t.hiliseimAlgus + t.kaal == t.varaseimLopp) kriitilised.add(t);

        while (!kriitilised.isEmpty()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Kriitiline tipp?");
            Optional<String> sisend = dialog.showAndWait();
            boolean korras = false;
            while (!korras) {
                while (sisend.isEmpty()) sisend = dialog.showAndWait();
                String sisendiSisu = sisend.get();
                Tipp t = leiaTipp(sisendiSisu);
                if (t == null) {
                    String kontrolliTulemus = "Sellist tippu ei eksisteeri.";
                    sammud.add(samm + "\t: Küsin kriitilist tippu. VIGA");
                    vead.add(samm++ + "\t: " + kontrolliTulemus);
                    Teavitaja.teavita(kontrolliTulemus, "Viga");
                    sisend = Optional.empty();
                } else if (!kriitilised.contains(t)) {
                    String kontrolliTulemus;
                    if (sisestatud.contains(t)) kontrolliTulemus = "See tipp on juba sisestatud.";
                    else kontrolliTulemus = "Sisestatud tipp ei ole kriitiline.";
                    sammud.add(samm + "\t: Küsin kriitilist tippu. VIGA");
                    vead.add(samm++ + "\t: " + kontrolliTulemus);
                    Teavitaja.teavita(kontrolliTulemus, "Viga");
                    sisend = Optional.empty();
                } else {
                    sammud.add(samm + "\t: Küsin kriitilist tippu. KORRAS");
                    t.tippGraafil.setFill(Color.ORANGERED);
                    sisestatud.add(t);
                    kriitilised.remove(t);
                    korras = true;
                    kuvaStruktuurid();
                }
            }
        }
        Logija.logi(vead, g, sammud, "Eeldusgraaf", false, true);
        Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()), "Info");
    }

    private void kysiLoppu() {
        boolean korras = false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Kogu projekti (graafi) varaseim lopuaeg?");
        Optional<String> sisend = dialog.showAndWait();
        while (!korras) {
            while (sisend.isEmpty()) sisend = dialog.showAndWait();
            String sisendiSisu = sisend.get();
            try {
                if (Integer.parseInt(sisendiSisu) != koguAeg) {
                    String kontrolliTulemus = "Kogu lõpuaeg peaks olema %d aga sisestati %s".formatted(koguAeg, Integer.parseInt(sisend.get()));
                    sammud.add(samm + "\t: Küsin kogu lõpuaega. VIGA");
                    vead.add(samm++ + "\t: " + kontrolliTulemus);
                    Teavitaja.teavita(kontrolliTulemus, "Viga");
                    sisend = Optional.empty();
                    continue;
                }
                sammud.add(samm + "\t: Küsin kogu lõpuaega. KORRAS");
                korras = true;
            } catch (NumberFormatException exception) {
                Teavitaja.teavita("Sisesta number", "Info");
                sisend = Optional.empty();
            }
        }
    }

    private int maxEelastest(Tipp tipp) {
        int max = 0;
        for (Tipp eelane : g.tipud)
            for (Tipp alluv : eelane.alluvad)
                if (alluv == tipp)
                    max = Math.max(max, eelane.varaseimLopp);
        return max;
    }

    private int minJarglastest(Tipp tipp) {
        int min = koguAeg;
        for (Tipp t : tipp.alluvad)
            min = Math.min(min, t.hiliseimAlgus);
        return min;
    }

    public int kysiSisendit(Tipp t, int oodatud, String mida) {
        boolean korras = false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(mida);
        Optional<String> sisend = dialog.showAndWait();
        while (!korras) {
            while (sisend.isEmpty()) sisend = dialog.showAndWait();
            String sisendiSisu = sisend.get();
            try {
                if (Integer.parseInt(sisendiSisu) != oodatud) {
                    String kontrolliTulemus = "Tipu %s %s peaks olema %d aga on %d".formatted(t.tähis, mida, oodatud, Integer.parseInt(sisend.get()));
                    sammud.add(samm + "\t: Küsin tipu " + t.tähis + " " + mida + ". VIGA");
                    vead.add(samm++ + "\t: " + kontrolliTulemus);
                    Teavitaja.teavita(kontrolliTulemus, "Viga");
                    sisend = Optional.empty();
                    continue;
                }
                korras = true;
                sammud.add(samm++ + "\t: Küsin tipu " + t.tähis + " " + mida + ". KORRAS");
            } catch (NumberFormatException exception) {
                Teavitaja.teavita("Sisesta number", "Info");
                sisend = Optional.empty();
            }
        }
        return Integer.parseInt(sisend.get());
    }

    public void kuvaStruktuurid() {
        pseudoToodeldud.getChildren().clear();
        for (Tipp t : sisestatud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis));
    }

    public void lukustaGraaf(MouseEvent ignored) {
        lukustaNupp.setVisible(false);
        for (Tipp t : g.tipud) t.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, MouseEvent::consume);

        kysiTopSort();
    }

    private void kysiTopSort() {
        boolean korras = false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Topoloogiline jarjestus?");
        Optional<String> sisend = dialog.showAndWait();
        while (!korras) {
            while (sisend.isEmpty()) sisend = dialog.showAndWait();
            String sisendiSisu = sisend.get();
            String error = sobib(sisendiSisu);
            if (error != null) {
                String kontrolliTulemus = "Sisestus ei ole sobiv topoloogiline jarjestus";
                Teavitaja.teavita(kontrolliTulemus, "Viga");
                sammud.add(samm + "\t: Küsin topolooglist järjestust. VIGA");
                vead.add(samm++ + "\t: " + kontrolliTulemus);
                sisend = Optional.empty();
                continue;
            }
            korras = true;
            sammud.add(samm++ + "\t: Küsin topoloogilist järjestust. KORRAS");
        }
    }

    private String sobib(String sisendiSisu) {
        String[] jupid = sisendiSisu.split(",");
        if (jupid.length != g.tipud.size()) return "Topoloogiline järjestus ei sisalda piisavalt tippe.";

        List<Tipp> potentsiaalne = new ArrayList<>();
        List<Tipp> jargmised = new ArrayList<>();
        Map<Tipp, Integer> sisendid = new HashMap<>();
        for (Tipp t : g.tipud)
            for (Tipp a : t.alluvad) {
                sisendid.putIfAbsent(a, 0);
                sisendid.put(a, sisendid.get(a) + 1);
            }

        for (Tipp tipp : g.tipud) if (!sisendid.containsKey(tipp)) jargmised.add(tipp);

        for (String s : jupid) {
            Tipp vastav = leiaTipp(s);
            if (vastav == null) return "Topoloogiline järjestus sisaldab tippe, mida ei eksisteeri.";

            if (!jargmised.contains(vastav)) return "Topoloogiline järjestus on vigane.";

            for (Tipp t : vastav.alluvad) {
                sisendid.put(t, sisendid.get(t) - 1);
                if (sisendid.get(t) == 0) jargmised.add(t);
            }
            jargmised.remove(vastav);
            potentsiaalne.add(vastav);
        }

        topoloogilineJarjestus = potentsiaalne;
        topoloogilineJarjestus.get(0).setPraegune();
        topsort.setText(potentsiaalne.stream().map(t -> t.tähis + "\t").collect(Collectors.joining()));
        return null;
    }

    private Tipp leiaTipp(String nimi) {
        for (Tipp tipp : g.tipud)
            if (Objects.equals(tipp.tähis, nimi))
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
}

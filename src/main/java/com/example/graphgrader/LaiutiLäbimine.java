package com.example.graphgrader;

import com.example.graphgrader.Graaf.*;
import com.example.graphgrader.Hindaja.Tegevus;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class LaiutiLäbimine {

    public Pane graaf;
    public HBox jarjekord;
    public HBox toodeldud;
    private final double tipuSuurus = 30;
    public Label tulemusVal = new Label("0");
    private List<Tegevus> tegevused = new ArrayList<>();
    private List<Tegevus> oiged = new ArrayList<>();
    private Map<Tipp, TippGraafil> tipud = new HashMap<>();

    private Graaf graaf1;

    public void showGraph(MouseEvent mouseEvent) throws IOException {
        graaf.getChildren().remove(0, graaf.getChildren().size());
        tegevused.clear();
        toodeldud.getChildren().remove(0, toodeldud.getChildren().size());
        jarjekord.getChildren().remove(0, jarjekord.getChildren().size());
        tulemusVal.setText("0");
        graaf.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        String failitee = "test1.txt";
        this.graaf1 = new Graaf(failitee);
        this.oiged = lahenda(graaf1);

        Tipp algus = graaf1.tipud.get(0);
        TippGraafil tippGraafil = new TippGraafil(40,40, tipuSuurus, algus);
        tipud.put(algus, tippGraafil);
        tippGraafil.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            tippGraafil.setToodeldud();
            tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, algus));
            toodeldud.getChildren().add(new Text(algus.tähis + " "));
        });
        algus.tippGraafil = tippGraafil;
        tippGraafil.setFill(Color.RED);
        Text text = new Text(algus.tähis + "");
        Group grupp = new Group(tippGraafil, text);
        grupp.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getX() < graaf.getLayoutX() + 25 || e.getX() > graaf.getLayoutX() + graaf.getWidth() - 35) return;
            if (e.getY() < 30 || e.getY() > graaf.getHeight() - 30) return;
            tippGraafil.setCenterX(e.getX());
            tippGraafil.setCenterY(e.getY());
            text.setX(e.getX() - 3);
            text.setY(e.getY() + 3);
            reload(graaf1);
        });

        graaf.getChildren().add(grupp);
        for (int i = 1; i < graaf1.tipud.size(); i++) {
            Tipp tipp = graaf1.tipud.get(i);
            TippGraafil tippGraafil1 = new TippGraafil(40,40, tipuSuurus, tipp);
            tipud.put(tipp, tippGraafil1);
            tippGraafil1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                tippGraafil1.setToodeldud();
                tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, tipp));
                toodeldud.getChildren().add(new Text(tipp.tähis + " "));
            });
            tipp.tippGraafil = tippGraafil1;
            tippGraafil1.setFill(Color.WHITE);
            Text text1 = new Text(tipp.tähis + "");
            Group grupp1 = new Group(tippGraafil1, text1);
            grupp1.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
                if (e.getX() < graaf.getLayoutX() + 25 || e.getX() > graaf.getLayoutX() + graaf.getWidth() - 35) return;
                if (e.getY() < 30 || e.getY() > graaf.getHeight() - 30) return;
                tippGraafil1.setCenterX(e.getX());
                tippGraafil1.setCenterY(e.getY());
                text1.setX(e.getX() - 3);
                text1.setY(e.getY() + 3);
                reload(graaf1);
            });
            graaf.getChildren().add(grupp1);
        }

        reload(graaf1);
    }

    public void CheckSolution(MouseEvent event) {
        double tulemus = kontrolli();
        this.tulemusVal.setText(String.valueOf(tulemus * 100) + "%");
    }

    public void reload(Graaf g) {
        graaf.getChildren().removeIf(e -> e instanceof Arrow);
        List<Arrow> nooled = new ArrayList<>();
        for (Kaar kaar : g.kaared) {
            Tipp algus = kaar.algus;
            Tipp lopp = kaar.lopp;
            Arrow arrow = new Arrow(
                    algus.tippGraafil.getCenterX(),
                    algus.tippGraafil.getCenterY(),
                    lopp.tippGraafil.getCenterX(),
                    lopp.tippGraafil.getCenterY(),
                    20
            );

            Text text = new Text("  { " + algus.tähis + " -> " + lopp.tähis + " }  ");
            text.setOnMouseClicked(e -> {
                Tipp otsitav = null;
                String s = text.getText().replace("{", "").replace("}", "").replace(" ", "");
                for (Tipp tipp : g.tipud) {
                    if (String.valueOf(tipp.tähis).equals(s.split("->")[1])) {
                        otsitav = tipp;
                        break;
                    }
                }
                TippGraafil t = tipud.get(otsitav);
                if (t.getFill() != Color.GREEN)
                    t.setPraegune();
                jarjekord.getChildren().remove(e.getSource());
                tegevused.add(new Tegevus(Tegevus.Voimalus.EEMALDA_JARJEKORRAST, lopp));
            });
            arrow.setOnMouseClicked(e -> {
                tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_JARJEKORDA, lopp));
                tipud.get(lopp).setJarjekorras();
                jarjekord.getChildren().add(text);
            });
            nooled.add(arrow);
        }
        graaf.getChildren().addAll(nooled);
    }

    public double kontrolli() {
        List<Tegevus> oiged = this.oiged;
        List<Tegevus> tehtud = this.tegevused;

        int valed = 0;
        for (int i = 0; i < Math.min(tehtud.size(), oiged.size()); i++) {
            if (!oiged.get(i).eq(tehtud.get(i))) {
                valed++;
            }
        }

        return 1.0 - (1.0 * valed / oiged.size());
    }

    public List<Tegevus> lahenda(Graaf g) {
        List<Tegevus> tegevused = new ArrayList<>();

        Deque<Tipp> ootel = new ArrayDeque<>();
        List<Tipp> toodeldud = new ArrayList<>();


        for (Tipp tipp : g.tipud.get(0).alluvad) {
            ootel.add(tipp);
            tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_JARJEKORDA, tipp));
        }

        tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, g.tipud.get(0)));
        toodeldud.add(g.tipud.get(0));

        while (!ootel.isEmpty()) {
            Tipp praegune = ootel.pop();
            tegevused.add(new Tegevus(Tegevus.Voimalus.EEMALDA_JARJEKORRAST, praegune));
            if (toodeldud.contains(praegune)) continue;
            for (Kaar kaar : praegune.kaared) {
                ootel.push(kaar.lopp);
                tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_JARJEKORDA, kaar.lopp));
            }
            tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, praegune));
            toodeldud.add(praegune);
        }

        return tegevused;
    }

    public void removeStep() {
        Tegevus viimane = tegevused.remove(tegevused.size() - 1);
        switch (viimane.v) {
            case LISA_JARJEKORDA -> {
                tipud.get(viimane.t).setTagasi();
                jarjekord.getChildren().remove(jarjekord.getChildren().size() - 1);
            }

            case EEMALDA_JARJEKORRAST -> {
                Text text = new Text("  { " + "x" + " -> " + viimane.t.tähis + " }  ");
                text.setOnMouseClicked(e -> {
                    Tipp otsitav = null;
                    String s = text.getText().replace("{", "").replace("}", "").replace(" ", "");
                    for (Tipp tipp : graaf1.tipud) {
                        if (String.valueOf(tipp.tähis).equals(s.split("->")[1])) {
                            otsitav = tipp;
                            break;
                        }
                    }
                    TippGraafil t = tipud.get(otsitav);
                    t.setTagasi();
                    jarjekord.getChildren().remove(e.getSource());
                });
                jarjekord.getChildren().add(0, text);
            }
            case TOODELDUD -> {
                tipud.get(viimane.t).setTagasi();
                toodeldud.getChildren().remove(toodeldud.getChildren().size() - 1);
            }
        }
    }
}

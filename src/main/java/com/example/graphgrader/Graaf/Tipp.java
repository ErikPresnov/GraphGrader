package com.example.graphgrader.Graaf;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Tipp {

    public String tähis;
    public TippGraafil tippGraafil;
    public List<Tipp> alluvad = new ArrayList<>();
    public List<Kaar> kaared = new ArrayList<>();
    public TipuSeis seis = TipuSeis.AVASTAMATA;
    public int kaal;
    public int varaseimLopp = 0, hiliseimAlgus = 0;
    public int x = 40, y = 40, syg = -1;
    public Tipp(String tähis) {
        this.tähis = tähis;
    }

    public void lisaAlluv(Tipp t) {
        this.alluvad.add(t);
    }

    public void setPraegune() {
        this.seis = TipuSeis.PRAEGUNE;
        this.tippGraafil.setFill(Color.RED);
    }

    public void setToodeldud() {
        this.seis = TipuSeis.TÖÖDELDUD;
        this.tippGraafil.setFill(Color.GREEN);
    }

    public void setAndmestruktuuris() {
        this.seis = TipuSeis.ANDMESTRUKTUURIS;
        this.tippGraafil.setFill(Color.ORANGE);
    }

    public void setOotel() {
        this.seis = TipuSeis.OOTEL;
        this.tippGraafil.setFill(Color.YELLOW);
    }

    public void setAvastamata() {
        this.seis = TipuSeis.AVASTAMATA;
        this.tippGraafil.setFill(Color.WHITE);
    }
}

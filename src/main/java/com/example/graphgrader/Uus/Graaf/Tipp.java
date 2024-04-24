package com.example.graphgrader.Uus.Graaf;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Tipp {
    public enum TipuSeis {
        AVASTAMATA, ANDMESTRUKTUURIS, PRAEGUNE, TÖÖDELDUD
    }
    public String tähis;
    public TippGraafil tippGraafil;
    public List<Tipp> alluvad = new ArrayList<>();
    public List<Kaar> kaared = new ArrayList<>();
    public TipuSeis seis = TipuSeis.AVASTAMATA;
    public int kaal;
    public Tipp(String tähis) {
        this.tähis = tähis;
    }

    public void lisaAlluv(Tipp t) {
        this.alluvad.add(t);
    }

    public void muudaSeisu(TipuSeis uus) {
        switch (uus) {
            case ANDMESTRUKTUURIS -> tippGraafil.setJarjekorras();
            case PRAEGUNE -> tippGraafil.setPraegune();
            case TÖÖDELDUD -> tippGraafil.setToodeldud();
            case AVASTAMATA -> tippGraafil.setFill(Color.WHITE);
        }
        this.seis = uus;
    }
}

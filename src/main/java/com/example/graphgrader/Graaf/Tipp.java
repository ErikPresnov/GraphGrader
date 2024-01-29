package com.example.graphgrader.Graaf;

import java.util.ArrayList;
import java.util.List;

public class Tipp {
    public String tähis;
    public TippGraafil tippGraafil;
    public List<Tipp> alluvad = new ArrayList<>();
    public List<Kaar> kaared = new ArrayList<>();
    public int index;

    public Tipp(String tähis) {
        this.tähis = tähis;
    }

    public void lisaAlluv(Tipp t) {
        this.alluvad.add(t);
    }

    public void uuendaAsukoht(TippGraafil g) {
        this.tippGraafil = g;
    }

    public void setIndex(int i) {this.index = i;}
    public int getIndex() {return index;}
}

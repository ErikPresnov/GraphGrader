package com.example.graphgrader.Graaf;

public class Kaar {
    public Tipp algus;
    public Tipp lopp;
    public int kaal;
    public Arrow arrow;

    public Kaar(Tipp algus, Tipp lopp, int kaal) {
        this.algus = algus;
        this.lopp = lopp;
        this.kaal = kaal;
    }

    public Kaar(Tipp algus, Tipp lopp) {
        this.algus = algus;
        this.lopp = lopp;
    }

    public String toString() {
        return algus.tähis + "->" + lopp.tähis + ": " + kaal;
    }
}

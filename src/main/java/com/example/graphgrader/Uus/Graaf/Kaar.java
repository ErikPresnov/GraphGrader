package com.example.graphgrader.Uus.Graaf;

public class Kaar {
    public Tipp algus;
    public Tipp lopp;
    public int kaal;
    public Kaar jargmine;
    public Arrow arrow;

    public Kaar(Tipp algus, Tipp lopp, int kaal, Kaar kaar) {
        this.algus = algus;
        this.lopp = lopp;
        this.kaal = kaal;
        this.jargmine = kaar;
    }

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

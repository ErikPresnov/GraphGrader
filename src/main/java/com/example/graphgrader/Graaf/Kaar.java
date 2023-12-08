package com.example.graphgrader.Graaf;

public class Kaar {
    public Tipp algus;
    public Tipp lopp;
    public int kaal;
    public Kaar jargmine;

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

}

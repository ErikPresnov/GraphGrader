package com.example.graphgrader.Hindaja;

import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;

public class Tegevus {

    public Voimalus v;
    public Tipp t;
    public enum Voimalus {
        LISA_JARJEKORDA, EEMALDA_JARJEKORRAST, TOODELDUD
    }

    public Tegevus(Voimalus v, Tipp t) {
        this.v = v;
        this.t = t;
    }

    @Override
    public String toString() {
        return v + " -> " + t.tähis;
    }

    public boolean eq(Tegevus t) {
        return t.t == this.t && t.v == this.v;
    }
}

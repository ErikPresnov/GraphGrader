package com.example.graphgrader.Generator;

import com.example.graphgrader.Graaf.Graaf;
import com.example.graphgrader.Graaf.Tipp;

import java.util.Objects;

public class Generator {
    public Graaf g;

    public Generator(String graaf) {
        if (Objects.equals(graaf, "laiuti"))
            this.g = LaiutiGraaf.genereeri(10);
        else if (Objects.equals(graaf, "prim")) {
            this.g = Prim.genereeri(7);
        }
    }

    public Graaf getG() {
        return g;
    }

    public void setG(Graaf g) {
        this.g = g;
    }
}

package com.example.graphgrader.Generator;

import com.example.graphgrader.Graaf.Graaf;

public class Generator {
    public Graaf g;

    public Generator() {
        this.g = LaiutiGraaf.genereeri(13);
    }

    public Graaf getG() {
        return g;
    }

    public void setG(Graaf g) {
        this.g = g;
    }
}

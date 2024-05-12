package com.example.graphgrader.Graaf;

import javafx.scene.shape.Circle;

public class TippGraafil extends Circle {

    public Tipp tipp;

    public TippGraafil(double centerX, double centerY, double radius, Tipp tipp) {
        super(centerX, centerY, radius);
        this.tipp = tipp;
    }
}

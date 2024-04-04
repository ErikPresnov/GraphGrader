package com.example.graphgrader.Graaf;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TippGraafil extends Circle {

    public Tipp tipp;

    public TippGraafil(double centerX, double centerY, double radius, Tipp tipp) {
        super(centerX, centerY, radius);
        this.tipp = tipp;
    }

    public void setToodeldud() {
        super.setFill(Color.GREEN);
    }

    public void setJarjekorras() {
        super.setFill(Color.ORANGE);
    }

    public void setPraegune() {
        super.setFill(Color.RED);
    }
}

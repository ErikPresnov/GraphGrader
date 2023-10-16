package com.example.graphgrader.Graaf;

import com.example.graphgrader.Hindaja.Tegevus;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class TippGraafil extends Circle {

    public Tipp tipp;
    public Paint viimane = Color.WHITE;

    public TippGraafil(double centerX, double centerY, double radius, Tipp tipp) {
        super(centerX, centerY, radius);
        this.tipp = tipp;
    }

    public void setToodeldud() {
        this.viimane = super.getFill();
        super.setFill(Color.GREEN);
    }

    public void setJarjekorras() {
        this.viimane = super.getFill();
        super.setFill(Color.YELLOW);
    }

    public void setPraegune() {
        this.viimane = super.getFill();
        super.setFill(Color.RED);
    }

    public void setTagasi() {
        super.setFill(viimane);
    }
}

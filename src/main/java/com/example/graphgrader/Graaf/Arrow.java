package com.example.graphgrader.Graaf;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 *
 * @author kn
 */
public class Arrow extends Path {
    private static final double defaultArrowHeadSize = 10.0;

    public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize){
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.DARKGREY);

        double angle = Math.atan2((endY - startY), (endX - startX));
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        int n = 37;

        startX += n*cos;
        startY += n*sin;

        double angle2 = angle - Math.PI;
        double sin2 = Math.sin(angle2);
        double cos2 = Math.cos(angle2);

        endX += n*cos2;
        endY += n*sin2;

        sin = Math.sin(angle - Math.PI / 2.0);
        cos = Math.cos(angle - Math.PI / 2.0);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;

        //Line
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));

        //ArrowHead
        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endX, endY));
    }

    public Arrow(double startX, double startY, double endX, double endY){
        this(startX, startY, endX, endY, defaultArrowHeadSize);
    }
}
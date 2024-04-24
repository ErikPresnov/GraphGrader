package com.example.graphgrader.Uus.Graaf;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 *
 * @author kn
 */
public class Arrow extends Path {
    private static final double defaultArrowHeadSize = 15.0;
    public double midX;
    public double midY;

    public Arrow(double startX, double startY, double endX, double endY, boolean showArrow, boolean doubled){
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.DARKGREY);

        double angle = Math.atan2((endY - startY), (endX - startX));
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        int n = 35;

        startX += n * cos;
        startY += n * sin;

        double angle2 = angle - Math.PI;
        double sin2 = Math.sin(angle2);
        double cos2 = Math.cos(angle2);

        endX += n * cos2;
        endY += n * sin2;

        if (doubled)
            getElements().add(new MoveTo(startX + 15 * cos, startY + 15 * sin));
        else
            getElements().add(new MoveTo(startX , startY));
        getElements().add(new LineTo(endX, endY));

        midX = startX;
        if (startX < endX) midX += Math.abs(startX - endX)/2;
        else midX -= Math.abs(startX - endX)/2;

        midY = startY;
        if (startY < endY) midY += Math.abs(startY - endY)/2;
        else midY -= Math.abs(startY - endY)/2;

        if (!showArrow) return;

        sin = Math.sin(angle - Math.PI / 2.0);
        cos = Math.cos(angle - Math.PI / 2.0);
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;

        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endX, endY));
    }
}
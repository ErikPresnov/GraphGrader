package com.example.graphgrader.Vana.Controllerid.BaasController;

import com.example.graphgrader.Vana.Graaf.Graaf;
import com.example.graphgrader.Uus.Graaf.Kaar;
import com.example.graphgrader.Uus.Graaf.Tipp;
import com.example.graphgrader.Vana.Util.TipuSobivus;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public abstract class Controller {
    public Pane graafiElement;
    public Graaf g;
    @FXML
    public TippController TippController;
    @FXML
    public KaarController KaarController;
    public abstract void lisa(Kaar k);
    public abstract void vota();
    public abstract TipuSobivus kontrolli(Tipp t);
    public abstract void joonistaTabel();

    public abstract void tee(Tipp lopp);

}

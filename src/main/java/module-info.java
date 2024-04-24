module com.example.graphgrader {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.graphgrader;
    opens com.example.graphgrader.Vana.Controllerid;
    opens com.example.graphgrader.Vana.Controllerid.BaasController;
}
module com.example.graphgrader {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.graphgrader;
    opens com.example.graphgrader.Algoritm;
    opens com.example.graphgrader.Algoritm.BaasController;
}
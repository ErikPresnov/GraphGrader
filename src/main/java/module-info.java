module com.example.graphgrader {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.graphgrader;
    opens com.example.graphgrader.Kontrollerid;
    opens com.example.graphgrader.Graaf;
    opens com.example.graphgrader.Util;
}
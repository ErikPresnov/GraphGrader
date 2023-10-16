module com.example.graphgrader {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.graphgrader to javafx.fxml;
    exports com.example.graphgrader;
    exports com.example.graphgrader.Graaf;
    opens com.example.graphgrader.Graaf to javafx.fxml;
}
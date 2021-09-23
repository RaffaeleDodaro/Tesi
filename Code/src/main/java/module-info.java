module com.example.code {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jbibtex;
    requires java.sql;


    opens com.tesi.code to javafx.fxml;
    exports com.tesi.code;
    exports com.tesi.code.Controller;
    opens com.tesi.code.Controller to javafx.fxml;
    exports com.tesi.code.Parser;
    opens com.tesi.code.Parser to javafx.fxml;
    exports com.tesi.code.Model;
    opens com.tesi.code.Model to javafx.fxml;
}
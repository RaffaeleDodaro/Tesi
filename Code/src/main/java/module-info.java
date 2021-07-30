module com.example.code {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jbibtex;


    opens com.tesi.code to javafx.fxml;
    exports com.tesi.code;
}
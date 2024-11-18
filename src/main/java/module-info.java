module com.gestion.projet {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires kernel;
    requires layout;


    opens com.gestion.projet to javafx.fxml;
    exports com.gestion.projet;


}
module com.proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.net.http;

    opens com.proyecto to javafx.fxml;
    opens com.proyecto.controllador to javafx.fxml;
    opens com.proyecto.modelo to javafx.fxml;
    opens com.proyecto.servicio to javafx.fxml;

    exports com.proyecto;
    exports com.proyecto.controllador;
    exports com.proyecto.modelo;
    exports com.proyecto.servicio;
}
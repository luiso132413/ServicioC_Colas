package com.proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Stage primaryStage;
    private Stage ticketsStage;
    private Stage cuentaStage;
    private static App instance;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        instance = this;

        cargarUsuario();
        cargarVentanaTickets();
    }

    public static App getInstance() {
        return instance;
    }

    private void cargarUsuario() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/CrearUsuario.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Sistema Servicio Cliente");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cargarVentanaTickets() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/tickets.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        ticketsStage = new Stage();
        ticketsStage.setTitle("Panel de Tickets");
        ticketsStage.setScene(scene);

        ticketsStage.setX(primaryStage.getX() + primaryStage.getWidth() + 20);
        ticketsStage.setY(primaryStage.getY());
        ticketsStage.show();
    }

    public void cargarVentanaCuenta() throws IOException {
        if (cuentaStage != null && cuentaStage.isShowing()) {
            cuentaStage.toFront();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/CrearCuenta.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        cuentaStage = new Stage();
        cuentaStage.setTitle("Crear Nueva Cuenta");
        cuentaStage.setScene(scene);

        cuentaStage.setX(primaryStage.getX() + primaryStage.getWidth() + 20);
        cuentaStage.setY(primaryStage.getY());
        cuentaStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
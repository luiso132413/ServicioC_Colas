package com.proyecto.controllador;

import com.proyecto.servicio.CuentaServicio;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

public class CuentaController {
    @FXML private TextField txtidentificacion;
    @FXML private ComboBox<String> cbTipoCuenta;

    private final CuentaServicio cuentaServicio = new CuentaServicio();

    @FXML
    public void initialize() {
        cbTipoCuenta.getItems().addAll("Monetaria", "Ahorros");
    }

    @FXML
    public void crearCuentaButton() {
        try {
            int identificacion = Integer.parseInt(txtidentificacion.getText());
            String tipoCuenta = cbTipoCuenta.getValue();

            if (tipoCuenta == null || tipoCuenta.isEmpty()) {
                mostrarAlerta("Debe seleccionar un tipo de cuenta.");
                return;
            }

            boolean creada = cuentaServicio.crearCuenta(identificacion, tipoCuenta);

            if (creada) {
                mostrarAlerta("Cuenta creada exitosamente.");
            } else {
                mostrarAlerta("No se pudo crear la cuenta.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("La identificación debe ser un número entero válido.");
        } catch (IOException e) {
            mostrarAlerta("Error al comunicarse con el servidor: " + e.getMessage());
        }
    }

    @FXML
    public void suspenderCuentaBt() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Gestión de Cuenta");
        dialog.setHeaderText("Ingrese el número de cuenta y seleccione una acción");

        ButtonType activarButtonType = new ButtonType("Activar", ButtonBar.ButtonData.OK_DONE);
        ButtonType desactivarButtonType = new ButtonType("Desactivar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(activarButtonType, desactivarButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNumeroCuenta = new TextField();
        txtNumeroCuenta.setPromptText("Número de cuenta (ej: 123456)");
        grid.add(new Label("Número de cuenta:"), 0, 0);
        grid.add(txtNumeroCuenta, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType != cancelButtonType) {
                try {
                    int numeroCuenta = Integer.parseInt(txtNumeroCuenta.getText());
                    boolean activar = buttonType == activarButtonType;

                    boolean exito = cuentaServicio.suspenderCuenta(numeroCuenta, !activar);

                    if (exito) {
                        mostrarAlerta("Cuenta " + (activar ? "activada" : "desactivada") + " correctamente.");
                    } else {
                        mostrarAlerta("No se pudo " + (activar ? "activar" : "desactivar") + " la cuenta.");
                    }
                } catch (NumberFormatException e) {
                    mostrarAlerta("El número de cuenta debe ser un valor numérico válido.");
                } catch (IOException e) {
                    mostrarAlerta("Error al comunicarse con el servidor: " + e.getMessage());
                }
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
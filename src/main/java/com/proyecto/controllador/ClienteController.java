package com.proyecto.controllador;

import com.proyecto.App;
import com.proyecto.modelo.Cliente;
import com.proyecto.servicio.ClienteServicio;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class ClienteController {
    @FXML private TextField txtnombre;
    @FXML private TextField txtapellido;
    @FXML private TextField txtidentificacion;
    @FXML private TextField txtcorreo;
    @FXML private TextField txttelefono;
    @FXML private TextField txtdireccion;

    private final ClienteServicio clienteServicio = new ClienteServicio();
    private Cliente clienteActual = null;

    @FXML
    private void onGuardarUsuario() {
        try {
            validarCampos();
            Cliente cliente = crearClienteDesdeFormulario();

            if (clienteActual == null) {
                clienteServicio.crearCliente(cliente);
                mostrarMensaje("Éxito", "Cliente creado exitosamente", AlertType.INFORMATION);
            } else {
                clienteServicio.actualizarCliente(cliente);
                mostrarMensaje("Éxito", "Cliente actualizado exitosamente", AlertType.INFORMATION);
            }

            limpiarCampos();
            clienteActual = null;
        } catch (Exception e) {
            manejarError(e);
        }
    }

    @FXML
    private void onBuscarUsuario() {
        try {
            validarCampoIdentificacion();
            int identificacion = Integer.parseInt(txtidentificacion.getText().trim());

            Cliente cliente = clienteServicio.buscarCliente(identificacion);

            if (cliente == null) {
                mostrarMensaje("No encontrado", "No existe cliente con la identificación " + identificacion, AlertType.INFORMATION);
                limpiarCampos();
                return;
            }

            clienteActual = cliente;
            llenarFormulario(cliente);
            mostrarMensaje("Éxito", "Cliente encontrado exitosamente", AlertType.INFORMATION);

        } catch (IllegalArgumentException e) {
            mostrarMensaje("Error en datos", e.getMessage(), AlertType.ERROR);
        } catch (IOException e) {
            mostrarMensaje("Error al buscar", e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            mostrarMensaje("Error inesperado", "Ocurrió un error al buscar el cliente", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void crearCuentaButton() {
        try {
            App.getInstance().cargarVentanaCuenta();
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudo abrir la ventana de crear cuenta", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private Cliente crearClienteDesdeFormulario() throws NumberFormatException {
        return new Cliente(
                Integer.parseInt(txtidentificacion.getText()),
                txtnombre.getText(),
                txtapellido.getText(),
                txtcorreo.getText(),
                txttelefono.getText(),
                txtdireccion.getText()
        );
    }

    private void llenarFormulario(Cliente cliente) {
        txtidentificacion.setText(String.valueOf(cliente.getIdentificacion()));
        txtnombre.setText(cliente.getNombre());
        txtapellido.setText(cliente.getApellido());
        txtcorreo.setText(cliente.getEmail());
        txttelefono.setText(cliente.getTelefono());
        txtdireccion.setText(cliente.getDireccion());
        txtidentificacion.setDisable(true);
    }

    private void validarCampos() throws IllegalArgumentException {
        validarCampoIdentificacion();

        if (txtnombre.getText().isEmpty() || txtapellido.getText().isEmpty()) {
            throw new IllegalArgumentException("Nombre y apellido son obligatorios");
        }

        if (txttelefono.getText().isEmpty()) {
            throw new IllegalArgumentException("Teléfono es obligatorio");
        }
    }

    private void validarCampoIdentificacion() throws IllegalArgumentException {
        if (txtidentificacion.getText().isEmpty()) {
            throw new IllegalArgumentException("Identificación es obligatoria");
        }

        try {
            Integer.parseInt(txtidentificacion.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Identificación debe ser un número válido");
        }
    }

    private void limpiarCampos() {
        txtnombre.clear();
        txtapellido.clear();
        txtidentificacion.clear();
        txtcorreo.clear();
        txttelefono.clear();
        txtdireccion.clear();
    }

    private void manejarError(Exception e) {
        String mensaje = e instanceof NumberFormatException
                ? "Error en datos: Identificación debe ser un número válido"
                : e instanceof IllegalArgumentException
                ? e.getMessage()
                : "Error: " + e.getMessage();

        mostrarMensaje("Error", mensaje, AlertType.ERROR);
    }

    private void mostrarMensaje(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
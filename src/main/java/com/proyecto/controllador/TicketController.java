    package com.proyecto.controllador;

    import com.proyecto.servicio.TicketServicio;
    import javafx.fxml.FXML;
    import javafx.scene.control.Alert;
    import javafx.scene.control.TextArea;
    import javafx.scene.control.Alert.AlertType;

    public class TicketController {
        @FXML private TextArea txtTicket;

        private TicketServicio ticketServicio = new TicketServicio();

        @FXML
        private void obtenerProximoTicket(){
            String ticket = ticketServicio.obtenerProximoTicket();
            if (ticket != null) {
                txtTicket.setText("Ticket atendido:\n" + ticket + "\n\nProceso completado.");
                mostrarMensaje("Atención completada", "Ticket " + ticket + " atendido con éxito", Alert.AlertType.INFORMATION);
            } else {
                txtTicket.setText("No hay tickets pendientes para atender.");
            }
        }

        @FXML
        private void finalizarOperacion(){
            boolean resultado = ticketServicio.finalizarOperacion();
            if (resultado) {
                txtTicket.setText("Operación finalizada con éxito.");
                mostrarMensaje("Operación completada", "La operación ha sido finalizada", Alert.AlertType.INFORMATION);
            } else {
                txtTicket.setText("No hay operación para finalizar.");
            }
        }

        private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo) {
            Alert alert = new Alert(tipo);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        }
    }
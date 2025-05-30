package com.proyecto.servicio;

import java.io.*;
import java.net.*;

public class TicketServicio {

    private static final String SERVER_HOST = "192.168.40.61";
    private static final int SERVER_PORT = 54321;

    public String obtenerProximoTicket() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("OBTENER_PROXIMO_SERVICIO");

            String respuesta = in.readLine();

            if (respuesta != null && respuesta.startsWith("PROXIMO_TICKET|")) {
                return respuesta.substring("PROXIMO_TICKET|".length());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean finalizarOperacion() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("FINALIZAR_OPERACION");
            String respuesta = in.readLine();

            return respuesta != null && respuesta.equals("OPERACION_FINALIZADA");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
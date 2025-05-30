package com.proyecto.servicio;

import com.proyecto.modelo.Cuenta;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CuentaServicio {

    private static final String API_BASE_URL = "https://fbanco.onrender.com/api/cuenta";
    private final HttpClient httpClient;

    public CuentaServicio() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public boolean crearCuenta(int identificacion, String tipoCuenta) throws IOException {
        Cuenta cuenta = new Cuenta();
        cuenta.setIdentificacion(identificacion);
        cuenta.setTipocuenta(tipoCuenta);

        try {
            enviarCuenta(cuenta, "/create");
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear cuenta: " + e.getMessage());
            return false;
        }
    }

    public boolean suspenderCuenta(int identificacion, boolean b) throws IOException {
        Cuenta cuenta = new Cuenta();
        cuenta.setIdentificacion(identificacion);

        try {
            suspensionCuenta(cuenta, "/suspender");
            return true;
        } catch (IOException e) {
            System.err.println("Error al suspender cuenta: " + e.getMessage());
            return false;
        }
    }

    private void enviarCuenta(Cuenta cuenta, String endpoint) throws IOException {
        try {
            JSONObject jcuenta = new JSONObject();
            jcuenta.put("identificacion", cuenta.getIdentificacion());
            jcuenta.put("tipo_cuenta", cuenta.getTipocuenta());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jcuenta.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() >= 400) {
                throw new IOException("Error en la API: " + response.body());
            }

            String tipoOperacion = endpoint.equals("/create") ? "Creación" : "Actualización";
            System.out.println(tipoOperacion + " de la cuenta exitosa: " + response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Operación interrumpida", e);
        } catch (Exception e) {
            throw new IOException("Error al comunicarse con el servidor: " + e.getMessage(), e);
        }
    }

    private void suspensionCuenta(Cuenta cuenta, String endpoint) throws IOException {
        try {
            JSONObject jcuenta = new JSONObject();
            jcuenta.put("numero_cuenta", cuenta.getIdentificacion());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jcuenta.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 404) {
                throw new IOException("Cuenta no encontrada");
            } else if (response.statusCode() >= 400) {
                throw new IOException("Error al suspender cuenta: " + response.body());
            }

            System.out.println("Operación de cuenta exitosa: " + response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Operación interrumpida", e);
        } catch (Exception e) {
            throw new IOException("Error al comunicarse con el servidor: " + e.getMessage(), e);
        }
    }
}
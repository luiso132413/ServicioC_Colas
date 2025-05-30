package com.proyecto.servicio;

import com.proyecto.modelo.Cliente;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ClienteServicio {
    private static final String API_BASE_URL = "https://fbanco.onrender.com/api/cliente";
    private final HttpClient httpClient;

    public ClienteServicio() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public void crearCliente(Cliente cliente) throws IOException {
        enviarCliente(cliente, "/create");
    }

    public void actualizarCliente(Cliente cliente) throws IOException {
        ClienteActualizado(cliente, "/actualizar");
    }

    public Cliente buscarCliente(int identificacion) throws IOException {
        return obtenerCliente(identificacion);
    }

    private void enviarCliente(Cliente cliente, String endpoint) throws IOException {
        try {
            JSONObject jcliente = new JSONObject();
            jcliente.put("identificacion", cliente.getIdentificacion());
            jcliente.put("nombre", cliente.getNombre());
            jcliente.put("apellido", cliente.getApellido());
            jcliente.put("email", cliente.getEmail());
            jcliente.put("telefono", cliente.getTelefono());
            jcliente.put("direccion", cliente.getDireccion());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jcliente.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() >= 400) {
                throw new IOException("Error en la API: " + response.body());
            }

            System.out.println("Creación del cliente exitosa: " + response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Operación interrumpida", e);
        } catch (Exception e) {
            throw new IOException("Error al comunicarse con el servidor: " + e.getMessage(), e);
        }
    }

    private Cliente obtenerCliente(int identificacion) throws IOException {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("identificacion", identificacion);

            System.out.println(">>> Enviando petición de búsqueda:");
            System.out.println("URL: " + API_BASE_URL + "/buscar");
            System.out.println("Body: " + requestBody.toString(2));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/buscar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("<<< Respuesta recibida:");
            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());

            if (response.statusCode() == 404) {
                return null;
            }

            if (response.statusCode() >= 400) {
                throw new IOException("Error HTTP " + response.statusCode() + ": " + response.body());
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            return mapearJsonACliente(jsonResponse);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Búsqueda interrumpida", e);
        }
    }

    private Cliente mapearJsonACliente(JSONObject json) throws IOException {
        try {
            if (!json.optBoolean("success", false)) {
                String errorMsg = json.optString("message", "Operación no exitosa en el servidor");
                throw new IOException(errorMsg);
            }

            if (!json.has("data")) {
                throw new IOException("La respuesta no contiene los datos del cliente");
            }

            JSONObject data = json.getJSONObject("data");

            if (!data.has("identificacion")) {
                throw new IOException("La respuesta no contiene el campo 'identificacion'");
            }

            return new Cliente(
                    data.getInt("identificacion"),
                    data.optString("nombre", ""),
                    data.optString("apellido", ""),
                    data.optString("email", ""),
                    data.optString("telefono", ""),
                    data.optString("direccion", "")
            );
        } catch (JSONException e) {
            throw new IOException("Error al procesar la respuesta JSON: " + e.getMessage(), e);
        }
    }

    public void ClienteActualizado(Cliente cliente, String endpoint) throws IOException {
        try {
            Cliente clienteExistente = buscarCliente(cliente.getIdentificacion());
            if (clienteExistente == null) {
                throw new IOException("No se puede actualizar: el cliente con identificación " +
                        cliente.getIdentificacion() + " no existe");
            }

            JSONObject jcliente = new JSONObject();
            jcliente.put("identificacion", cliente.getIdentificacion());
            jcliente.put("nombre", cliente.getNombre());
            jcliente.put("apellido", cliente.getApellido());
            jcliente.put("email", cliente.getEmail());
            jcliente.put("telefono", cliente.getTelefono());
            jcliente.put("direccion", cliente.getDireccion());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jcliente.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 404) {
                throw new IOException("Cliente no encontrado para actualizar");
            } else if (response.statusCode() >= 400) {
                throw new IOException("Error al actualizar cliente: " + response.body());
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            if (!jsonResponse.optBoolean("success", false)) {
                throw new IOException("Error en la actualización: " +
                        jsonResponse.optString("message", "Error desconocido"));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Actualización interrumpida", e);
        } catch (JSONException e) {
            throw new IOException("Error al procesar los datos JSON", e);
        }
    }
}
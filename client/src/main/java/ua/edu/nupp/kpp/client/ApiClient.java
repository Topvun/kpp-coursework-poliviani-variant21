package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.client.model.Client;
import ua.edu.nupp.kpp.client.model.Courier;
import ua.edu.nupp.kpp.client.model.DeliveryOrder;
import ua.edu.nupp.kpp.client.model.Shipment;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/courier-delivery-server";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }


    private String enc(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET()
                .build();
        return send(request);
    }

    private String post(String endpoint, Map<String, String> params) throws IOException, InterruptedException {
        StringBuilder body = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) body.append("&");
            body.append(enc(entry.getKey())).append("=").append(enc(entry.getValue()));
            first = false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();
        return send(request);
    }

    private String send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() >= 400) {
            throw new IOException("Помилка сервера (HTTP " + response.statusCode() + "):\n" + response.body());
        }
        return response.body();
    }

    // ================= 1. CLIENTS (Клієнти) =================

    public List<Client> getClients() throws IOException, InterruptedException {
        return mapper.readValue(get("/clients"), new TypeReference<List<Client>>() {});
    }

    public void addClient(Client c) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "add");
        params.put("name", c.getName());
        params.put("phone", c.getPhone());
        params.put("address", c.getAddress());
        post("/clients", params);
    }

    public void updateClient(Client c) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "update");
        params.put("id", String.valueOf(c.getId()));
        params.put("name", c.getName());
        params.put("phone", c.getPhone());
        params.put("address", c.getAddress());
        post("/clients", params);
    }

    public void deleteClient(Long id) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "delete");
        params.put("id", String.valueOf(id));
        post("/clients", params);
    }

    // ================= 2. COURIERS (Кур'єри) =================

    public List<Courier> getCouriers() throws IOException, InterruptedException {
        return mapper.readValue(get("/couriers"), new TypeReference<List<Courier>>() {});
    }

    public void addCourier(Courier c) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "add");
        params.put("name", c.getName());
        params.put("phone", c.getPhone());
        params.put("transportType", c.getTransportType());
        params.put("status", c.getStatus());
        post("/couriers", params);
    }

    public void updateCourier(Courier c) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "update");
        params.put("id", String.valueOf(c.getId()));
        params.put("name", c.getName());
        params.put("phone", c.getPhone());
        params.put("transportType", c.getTransportType());
        params.put("status", c.getStatus());
        post("/couriers", params);
    }

    public void deleteCourier(Long id) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "delete");
        params.put("id", String.valueOf(id));
        post("/couriers", params);
    }

    // ================= 3. SHIPMENTS (Відправлення) =================

    public List<Shipment> getShipments() throws IOException, InterruptedException {
        return mapper.readValue(get("/shipments"), new TypeReference<List<Shipment>>() {});
    }

    public void addShipment(Shipment s) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "add");
        params.put("trackingNumber", s.getTrackingNumber());
        params.put("description", s.getDescription());
        params.put("weight", String.valueOf(s.getWeight()));
        params.put("destinationAddress", s.getDestinationAddress());
        post("/shipments", params);
    }

    public void updateShipment(Shipment s) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "update");
        params.put("id", String.valueOf(s.getId()));
        params.put("trackingNumber", s.getTrackingNumber());
        params.put("description", s.getDescription());
        params.put("weight", String.valueOf(s.getWeight()));
        params.put("destinationAddress", s.getDestinationAddress());
        post("/shipments", params);
    }

    public void deleteShipment(Long id) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "delete");
        params.put("id", String.valueOf(id));
        post("/shipments", params);
    }

    // ================= 4. ORDERS (Замовлення) =================

    public List<DeliveryOrder> getOrders() throws IOException, InterruptedException {
        return mapper.readValue(get("/orders"), new TypeReference<List<DeliveryOrder>>() {});
    }

    public void addOrder(DeliveryOrder o) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "add");
        params.put("clientId", String.valueOf(o.getClientId()));
        params.put("courierId", String.valueOf(o.getCourierId()));
        params.put("shipmentId", String.valueOf(o.getShipmentId()));
        params.put("orderDate", o.getOrderDate());
        params.put("status", o.getStatus());
        params.put("deliveryCost", String.valueOf(o.getDeliveryCost()));
        post("/orders", params);
    }

    public void updateOrder(DeliveryOrder o) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "update");
        params.put("id", String.valueOf(o.getId()));
        params.put("clientId", String.valueOf(o.getClientId()));
        params.put("courierId", String.valueOf(o.getCourierId()));
        params.put("shipmentId", String.valueOf(o.getShipmentId()));
        params.put("orderDate", o.getOrderDate());
        params.put("status", o.getStatus());
        params.put("deliveryCost", String.valueOf(o.getDeliveryCost()));
        post("/orders", params);
    }

    public void deleteOrder(Long id) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "delete");
        params.put("id", String.valueOf(id));
        post("/orders", params);
    }

    // Предметна операція зміни стану замовлення (Пункт 7.10)
    public void changeOrderStatus(Long id, String newStatus) throws IOException, InterruptedException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action", "transition");
        params.put("id", String.valueOf(id));
        params.put("status", newStatus);
        post("/orders", params);
    }
}
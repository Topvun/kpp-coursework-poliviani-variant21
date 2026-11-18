package com.example.courierdeliveryclient.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.courierdeliveryclient.model.Client;
import com.example.courierdeliveryclient.model.Courier;
import com.example.courierdeliveryclient.model.Shipment;
import com.example.courierdeliveryclient.model.DeliveryOrder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/courier-delivery-server";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = new ObjectMapper();
    }

    // Завантаження списку клієнтів
    public List<Client> getClients() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/clients"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<List<Client>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Завантаження списку кур'єрів
    public List<Courier> getCouriers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/couriers"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<List<Courier>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Завантаження списку відправлень
    public List<Shipment> getShipments() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/shipments"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<List<Shipment>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Завантаження списку замовлень
    public List<DeliveryOrder> getOrders() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/orders"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<List<DeliveryOrder>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
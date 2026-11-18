package ua.edu.nupp.kpp.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrder {
    private Long id;
    private Long clientId;
    private Long courierId;
    private Long shipmentId;
    private double deliveryCost;

    @JsonProperty("orderStatus")
    private String status;

    private String orderDate; // або LocalDate, залежно від вашої реалізації

    public DeliveryOrder() {}

    public DeliveryOrder(Long id, Long clientId, Long courierId, Long shipmentId, double deliveryCost, String status, String orderDate) {
        this.id = id;
        this.clientId = clientId;
        this.courierId = courierId;
        this.shipmentId = shipmentId;
        this.deliveryCost = deliveryCost;
        this.status = status;
        this.orderDate = orderDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }

    public double getDeliveryCost() { return deliveryCost; }
    public void setDeliveryCost(double deliveryCost) { this.deliveryCost = deliveryCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
}
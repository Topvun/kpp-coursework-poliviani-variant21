package com.example.courierdeliveryclient.model;

public class DeliveryOrder {
    private Long id;
    private Long clientId;
    private Long courierId;
    private Long shipmentId;
    private String orderStatus;
    private double f1;
    private double f2;
    private double f3;
    private double deliveryScore;

    // Геттери та сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public double getF1() { return f1; }
    public void setF1(double f1) { this.f1 = f1; }

    public double getF2() { return f2; }
    public void setF2(double f2) { this.f2 = f2; }

    public double getF3() { return f3; }
    public void setF3(double f3) { this.f3 = f3; }

    public double getDeliveryScore() { return deliveryScore; }
    public void setDeliveryScore(double deliveryScore) { this.deliveryScore = deliveryScore; }
}
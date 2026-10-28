package ua.edu.nupp.kpp.server;

public class DeliveryOrder {
    private Long id;
    private Long clientId;
    private Long courierId;
    private Long shipmentId;
    private OrderStatus orderStatus;
    private double f1;
    private double f2;
    private double f3;
    private double deliveryScore;

    public DeliveryOrder() {}

    public DeliveryOrder(Long id, Long clientId, Long courierId, Long shipmentId, OrderStatus orderStatus, double f1, double f2, double f3, double deliveryScore) {
        this.id = id;
        this.clientId = clientId;
        this.courierId = courierId;
        this.shipmentId = shipmentId;
        this.orderStatus = orderStatus;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.deliveryScore = deliveryScore;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }

    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    public double getF1() { return f1; }
    public void setF1(double f1) { this.f1 = f1; }

    public double getF2() { return f2; }
    public void setF2(double f2) { this.f2 = f2; }

    public double getF3() { return f3; }
    public void setF3(double f3) { this.f3 = f3; }

    public double getDeliveryScore() { return deliveryScore; }
    public void setDeliveryScore(double deliveryScore) { this.deliveryScore = deliveryScore; }
}
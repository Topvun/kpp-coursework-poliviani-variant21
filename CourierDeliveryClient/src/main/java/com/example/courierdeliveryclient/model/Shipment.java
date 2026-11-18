package com.example.courierdeliveryclient.model;

public class Shipment {
    private Long id;
    private String description;
    private double weight;
    private double serviceLevel;
    private double sensitivity;

    // Геттери та сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getServiceLevel() { return serviceLevel; }
    public void setServiceLevel(double serviceLevel) { this.serviceLevel = serviceLevel; }

    public double getSensitivity() { return sensitivity; }
    public void setSensitivity(double sensitivity) { this.sensitivity = sensitivity; }
}
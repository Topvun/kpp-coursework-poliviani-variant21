package ua.edu.nupp.kpp.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shipment {
    private Long id;
    private String trackingNumber;
    private String description;
    private double weight;
    private String destinationAddress;

    public Shipment() {}

    public Shipment(Long id, String trackingNumber, String description, double weight, String destinationAddress) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.description = description;
        this.weight = weight;
        this.destinationAddress = destinationAddress;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
}
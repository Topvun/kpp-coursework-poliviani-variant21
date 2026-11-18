package ua.edu.nupp.kpp.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Courier {
    private Long id;
    private String name;
    private String phone;

    @JsonProperty("vehicleType")
    private String transportType;

    private String status;

    public Courier() {}

    public Courier(Long id, String name, String phone, String transportType, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.transportType = transportType;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
package ua.edu.nupp.kpp.server;

public class Courier {
    private Long id;
    private String name;
    private String phone;
    private String vehicleType;

    public Courier() {}

    public Courier(Long id, String name, String phone, String vehicleType) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.vehicleType = vehicleType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}
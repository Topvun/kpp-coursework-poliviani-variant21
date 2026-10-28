package ua.edu.nupp.kpp.server;

public class Client {
    private Long id;
    private String full_name;
    private String phone;
    private String address;

    public Client() {}

    public Client(Long id, String full_name, String phone, String address) {
        this.id = id;
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return full_name; }
    public void setName(String name) { this.full_name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
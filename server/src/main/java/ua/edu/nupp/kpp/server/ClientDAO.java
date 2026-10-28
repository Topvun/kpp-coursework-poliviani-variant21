package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // Створення (Create)
    public void add(Client client) throws SQLException {
        String sql = "INSERT INTO client (full_name, phone, address) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getPhone());
            pstmt.setString(3, client.getAddress());
            pstmt.executeUpdate();
        }
    }

    // Отримання всіх записів (Read All)
    public List<Client> getAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getLong("id"),
                        rs.getString("full_name"), // Використовуємо правильну назву стовпця з БД
                        rs.getString("phone"),
                        rs.getString("address")
                );
                clients.add(client);
            }
        }
        return clients;
    }

    // Отримання за ID (Read by ID)
    public Client getById(long id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getLong("id"),
                            rs.getString("full_name"), // Використовуємо правильну назву стовпця з БД
                            rs.getString("phone"),
                            rs.getString("address")
                    );
                }
            }
        }
        return null;
    }

    // Оновлення (Update)
    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET full_name = ?, phone = ?, address = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getPhone());
            pstmt.setString(3, client.getAddress());
            pstmt.setLong(4, client.getId());
            pstmt.executeUpdate();
        }
    }

    // Видалення (Delete)
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
}
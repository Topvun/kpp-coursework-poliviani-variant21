package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {

    public void add(Shipment shipment) throws SQLException {
        String sql = "INSERT INTO shipment (description, weight, service_level, sensitivity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shipment.getDescription());
            pstmt.setDouble(2, shipment.getWeight());
            pstmt.setDouble(3, shipment.getServiceLevel());
            pstmt.setDouble(4, shipment.getSensitivity());
            pstmt.executeUpdate();
        }
    }

    public List<Shipment> getAll() throws SQLException {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM shipment";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Shipment shipment = new Shipment(
                        rs.getLong("id"),
                        rs.getString("description"),
                        rs.getDouble("weight"),
                        rs.getDouble("service_level"),
                        rs.getDouble("sensitivity")
                );
                shipments.add(shipment);
            }
        }
        return shipments;
    }

    public Shipment getById(long id) throws SQLException {
        String sql = "SELECT * FROM shipment WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Shipment(
                            rs.getLong("id"),
                            rs.getString("description"),
                            rs.getDouble("weight"),
                            rs.getDouble("service_level"),
                            rs.getDouble("sensitivity")
                    );
                }
            }
        }
        return null;
    }

    public void update(Shipment shipment) throws SQLException {
        String sql = "UPDATE shipment SET description = ?, weight = ?, service_level = ?, sensitivity = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shipment.getDescription());
            pstmt.setDouble(2, shipment.getWeight());
            pstmt.setDouble(3, shipment.getServiceLevel());
            pstmt.setDouble(4, shipment.getSensitivity());
            pstmt.setLong(5, shipment.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM shipment WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
}
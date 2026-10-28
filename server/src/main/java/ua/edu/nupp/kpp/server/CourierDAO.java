package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourierDAO {

    public void add(Courier courier) throws SQLException {
        String sql = "INSERT INTO courier (full_name, phone, vehicle_type) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courier.getName());
            pstmt.setString(2, courier.getPhone());
            pstmt.setString(3, courier.getVehicleType());
            pstmt.executeUpdate();
        }
    }

    public List<Courier> getAll() throws SQLException {
        List<Courier> couriers = new ArrayList<>();
        String sql = "SELECT * FROM courier";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Courier courier = new Courier(
                        rs.getLong("id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("vehicle_type")
                );
                couriers.add(courier);
            }
        }
        return couriers;
    }

    public Courier getById(long id) throws SQLException {
        String sql = "SELECT * FROM courier WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Courier(
                            rs.getLong("id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("vehicle_type")
                    );
                }
            }
        }
        return null;
    }

    public void update(Courier courier) throws SQLException {
        String sql = "UPDATE courier SET full_name = ?, phone = ?, vehicle_type = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courier.getName());
            pstmt.setString(2, courier.getPhone());
            pstmt.setString(3, courier.getVehicleType());
            pstmt.setLong(4, courier.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM courier WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
}
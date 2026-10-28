package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryOrderDAO {

    public void add(DeliveryOrder order) throws SQLException {
        String sql = "INSERT INTO delivery_order (client_id, courier_id, shipment_id, state, f1, f2, f3, integral_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, order.getClientId());
            pstmt.setLong(2, order.getCourierId());
            pstmt.setLong(3, order.getShipmentId());
            pstmt.setString(4, order.getOrderStatus().name());
            pstmt.setDouble(5, order.getF1());
            pstmt.setDouble(6, order.getF2());
            pstmt.setDouble(7, order.getF3());
            pstmt.setDouble(8, order.getDeliveryScore());
            pstmt.executeUpdate();
        }
    }

    public List<DeliveryOrder> getAll() throws SQLException {
        List<DeliveryOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM delivery_order";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DeliveryOrder order = new DeliveryOrder(
                        rs.getLong("id"),
                        rs.getLong("client_id"),
                        rs.getLong("courier_id"),
                        rs.getLong("shipment_id"),
                        OrderStatus.valueOf(rs.getString("state")),
                        rs.getDouble("f1"),
                        rs.getDouble("f2"),
                        rs.getDouble("f3"),
                        rs.getDouble("integral_score")
                );
                orders.add(order);
            }
        }
        return orders;
    }

    public DeliveryOrder getById(long id) throws SQLException {
        String sql = "SELECT * FROM delivery_order WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DeliveryOrder(
                            rs.getLong("id"),
                            rs.getLong("client_id"),
                            rs.getLong("courier_id"),
                            rs.getLong("shipment_id"),
                            OrderStatus.valueOf(rs.getString("state")),
                            rs.getDouble("f1"),
                            rs.getDouble("f2"),
                            rs.getDouble("f3"),
                            rs.getDouble("integral_score")
                    );
                }
            }
        }
        return null;
    }

    public void update(DeliveryOrder order) throws SQLException {
        String sql = "UPDATE delivery_order SET client_id = ?, courier_id = ?, shipment_id = ?, state = ?, f1 = ?, f2 = ?, f3 = ?, integral_score = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, order.getClientId());
            pstmt.setLong(2, order.getCourierId());
            pstmt.setLong(3, order.getShipmentId());
            pstmt.setString(4, order.getOrderStatus().name());
            pstmt.setDouble(5, order.getF1());
            pstmt.setDouble(6, order.getF2());
            pstmt.setDouble(7, order.getF3());
            pstmt.setDouble(8, order.getDeliveryScore());
            pstmt.setLong(9, order.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM delivery_order WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public void updateState(long id, OrderStatus status) throws SQLException {
        String sql = "UPDATE delivery_order SET state = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        }
    }
}
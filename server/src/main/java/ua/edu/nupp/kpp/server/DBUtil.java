package ua.edu.nupp.kpp.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // URL для підключення до нашої бази даних Варіанта №21
    private static final String URL = "jdbc:mysql://localhost:3306/delivery_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";     // Стандартний користувач XAMPP
    private static final String PASSWORD = "";     // За замовчуванням у XAMPP пароль порожній

    public static Connection getConnection() throws SQLException {
        try {
            // Завантаження драйвера MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver не знайдено!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
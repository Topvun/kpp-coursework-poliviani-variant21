package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CourierService {
    private static final Logger LOG = Logger.getLogger(CourierService.class.getName());
    private final CourierDAO dao = new CourierDAO();

    public List<Courier> getAll() throws SQLException {
        return dao.getAll();
    }

    public Courier getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Courier courier) throws SQLException {
        validate(courier);
        dao.add(courier);
        LOG.info("Додано нового кур'єра: " + courier.getName());
    }

    public void update(Courier courier) throws SQLException {
        validate(courier);
        dao.update(courier);
        LOG.info("Оновлено дані кур'єра з ID: " + courier.getId());
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено кур'єра з ID: " + id);
    }

    private void validate(Courier courier) {
        if (courier == null) {
            throw new IllegalArgumentException("Кур'єр не може бути null");
        }
        if (courier.getName() == null || courier.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ кур'єра є обов'язковим");
        }
        if (courier.getVehicleType() == null || courier.getVehicleType().trim().isEmpty()) {
            throw new IllegalArgumentException("Тип транспортного засобу є обов'язковим");
        }
    }
}
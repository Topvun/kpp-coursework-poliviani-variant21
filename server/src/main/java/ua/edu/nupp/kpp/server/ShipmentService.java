package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ShipmentService {
    private static final Logger LOG = Logger.getLogger(ShipmentService.class.getName());
    private final ShipmentDAO dao = new ShipmentDAO();

    public List<Shipment> getAll() throws SQLException {
        return dao.getAll();
    }

    public Shipment getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Shipment shipment) throws SQLException {
        validate(shipment);
        dao.add(shipment);
        LOG.info("Додано нове відправлення (посилку)");
    }

    public void update(Shipment shipment) throws SQLException {
        validate(shipment);
        dao.update(shipment);
        LOG.info("Оновлено відправлення з ID: " + shipment.getId());
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено відправлення з ID: " + id);
    }

    private void validate(Shipment shipment) {
        if (shipment == null) {
            throw new IllegalArgumentException("Посилка не може бути null");
        }
        if (shipment.getDescription() == null || shipment.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Опис посилки є обов'язковим");
        }
        if (shipment.getWeight() <= 0) {
            throw new IllegalArgumentException("Вага посилки має бути додатньою");
        }
        if (shipment.getServiceLevel() < 0 || shipment.getServiceLevel() > 1) {
            throw new IllegalArgumentException("Рівень обслуговування має бути в діапазоні від 0 до 1");
        }
        if (shipment.getSensitivity() < 0 || shipment.getSensitivity() > 1) {
            throw new IllegalArgumentException("Чуттливість має бути в діапазоні від 0 до 1");
        }
    }
}
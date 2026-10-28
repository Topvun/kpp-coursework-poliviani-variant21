package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class DeliveryOrderService {
    private static final Logger LOG = Logger.getLogger(DeliveryOrderService.class.getName());

    private final DeliveryOrderDAO dao = new DeliveryOrderDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final CourierDAO courierDAO = new CourierDAO();
    private final ShipmentDAO shipmentDAO = new ShipmentDAO();

    public List<DeliveryOrder> getAll() throws SQLException {
        return dao.getAll();
    }

    public DeliveryOrder getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(long clientId, long courierId, long shipmentId) throws SQLException {
        DeliveryOrder order = buildOrder(null, clientId, courierId, shipmentId, OrderStatus.NEW);
        dao.add(order);
        LOG.info("Створено нове кур'єрське замовлення у стані NEW");
    }

    public void update(long id, long clientId, long courierId, long shipmentId) throws SQLException {
        DeliveryOrder old = dao.getById(id);
        if (old == null) {
            throw new IllegalArgumentException("Замовлення з ID " + id + " не знайдено");
        }
        DeliveryOrder order = buildOrder(id, clientId, courierId, shipmentId, old.getOrderStatus());
        dao.update(order);
        LOG.info("Оновлено дані замовлення ID " + id);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено замовлення ID " + id);
    }

    // Метод перевірки допустимості переходу між станами життєвого циклу
    public static boolean isAllowedTransition(OrderStatus from, OrderStatus to) {
        if (from == to) {
            return false; // Повторний перехід у той самий стан заборонено
        }

        // Дозволені послідовні переходи
        if (from == OrderStatus.NEW && to == OrderStatus.ACCEPTED) return true;
        if (from == OrderStatus.ACCEPTED && to == OrderStatus.IN_TRANSIT) return true;
        if (from == OrderStatus.IN_TRANSIT && to == OrderStatus.DELIVERED) return true;

        // Можливість скасування з перших двох станів
        if ((from == OrderStatus.NEW || from == OrderStatus.ACCEPTED) && to == OrderStatus.CANCELLED) return true;

        return false;
    }

    // Безпечна зміна стану з перевіркою бізнес-правил
    public void changeState(long id, OrderStatus nextStatus) throws SQLException {
        if (nextStatus == null) {
            throw new IllegalArgumentException("Новий статус не може бути null");
        }

        DeliveryOrder current = dao.getById(id);
        if (current == null) {
            throw new IllegalArgumentException("Замовлення з ID " + id + " не знайдено");
        }

        if (!isAllowedTransition(current.getOrderStatus(), nextStatus)) {
            LOG.warning("Недопустимий перехід стану замовлення ID " + id + ": " + current.getOrderStatus() + " -> " + nextStatus);
            throw new IllegalArgumentException("Недопустимий перехід стану з " + current.getOrderStatus() + " у " + nextStatus);
        }

        dao.updateState(id, nextStatus);
        LOG.info("Змінено стан замовлення ID " + id + ": " + current.getOrderStatus() + " -> " + nextStatus);
    }

    private DeliveryOrder buildOrder(Long id, long clientId, long courierId, long shipmentId, OrderStatus status) throws SQLException {
        if (clientId <= 0 || courierId <= 0 || shipmentId <= 0) {
            throw new IllegalArgumentException("Некоректні зовнішні ідентифікатори зв'язків");
        }

        if (clientDAO.getById(clientId) == null) {
            throw new IllegalArgumentException("Клієнта не знайдено");
        }
        if (courierDAO.getById(courierId) == null) {
            throw new IllegalArgumentException("Кур'єра не знайдено");
        }
        Shipment shipment = shipmentDAO.getById(shipmentId);
        if (shipment == null) {
            throw new IllegalArgumentException("Посилку не знайдено");
        }

        double f1 = RiskCalculator.factor1(shipment.getWeight());
        double f2 = shipment.getServiceLevel() * 100.0;
        double f3 = shipment.getSensitivity() * 100.0;
        double integralScore = RiskCalculator.calculate(f1, f2, f3);

        return new DeliveryOrder(id, clientId, courierId, shipmentId, status, f1, f2, f3, integralScore);
    }
}
package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ClientService {
    private static final Logger LOG = Logger.getLogger(ClientService.class.getName());
    private final ClientDAO dao = new ClientDAO();

    public List<Client> getAll() throws SQLException {
        return dao.getAll();
    }

    public Client getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Client client) throws SQLException {
        validate(client);
        dao.add(client);
        LOG.info("Додано нового клієнта: " + client.getName());
    }

    public void update(Client client) throws SQLException {
        validate(client);
        dao.update(client);
        LOG.info("Оновлено дані клієнта з ID: " + client.getId());
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено клієнта з ID: " + id);
    }

    private void validate(Client client) {
        if (client == null) {
            LOG.warning("спроба передати null-клієнта");
            throw new IllegalArgumentException("Клієнт не може бути null");
        }
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ клієнта є обов'язковим");
        }
        if (client.getPhone() == null || client.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон клієнта є обов'язковим");
        }
    }
}
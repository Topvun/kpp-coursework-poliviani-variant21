package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShipmentServlet extends HttpServlet {
    private final ShipmentService service = new ShipmentService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            mapper.writeValue(resp.getWriter(), service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                Shipment shipment = new Shipment(
                        null,
                        req.getParameter("description"),
                        Double.parseDouble(req.getParameter("weight")),
                        Double.parseDouble(req.getParameter("service_level")),
                        Double.parseDouble(req.getParameter("sensitivity"))
                );
                service.add(shipment);
                writeOk(resp);
            } else if ("update".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                Shipment shipment = new Shipment(
                        id,
                        req.getParameter("description"),
                        Double.parseDouble(req.getParameter("weight")),
                        Double.parseDouble(req.getParameter("service_level")),
                        Double.parseDouble(req.getParameter("sensitivity"))
                );
                service.update(shipment);
                writeOk(resp);
            } else if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                service.delete(id);
                writeOk(resp);
            } else {
                throw new IllegalArgumentException("Невідома дія: " + action);
            }
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    private void writeOk(HttpServletResponse resp) throws IOException {
        Map<String, String> res = new HashMap<>();
        res.put("status", "ok");
        mapper.writeValue(resp.getWriter(), res);
    }

    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        if (e instanceof IllegalArgumentException) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        Map<String, String> err = new HashMap<>();
        err.put("error", e.getMessage() != null ? e.getMessage() : "Помилка обробки запиту");
        mapper.writeValue(resp.getWriter(), err);
    }
}
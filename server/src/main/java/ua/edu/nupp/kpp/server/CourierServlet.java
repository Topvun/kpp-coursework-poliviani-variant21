package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CourierServlet extends HttpServlet {
    private final CourierService service = new CourierService();
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
                String name = firstNonNull(req.getParameter("name"), req.getParameter("full_name"));
                String phone = req.getParameter("phone");
                String transportType = firstNonNull(req.getParameter("transportType"), req.getParameter("vehicle_type"));
                String status = req.getParameter("status");

                Courier courier = buildCourier(null, name, phone, transportType, status);
                service.add(courier);
                writeOk(resp);
            } else if ("update".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                String name = firstNonNull(req.getParameter("name"), req.getParameter("full_name"));
                String phone = req.getParameter("phone");
                String transportType = firstNonNull(req.getParameter("transportType"), req.getParameter("vehicle_type"));
                String status = req.getParameter("status");

                Courier courier = buildCourier(id, name, phone, transportType, status);
                service.update(courier);
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

    private Courier buildCourier(Long id, String name, String phone, String transportType, String status) {
        try {
            return Courier.class
                    .getConstructor(Long.class, String.class, String.class, String.class, String.class)
                    .newInstance(id, name, phone, transportType, status);
        } catch (ReflectiveOperationException e) {
            Courier courier = new Courier(id, name, phone, transportType);
            if (status != null) {
                try {
                    Courier.class.getMethod("setStatus", String.class).invoke(courier, status);
                } catch (ReflectiveOperationException ignored) {
                }
            }
            return courier;
        }
    }

    private String firstNonNull(String primary, String fallback) {
        return primary != null ? primary : fallback;
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
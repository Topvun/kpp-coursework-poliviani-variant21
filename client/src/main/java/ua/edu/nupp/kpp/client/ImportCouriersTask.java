package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;
import ua.edu.nupp.kpp.client.model.Courier;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportCouriersTask extends Task<Integer> {
    private static final Logger LOG = Logger.getLogger(ImportCouriersTask.class.getName());
    private static final String EXPECTED_HEADER = "courier_id;name;phone;status";

    private final Path csvPath;
    private final ApiClient apiClient;

    public ImportCouriersTask(Path csvPath, ApiClient apiClient) {
        this.csvPath = csvPath;
        this.apiClient = apiClient;
    }

    @Override
    protected Integer call() throws Exception {
        updateMessage("Читання CSV-файлу...");
        if (!Files.exists(csvPath) || !Files.isReadable(csvPath)) {
            throw new IllegalArgumentException("Не вдалося прочитати CSV-файл: " + csvPath);
        }

        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        if (lines.isEmpty() || !EXPECTED_HEADER.equals(lines.get(0).trim())) {
            throw new IllegalArgumentException("Некоректний заголовок CSV-файлу");
        }

        List<String> dataLines = lines.stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .toList();

        int total = dataLines.size();
        if (total == 0) {
            updateProgress(1, 1);
            updateMessage("Немає рядків для імпорту");
            LOG.info("CSV-імпорт завершено: рядків для імпорту немає");
            return 0;
        }

        LOG.info("Початок CSV-імпорту з файлу " + csvPath.getFileName() + " (рядків: " + total + ")");
        updateProgress(0, total);

        int processed = 0;
        int imported = 0;

        for (int i = 0; i < dataLines.size(); i++) {
            if (isCancelled()) {
                break;
            }

            int lineNumber = i + 2;
            String[] fields = dataLines.get(i).split(";", -1);

            if (fields.length != 4) {
                LOG.warning("Рядок " + lineNumber + " пропущено: очікується 4 поля, отримано " + fields.length);
                processed++;
                updateProgress(processed, total);
                updateMessage("Опрацьовано " + processed + " із " + total + " (імпортовано: " + imported + ")");
                continue;
            }

            try {
                Long.parseLong(fields[0].trim());
                String name = fields[1].trim();
                String phone = fields[2].trim();
                String status = fields[3].trim();

                if (name.isEmpty() || phone.isEmpty() || status.isEmpty()) {
                    throw new IllegalArgumentException("Порожнє обов'язкове поле у рядку " + lineNumber);
                }

                Courier courier = new Courier();
                courier.setName(name);
                courier.setPhone(phone);
                courier.setTransportType("car");
                courier.setStatus(status);

                apiClient.addCourier(courier);
                imported++;
            } catch (NumberFormatException e) {
                LOG.warning("Рядок " + lineNumber + " пропущено через помилку числа: " + e.getMessage());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Помилка під час імпорту рядка " + lineNumber, e);
                throw e;
            }

            processed++;
            updateProgress(processed, total);
            updateMessage("Опрацьовано " + processed + " із " + total + " (імпортовано: " + imported + ")");
        }

        LOG.info("CSV-імпорт завершено. Успішно додано: " + imported);
        return imported;
    }
}
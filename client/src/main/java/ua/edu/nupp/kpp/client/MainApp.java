package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ua.edu.nupp.kpp.client.model.Client;
import ua.edu.nupp.kpp.client.model.Courier;
import ua.edu.nupp.kpp.client.model.DeliveryOrder;
import ua.edu.nupp.kpp.client.model.Shipment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MainApp extends Application {

    private ApiClient apiClient;

    // 1. Клієнти
    private TableView<Client> clientTable;
    private final TextField clientNameField = new TextField();
    private final TextField clientPhoneField = new TextField();
    private final TextField clientAddressField = new TextField();
    private Long selectedClientId = null;

    // 2. Кур'єри
    private TableView<Courier> courierTable;
    private final TextField courierNameField = new TextField();
    private final TextField courierPhoneField = new TextField();
    private final TextField courierTransportField = new TextField();
    private final TextField courierStatusField = new TextField();
    private Long selectedCourierId = null;

    // 3. Відправлення
    private TableView<Shipment> shipmentTable;
    private final TextField shipmentTrackField = new TextField();
    private final TextField shipmentDescField = new TextField();
    private final TextField shipmentWeightField = new TextField();
    private final TextField shipmentAddrField = new TextField();
    private Long selectedShipmentId = null;

    // 4. Замовлення
    private TableView<DeliveryOrder> orderTable;
    private final TextField orderClientField = new TextField();
    private final TextField orderCourierField = new TextField();
    private final TextField orderShipmentField = new TextField();
    private final TextField orderDateField = new TextField();
    private final TextField orderStatusField = new TextField();
    private final TextField orderCostField = new TextField();
    private Long selectedOrderId = null;

    @Override
    public void start(Stage stage) {
        apiClient = new ApiClient();
        stage.setTitle("Courier Delivery Management (Клієнт)");

        TabPane tabPane = new TabPane();

        Tab clientTab = new Tab("Клієнти", createClientView());
        clientTab.setClosable(false);

        Tab courierTab = new Tab("Кур'єри", createCourierView());
        courierTab.setClosable(false);

        Tab shipmentTab = new Tab("Відправлення", createShipmentView());
        shipmentTab.setClosable(false);

        Tab orderTab = new Tab("Замовлення", createOrderView());
        orderTab.setClosable(false);

        tabPane.getTabs().addAll(clientTab, courierTab, shipmentTab, orderTab);

        Scene scene = new Scene(tabPane, 1100, 620);
        stage.setScene(scene);
        stage.show();

        loadClients();
        loadCouriers();
        loadShipments();
        loadOrders();
    }

    // ==================== 1. ВКЛАДКА КЛІЄНТІВ ====================
    private VBox createClientView() {
        clientTable = new TableView<>();
        TableColumn<Client, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Client, String> nameCol = new TableColumn<>("ПІБ");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Client, String> phoneCol = new TableColumn<>("Телефон");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<Client, String> addrCol = new TableColumn<>("Адреса");
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        clientTable.getColumns().addAll(idCol, nameCol, phoneCol, addrCol);

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                selectedClientId = newV.getId();
                clientNameField.setText(newV.getName());
                clientPhoneField.setText(newV.getPhone());
                clientAddressField.setText(newV.getAddress());
            }
        });

        clientNameField.setPromptText("ПІБ");
        clientPhoneField.setPromptText("Телефон");
        clientAddressField.setPromptText("Адреса");

        Button addBtn = new Button("Додати");
        addBtn.setOnAction(e -> {
            try {
                Client c = new Client(null, clientNameField.getText(), clientPhoneField.getText(), clientAddressField.getText());
                apiClient.addClient(c);
                loadClients();
                clearClientForm();
            } catch (Exception ex) {
                showError("Помилка додавання", ex.getMessage());
            }
        });

        Button updateBtn = new Button("Змінити");
        updateBtn.setOnAction(e -> {
            if (selectedClientId == null) {
                showError("Попередження", "Виберіть клієнта для редагування!");
                return;
            }
            try {
                Client c = new Client(selectedClientId, clientNameField.getText(), clientPhoneField.getText(), clientAddressField.getText());
                apiClient.updateClient(c);
                loadClients();
                clearClientForm();
            } catch (Exception ex) {
                showError("Помилка оновлення", ex.getMessage());
            }
        });

        Button deleteBtn = new Button("Видалити");
        deleteBtn.setOnAction(e -> {
            if (selectedClientId == null) {
                showError("Попередження", "Виберіть клієнта для видалення!");
                return;
            }
            try {
                apiClient.deleteClient(selectedClientId);
                loadClients();
                clearClientForm();
            } catch (Exception ex) {
                showError("Помилка видалення", ex.getMessage());
            }
        });

        Button refreshBtn = new Button("Оновити");
        refreshBtn.setOnAction(e -> loadClients());

        HBox formBox = new HBox(10, clientNameField, clientPhoneField, clientAddressField);
        HBox btnBox = new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn);

        VBox vbox = new VBox(10, clientTable, formBox, btnBox);
        vbox.setPadding(new Insets(15));
        return vbox;
    }

    private void loadClients() {
        try {
            ObservableList<Client> data = FXCollections.observableArrayList(apiClient.getClients());
            clientTable.setItems(data);
        } catch (Exception e) {
            showError("Помилка завантаження клієнтів", e.getMessage());
        }
    }

    private void clearClientForm() {
        selectedClientId = null;
        clientNameField.clear();
        clientPhoneField.clear();
        clientAddressField.clear();
    }

    // ==================== 2. ВКЛАДКА КУР'ЄРІВ + CSV-ІМПОРТ ====================
    private VBox createCourierView() {
        courierTable = new TableView<>();
        TableColumn<Courier, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Courier, String> nameCol = new TableColumn<>("ПІБ");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Courier, String> phoneCol = new TableColumn<>("Телефон");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<Courier, String> transportCol = new TableColumn<>("Транспорт");
        transportCol.setCellValueFactory(new PropertyValueFactory<>("transportType"));
        TableColumn<Courier, String> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        courierTable.getColumns().addAll(idCol, nameCol, phoneCol, transportCol, statusCol);

        courierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                selectedCourierId = newV.getId();
                courierNameField.setText(newV.getName());
                courierPhoneField.setText(newV.getPhone());
                courierTransportField.setText(newV.getTransportType());
                courierStatusField.setText(newV.getStatus());
            }
        });

        courierNameField.setPromptText("ПІБ");
        courierPhoneField.setPromptText("Телефон");
        courierTransportField.setPromptText("Транспорт");
        courierStatusField.setPromptText("Статус");

        Button addBtn = new Button("Додати");
        addBtn.setOnAction(e -> {
            try {
                Courier c = new Courier(null, courierNameField.getText(), courierPhoneField.getText(), courierTransportField.getText(), courierStatusField.getText());
                apiClient.addCourier(c);
                loadCouriers();
                clearCourierForm();
            } catch (Exception ex) {
                showError("Помилка додавання", ex.getMessage());
            }
        });

        Button updateBtn = new Button("Змінити");
        updateBtn.setOnAction(e -> {
            if (selectedCourierId == null) {
                showError("Попередження", "Виберіть кур'єра для редагування!");
                return;
            }
            try {
                Courier c = new Courier(selectedCourierId, courierNameField.getText(), courierPhoneField.getText(), courierTransportField.getText(), courierStatusField.getText());
                apiClient.updateCourier(c);
                loadCouriers();
                clearCourierForm();
            } catch (Exception ex) {
                showError("Помилка оновлення", ex.getMessage());
            }
        });

        Button deleteBtn = new Button("Видалити");
        deleteBtn.setOnAction(e -> {
            if (selectedCourierId == null) {
                showError("Попередження", "Виберіть кур'єра для видалення!");
                return;
            }
            try {
                apiClient.deleteCourier(selectedCourierId);
                loadCouriers();
                clearCourierForm();
            } catch (Exception ex) {
                showError("Помилка видалення", ex.getMessage());
            }
        });

        Button refreshBtn = new Button("Оновити");
        refreshBtn.setOnAction(e -> loadCouriers());

        Button importCsvBtn = new Button("Імпортувати CSV");
        ProgressBar importProgress = new ProgressBar(0);
        importProgress.setPrefWidth(180);
        Label importStatusLabel = new Label("CSV не імпортовано");

        importCsvBtn.setOnAction(e -> {
            ImportCouriersTask task = new ImportCouriersTask(
                    Path.of("sample-data", "couriers.csv"),
                    apiClient
            );

            importCsvBtn.setDisable(true);
            importProgress.progressProperty().bind(task.progressProperty());
            importStatusLabel.textProperty().bind(task.messageProperty());

            task.setOnSucceeded(ev -> {
                importProgress.progressProperty().unbind();
                importStatusLabel.textProperty().unbind();
                importCsvBtn.setDisable(false);
                importStatusLabel.setText("Готово. Імпортовано: " + task.getValue());
                loadCouriers();
            });

            task.setOnFailed(ev -> {
                importProgress.progressProperty().unbind();
                importStatusLabel.textProperty().unbind();
                importCsvBtn.setDisable(false);
                Throwable ex = task.getException();
                String message = (ex != null && ex.getMessage() != null && !ex.getMessage().isBlank())
                        ? ex.getMessage()
                        : "Не вдалося виконати імпорт CSV";
                importStatusLabel.setText("Помилка імпорту");
                showError("Помилка імпорту CSV", message);
            });

            task.setOnCancelled(ev -> {
                importProgress.progressProperty().unbind();
                importStatusLabel.textProperty().unbind();
                importCsvBtn.setDisable(false);
                importStatusLabel.setText("Імпорт скасовано; частина записів могла бути збережена");
            });

            Thread worker = new Thread(task, "csv-import-couriers");
            worker.setDaemon(true);
            worker.start();
        });

        HBox formBox = new HBox(10, courierNameField, courierPhoneField, courierTransportField, courierStatusField);
        HBox btnBox = new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn, importCsvBtn, importProgress, importStatusLabel);

        VBox vbox = new VBox(10, courierTable, formBox, btnBox);
        vbox.setPadding(new Insets(15));
        return vbox;
    }

    private void loadCouriers() {
        try {
            ObservableList<Courier> data = FXCollections.observableArrayList(apiClient.getCouriers());
            courierTable.setItems(data);
        } catch (Exception e) {
            showError("Помилка завантаження кур'єрів", e.getMessage());
        }
    }

    private void clearCourierForm() {
        selectedCourierId = null;
        courierNameField.clear();
        courierPhoneField.clear();
        courierTransportField.clear();
        courierStatusField.clear();
    }

    // ==================== 3. ВКЛАДКА ВІДПРАВЛЕНЬ ====================
    private VBox createShipmentView() {
        shipmentTable = new TableView<>();
        TableColumn<Shipment, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Shipment, String> trackCol = new TableColumn<>("Трак-номер");
        trackCol.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        TableColumn<Shipment, String> descCol = new TableColumn<>("Опис");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Shipment, Double> weightCol = new TableColumn<>("Вага");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        TableColumn<Shipment, String> addrCol = new TableColumn<>("Адреса призначення");
        addrCol.setCellValueFactory(new PropertyValueFactory<>("destinationAddress"));

        shipmentTable.getColumns().addAll(idCol, trackCol, descCol, weightCol, addrCol);

        shipmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                selectedShipmentId = newV.getId();
                shipmentTrackField.setText(newV.getTrackingNumber());
                shipmentDescField.setText(newV.getDescription());
                shipmentWeightField.setText(String.valueOf(newV.getWeight()));
                shipmentAddrField.setText(newV.getDestinationAddress());
            }
        });

        shipmentTrackField.setPromptText("Трак-номер");
        shipmentDescField.setPromptText("Опис");
        shipmentWeightField.setPromptText("Вага");
        shipmentAddrField.setPromptText("Адреса призначення");

        Button addBtn = new Button("Додати");
        addBtn.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(shipmentWeightField.getText());
                Shipment s = new Shipment(null, shipmentTrackField.getText(), shipmentDescField.getText(), weight, shipmentAddrField.getText());
                apiClient.addShipment(s);
                loadShipments();
                clearShipmentForm();
            } catch (Exception ex) {
                showError("Помилка додавання", ex.getMessage());
            }
        });

        Button updateBtn = new Button("Змінити");
        updateBtn.setOnAction(e -> {
            if (selectedShipmentId == null) {
                showError("Попередження", "Виберіть відправлення для редагування!");
                return;
            }
            try {
                double weight = Double.parseDouble(shipmentWeightField.getText());
                Shipment s = new Shipment(selectedShipmentId, shipmentTrackField.getText(), shipmentDescField.getText(), weight, shipmentAddrField.getText());
                apiClient.updateShipment(s);
                loadShipments();
                clearShipmentForm();
            } catch (Exception ex) {
                showError("Помилка оновлення", ex.getMessage());
            }
        });

        Button deleteBtn = new Button("Видалити");
        deleteBtn.setOnAction(e -> {
            if (selectedShipmentId == null) {
                showError("Попередження", "Виберіть відправлення для видалення!");
                return;
            }
            try {
                apiClient.deleteShipment(selectedShipmentId);
                loadShipments();
                clearShipmentForm();
            } catch (Exception ex) {
                showError("Помилка видалення", ex.getMessage());
            }
        });

        Button refreshBtn = new Button("Оновити");
        refreshBtn.setOnAction(e -> loadShipments());

        HBox formBox = new HBox(10, shipmentTrackField, shipmentDescField, shipmentWeightField, shipmentAddrField);
        HBox btnBox = new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn);

        VBox vbox = new VBox(10, shipmentTable, formBox, btnBox);
        vbox.setPadding(new Insets(15));
        return vbox;
    }

    private void loadShipments() {
        try {
            ObservableList<Shipment> data = FXCollections.observableArrayList(apiClient.getShipments());
            shipmentTable.setItems(data);
        } catch (Exception e) {
            showError("Помилка завантаження відправлень", e.getMessage());
        }
    }

    private void clearShipmentForm() {
        selectedShipmentId = null;
        shipmentTrackField.clear();
        shipmentDescField.clear();
        shipmentWeightField.clear();
        shipmentAddrField.clear();
    }

    // ==================== 4. ВКЛАДКА ЗАМОВЛЕНЬ + CSV-ІМПОРТ ====================
    private VBox createOrderView() {
        orderTable = new TableView<>();
        TableColumn<DeliveryOrder, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<DeliveryOrder, Long> clientCol = new TableColumn<>("ID Клієнта");
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        TableColumn<DeliveryOrder, Long> courierCol = new TableColumn<>("ID Кур'єра");
        courierCol.setCellValueFactory(new PropertyValueFactory<>("courierId"));
        TableColumn<DeliveryOrder, Long> shipmentCol = new TableColumn<>("ID Відправлення");
        shipmentCol.setCellValueFactory(new PropertyValueFactory<>("shipmentId"));
        TableColumn<DeliveryOrder, String> dateCol = new TableColumn<>("Дата");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        TableColumn<DeliveryOrder, String> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<DeliveryOrder, Double> costCol = new TableColumn<>("Вартість");
        costCol.setCellValueFactory(new PropertyValueFactory<>("deliveryCost"));

        orderTable.getColumns().addAll(idCol, clientCol, courierCol, shipmentCol, dateCol, statusCol, costCol);

        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                selectedOrderId = newV.getId();
                orderClientField.setText(String.valueOf(newV.getClientId()));
                orderCourierField.setText(String.valueOf(newV.getCourierId()));
                orderShipmentField.setText(String.valueOf(newV.getShipmentId()));
                orderDateField.setText(newV.getOrderDate());
                orderStatusField.setText(newV.getStatus());
                orderCostField.setText(String.valueOf(newV.getDeliveryCost()));
            }
        });

        orderClientField.setPromptText("ID Клієнта");
        orderCourierField.setPromptText("ID Кур'єра");
        orderShipmentField.setPromptText("ID Відправлення");
        orderDateField.setPromptText("Дата (рррр-мм-дд)");
        orderStatusField.setPromptText("Статус");
        orderCostField.setPromptText("Вартість");

        Button addBtn = new Button("Додати");
        addBtn.setOnAction(e -> {
            try {
                Long clientId = Long.parseLong(orderClientField.getText());
                Long courierId = Long.parseLong(orderCourierField.getText());
                Long shipmentId = Long.parseLong(orderShipmentField.getText());
                double cost = Double.parseDouble(orderCostField.getText());

                DeliveryOrder order = new DeliveryOrder(null, clientId, courierId, shipmentId, cost, orderStatusField.getText(), orderDateField.getText());
                apiClient.addOrder(order);
                loadOrders();
                clearOrderForm();
            } catch (Exception ex) {
                showError("Помилка додавання", ex.getMessage());
            }
        });

        Button updateBtn = new Button("Змінити");
        updateBtn.setOnAction(e -> {
            if (selectedOrderId == null) {
                showError("Попередження", "Виберіть замовлення для редагування!");
                return;
            }
            try {
                Long clientId = Long.parseLong(orderClientField.getText());
                Long courierId = Long.parseLong(orderCourierField.getText());
                Long shipmentId = Long.parseLong(orderShipmentField.getText());
                double cost = Double.parseDouble(orderCostField.getText());

                DeliveryOrder order = new DeliveryOrder(selectedOrderId, clientId, courierId, shipmentId, cost, orderStatusField.getText(), orderDateField.getText());
                apiClient.updateOrder(order);
                loadOrders();
                clearOrderForm();
            } catch (Exception ex) {
                showError("Помилка оновлення", ex.getMessage());
            }
        });

        Button statusBtn = new Button("Змінити статус");
        statusBtn.setOnAction(e -> {
            if (selectedOrderId == null) {
                showError("Попередження", "Виберіть замовлення для зміни статусу!");
                return;
            }
            try {
                apiClient.changeOrderStatus(selectedOrderId, orderStatusField.getText());
                loadOrders();
                clearOrderForm();
            } catch (Exception ex) {
                showError("Помилка зміни статусу", ex.getMessage());
            }
        });

        Button deleteBtn = new Button("Видалити");
        deleteBtn.setOnAction(e -> {
            if (selectedOrderId == null) {
                showError("Попередження", "Виберіть замовлення для видалення!");
                return;
            }
            try {
                apiClient.deleteOrder(selectedOrderId);
                loadOrders();
                clearOrderForm();
            } catch (Exception ex) {
                showError("Помилка видалення", ex.getMessage());
            }
        });

        Button refreshBtn = new Button("Оновити");
        refreshBtn.setOnAction(e -> loadOrders());

        // Кнопка початкового CSV-імпорту (Пункт 10 Етапу III)
        Button importCsvBtn = new Button("Імпорт CSV");
        importCsvBtn.setOnAction(e -> importTestCsvData());

        HBox formBox = new HBox(10, orderClientField, orderCourierField, orderShipmentField, orderDateField, orderStatusField, orderCostField);
        HBox btnBox = new HBox(10, addBtn, updateBtn, statusBtn, deleteBtn, refreshBtn, importCsvBtn);

        VBox vbox = new VBox(10, orderTable, formBox, btnBox);
        vbox.setPadding(new Insets(15));
        return vbox;
    }

    private void importTestCsvData() {
        try {
            Path file = Path.of("sample-data", "measurements.csv");

            if (!Files.exists(file)) {
                showError("Помилка CSV", "Файл " + file.toString() + " не знайдено у папці sample-data!");
                return;
            }

            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            if (lines.size() > 1) {
                String dataLine = lines.get(1);
                String[] parts = dataLine.split(";", -1);

                if (parts.length < 4) {
                    throw new IllegalArgumentException("Некоректна кількість стовпців у CSV: " + parts.length);
                }

                Long clientId = Long.parseLong(parts[0].trim());
                Long courierId = Long.parseLong(parts[1].trim());
                Long shipmentId = Long.parseLong(parts[2].trim());
                double cost = Double.parseDouble(parts[3].trim());
                String status = parts.length > 4 ? parts[4].trim() : "NEW";
                String orderDate = parts.length > 5 ? parts[5].trim() : "2026-11-18";

                DeliveryOrder newOrder = new DeliveryOrder(
                        null, clientId, courierId, shipmentId, cost, status, orderDate
                );

                apiClient.addOrder(newOrder);
                loadOrders();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успіх CSV");
                alert.setHeaderText(null);
                alert.setContentText("Тестовий рядок з CSV успішно розібрано та надіслано на сервер!");
                alert.showAndWait();

            } else {
                showError("Попередження", "CSV-файл містить лише заголовок або є порожнім.");
            }

        } catch (NumberFormatException e) {
            showError("Помилка формату", "Помилка чисельного конвертування з CSV: " + e.getMessage());
        } catch (Exception e) {
            showError("Помилка CSV", "Не вдалося обробити CSV-файл: " + e.getMessage());
        }
    }

    private void loadOrders() {
        try {
            ObservableList<DeliveryOrder> data = FXCollections.observableArrayList(apiClient.getOrders());
            orderTable.setItems(data);
        } catch (Exception e) {
            showError("Помилка завантаження замовлень", e.getMessage());
        }
    }

    private void clearOrderForm() {
        selectedOrderId = null;
        orderClientField.clear();
        orderCourierField.clear();
        orderShipmentField.clear();
        orderDateField.clear();
        orderStatusField.clear();
        orderCostField.clear();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
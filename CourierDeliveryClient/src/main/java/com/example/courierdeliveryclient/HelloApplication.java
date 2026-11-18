package com.example.courierdeliveryclient;

import com.example.courierdeliveryclient.model.Client;
import com.example.courierdeliveryclient.service.ApiClient;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class HelloApplication extends Application {

    private ApiClient apiClient;
    private TableView<Client> table;

    @Override
    public void start(Stage stage) {
        apiClient = new ApiClient();
        stage.setTitle("Courier Delivery Management - Клієнт");

        // Створюємо таблицю для клієнтів
        table = new TableView<>();

        TableColumn<Client, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Client, String> nameCol = new TableColumn<>("ПІБ");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(200);

        TableColumn<Client, String> phoneCol = new TableColumn<>("Телефон");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setMinWidth(130);

        TableColumn<Client, String> addressCol = new TableColumn<>("Адреса");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setMinWidth(250);

        table.getColumns().addAll(idCol, nameCol, phoneCol, addressCol);

        // Кнопка для завантаження/оновлення даних з сервера
        Button loadButton = new Button("Завантажити клієнтів з сервера");
        loadButton.setOnAction(e -> loadClientsData());

        // Головний контейнер
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.getChildren().addAll(loadButton, table);

        Scene scene = new Scene(vbox, 700, 400);
        stage.setScene(scene);
        stage.show();

        // Автоматично завантажуємо дані при старті
        loadClientsData();
    }

    private void loadClientsData() {
        List<Client> clientsList = apiClient.getClients();
        ObservableList<Client> observableClients = FXCollections.observableArrayList(clientsList);
        table.setItems(observableClients);
    }

    public static void main(String[] args) {
        launch();
    }
}

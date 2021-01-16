package delivery.management.controller;

import delivery.management.model.Delivery;
import delivery.management.model.Letter;
import delivery.management.model.Received;
import delivery.management.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DeliveryController extends Controller{
    @FXML Button backButton, imageButton;
    @FXML TableView tableView;
    @FXML TableColumn<Received, String> typeCol, numCol, desCol, sizeCol, timeCol, receiverCol;

    protected String cache; // store addition data
    protected Delivery data;

    @Override
    public void initialize() {
        super.initialize();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Received[] receiveds = Received.getReceivedData();
                    if (receiveds != null) {
                        // Show data on TableView
                        ObservableList<Received> receivedList = FXCollections.observableArrayList();
                        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeData"));
                        numCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
                        desCol.setCellValueFactory(new PropertyValueFactory<>("senderInformation"));
                        sizeCol.setCellValueFactory(new PropertyValueFactory<>("stringSize"));
                        timeCol.setCellValueFactory(new PropertyValueFactory<>("datetimeData"));
                        receiverCol.setCellValueFactory(new PropertyValueFactory<>("receiverData"));
                        for (Received received : receiveds)
                            receivedList.add(received);
                        tableView.setItems(receivedList);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initialize(String comment) {
        super.initialize();
    }

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/staff.fxml", condo.getSession());
    }

    @FXML public void handleLetterButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/letter.fxml", condo.getSession());
    }

    @FXML public void handleSuppliesButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/supplies.fxml", condo.getSession());
    }

    @FXML public void handleDocumentButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/document.fxml", condo.getSession());
    }

    @FXML public void handleSearchButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/search.fxml", condo.getSession());
    }

    @FXML public void handleDeliveryViewPage(ActionEvent event, String fxmlPath, User session, Delivery data) throws IOException {
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(loader.load(), 800, 600));

        DeliveryController controller = loader.getController();
        controller.setSession(session);
        controller.setData(data);

        stage.show();
    }

    @FXML public void handleDeliveryViewPageCache(ActionEvent event, String fxmlPath, User session, Delivery data, String cache) throws IOException {
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(loader.load(), 800, 600));

        DeliveryController controller = loader.getController();
        controller.setSession(session);
        controller.setData(data);
        controller.setCache(cache);

        stage.show();
    }

    public void setData(Delivery data) {
        this.data = data;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getCache() {
        return cache;
    }
}
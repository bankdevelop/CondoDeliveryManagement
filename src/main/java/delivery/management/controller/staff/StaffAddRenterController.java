package delivery.management.controller.staff;

import delivery.management.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import delivery.management.controller.Controller;
import delivery.management.model.Renter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

public class StaffAddRenterController extends Controller {
    private String roomNumber;

    @FXML Button roomButton, deliveryButton, addNewButton;
    @FXML TextField addNewName, addNewTel;
    @FXML Label roomNumberLabel, errorLabel, succedLabel;
    @FXML TableView renterTableView;
    @FXML TableColumn<Renter, String> renterName, telNumber, colControl;

    @Override
    public void initialize() {
        super.initialize();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                roomNumberLabel.setText(roomNumber);
                try {
                    Renter[] renters = Renter.getRentData(roomNumber);
                    if (renters != null) {
                        for (Renter renter:renters){
                            Button temp = new Button("Delete");
                            temp.setOnAction(event -> {
                                try {
                                    handleDeleteButton(event, roomNumber, renter.getTelNumber());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            renter.setControlButton(temp);
                        }

                        // Show data on TableView
                        ObservableList<Renter> renterlist = FXCollections.observableArrayList();
                        renterName.setCellValueFactory(new PropertyValueFactory<>("renterName"));
                        telNumber.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
                        colControl.setCellValueFactory(new PropertyValueFactory<>("colControl"));
                        for (Renter renter : renters) {
                            renterlist.add(renter);
                        }
                        renterTableView.setItems(renterlist);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCurrentRoom(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    @FXML public void handleDeleteButton(ActionEvent event, String roomNumber, String telNumber) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "ต้องการลบผู้เช่าออก"+ " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Renter.removeRenter(roomNumber, telNumber);
            handleNextRenterPage(event, condo.getSession(), roomNumber);
        }
    }

    @FXML public void handleAddNewButton(ActionEvent event) throws IOException {
        if (addNewName.getText().equals("")) errorLabel.setText("กรุณากรอก Name");
        else if (addNewTel.getText().equals("")) errorLabel.setText("กรุณากรอก TelNumber");
        else {
            if (!Renter.isRoomFull(roomNumber)) {
                String timeStr = LocalDateTime.now().toString();
                Renter renter = new Renter(roomNumber, addNewName.getText(), addNewTel.getText(), timeStr);
                if (Renter.addRenter(roomNumber, renter)) {
                    errorLabel.setText("");
                    succedLabel.setText("ลงทะเบียนเสร็จเรียบร้อย");
                    handleNextRenterPage(event, condo.getSession(), roomNumber);
                }else{
                    errorLabel.setText("ลงทะเบียนผู้ใช้ซ่ำ");
                }
            }else{
                errorLabel.setText("ห้องนี้เต็มแล้ว");
            }
        }
    }

    @FXML public void handleRoomButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/staff.fxml", condo.getSession());
    }

    @FXML public void handleDeliveryButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/delivery.fxml", condo.getSession());
    }

    @FXML public void handleNextRenterPage(ActionEvent event, User session, String roomNumber) throws IOException {
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/staff/renter.fxml"));
        stage.setScene(new Scene(loader.load(), 800, 600));

        StaffAddRenterController TC = loader.getController();
        TC.setCurrentRoom(roomNumber);
        TC.setSession(session);

        stage.show();
    }
}

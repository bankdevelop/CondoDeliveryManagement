package delivery.management.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import delivery.management.controller.staff.StaffAddRenterController;
import delivery.management.model.Renter;
import delivery.management.model.Room;
import delivery.management.model.User;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StaffController extends Controller {
    private static String path = "";

    @FXML FlowPane roomButton;
    @FXML Label roomNumberLabel, floorLabel, numLabel, statusLabel, typeLabel, errorLabel;
    @FXML HBox controllerHBox;
    @FXML Button deliveryButton, addNewRoomButton;
    @FXML CheckBox oneTypeCheckBox, zeroTypeCheckBox;
    @FXML Spinner<Integer> buildingSpinner, floorSpinner, numSpinner;
    
    private int currentType = 0;

    @Override
    public void initialize() {
        super.initialize();

        SpinnerValueFactory<Integer> buildSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,9);
        SpinnerValueFactory<Integer> floorSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,9);
        SpinnerValueFactory<Integer> numSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,9);
        buildingSpinner.setValueFactory(buildSpinnerValue);
        floorSpinner.setValueFactory(floorSpinnerValue);
        numSpinner.setValueFactory(numSpinnerValue);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Room[] listRoom = Room.getRoomData();
                    roomButton.setMaxWidth(380);
                    condo.setListRoom(listRoom);

                    int hieght = listRoom.length;

                    ToggleGroup group = new ToggleGroup();

                    for (int i=hieght-1; i>=0; i--){
                        Room data = listRoom[i];
                        ToggleButton button = new ToggleButton(data.getRoomNumber());
                        button.setToggleGroup(group);
                        button.setOnAction(event -> handleRoomButton(event));

                        if ( data.isRented() )
                            button.setStyle("-fx-background-color: pink;");
                        else
                            button.setStyle("-fx-background-color: greenyellow;");
                        roomButton.getChildren().addAll(button);
                    }

                }catch (FileNotFoundException e){
                    System.out.println(e);
                }
            }
        });
    }

    @FXML public void handleRoomButton(ActionEvent event){
        controllerHBox.getChildren().clear();
        String text = ((ToggleButton)event.getSource()).getText();
        int floor = Integer.parseInt(text.charAt(1)+"");
        int num = Integer.parseInt(text.charAt(2)+"");
        Room[] rooms = condo.getListRoom();
        Room room = null;

        for (Room temp : rooms)
            if (temp.getRoomNumber().equals(text))
                room = temp;

        if (room != null){
            roomNumberLabel.setText(text);
            floorLabel.setText(floor+"");
            numLabel.setText(num+1+"");
            typeLabel.setText(room.getTypeRoom());

            Button button, button2;
            if (room.isRented()) {
                button = new Button("เคลียร์ห้องให้ว่าง");
                button.setOnAction(e -> {
                    try {
                        handleClearRoomButton(e, text);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                button.setStyle("-fx-background-color: pink;");

                button2 = new Button("จัดการผู้เช่า");
                button2.setOnAction(e -> {
                    try {
                        handleRenterButton(e, text);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                button2.setStyle("-fx-background-color: yellow;");
                controllerHBox.getChildren().addAll(button2);

                statusLabel.setText("มีคนเช่าอยู่");
                statusLabel.setStyle("-fx-text-fill: red;");
            }else{
                button = new Button("เพิ่มคนเช่า");
                button.setOnAction(e -> {
                    try {
                        handleRenterButton(e, text);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                button.setStyle("-fx-background-color: greenyellow;");

                statusLabel.setText("ห้องว่าง");
                statusLabel.setStyle("-fx-text-fill: green;");
            }
            controllerHBox.getChildren().addAll(button);
        }
    }

    @FXML public void handleClearRoomButton(ActionEvent event, String roomNumber) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "ต้องการลบผู้เช่าปัจจุบันออก"+ " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Renter.removeRent(roomNumber);
            handleNextPage(event, "/view/staff.fxml", condo.getSession());
        }
    }

    @FXML public void handleRenterButton(ActionEvent event, String roomNumber) throws IOException {
        handleNextRenterPage(event, condo.getSession(), roomNumber);
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

    @FXML public void handleDeliveryButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/delivery.fxml", condo.getSession());
    }

    @FXML public void handleChangeGoPassPageButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/staff/staffchangepass.fxml", condo.getSession());
    }

    @FXML public void handleZeroTypeCheckbox(ActionEvent event){
        handleClearAllCheckBox();
        this.currentType = 0;
        zeroTypeCheckBox.setSelected(true);
    }

    @FXML public void handleOneTypeCheckbox(ActionEvent event){
        handleClearAllCheckBox();
        this.currentType = 1;
        oneTypeCheckBox.setSelected(true);
    }

    @FXML public void handleClearAllCheckBox(){
        zeroTypeCheckBox.setSelected(false);
        oneTypeCheckBox.setSelected(false);
    }

    @FXML public void handleAddNewRoomButton(ActionEvent event) throws IOException {
        int buildingNum = buildingSpinner.getValue();
        int floor = floorSpinner.getValue();
        int num = numSpinner.getValue();
        String roomNumber = ""+buildingNum+floor+num;

        boolean result = Room.addNewRoom(new Room(roomNumber, this.currentType));
        if (!result) errorLabel.setText("หมายเลขห้องนี้ถูกใช้แล้ว");
        else handleNextPage(event, "/view/staff.fxml", condo.getSession());
    }
}
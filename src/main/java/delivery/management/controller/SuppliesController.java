package delivery.management.controller;

import delivery.management.model.Letter;
import delivery.management.model.Room;
import delivery.management.model.Supplies;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

public class SuppliesController extends DeliveryController{
    @FXML Button backButton, imageButton;
    @FXML Label imageLabel, errorLabel;
    @FXML CheckBox size1, size2, size3, size4;
    @FXML TextField roomNumField, serviceField, trackingField;
    @FXML TextArea senderInfoField;
    @FXML TableView tableView;
    @FXML TableColumn<Letter, String> colRoomNum, colSender, colSize, colDatetime;
    @FXML TableColumn<Letter, Button> colView, colReceive;
    
    private FileChooser fileChooser;
    private File filePath;
    private int currentSize = 1;

    @Override
    public void initialize() {
        super.initialize("ignore Delivery initialize but use Controller initialize instead");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Supplies[] supplies = Supplies.getSuppliesData();
                    if (supplies != null) {
                        // Show data on TableView
                        ObservableList<Supplies> suppliesList = FXCollections.observableArrayList();
                        colRoomNum.setCellValueFactory(new PropertyValueFactory<>("recipientRoomNumber"));
                        colSender.setCellValueFactory(new PropertyValueFactory<>("senderInformation"));
                        colSize.setCellValueFactory(new PropertyValueFactory<>("stringSize"));
                        colDatetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
                        colView.setCellValueFactory(new PropertyValueFactory<>("view"));
                        colReceive.setCellValueFactory(new PropertyValueFactory<>("receive"));
                        for (Supplies supplie : supplies) {
                            Button viewLetter = new Button("ดูข้อมูล");
                            Button receiveLetter = new Button("รับของ");
                            viewLetter.setOnAction(event -> {
                                try {
                                    handleViewLetterButton(event, supplie);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            receiveLetter.setOnAction(event -> {
                                try {
                                    handleReceiveLetterButton(event, supplie);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            supplie.setReceive(receiveLetter);
                            supplie.setView(viewLetter);
                            suppliesList.add(supplie);
                        }
                        tableView.setItems(suppliesList);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML public void handleImageButton(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        fileChooser = new FileChooser();
        this.filePath = fileChooser.showOpenDialog(stage);

        if (this.filePath != null)
            imageLabel.setText(filePath.getPath());
    }

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/delivery.fxml", condo.getSession());
    }

    @FXML private void handleSize1CheckBox(ActionEvent event){
        clearSizeCheckBox();
        this.currentSize = 1;
        size1.setSelected(true);
    }

    @FXML private void handleSize2CheckBox(ActionEvent event){
        clearSizeCheckBox();
        this.currentSize = 2;
        size2.setSelected(true);
    }

    @FXML private void handleSize3CheckBox(ActionEvent event){
        clearSizeCheckBox();
        this.currentSize = 3;
        size3.setSelected(true);
    }

    @FXML private void handleSize4CheckBox(ActionEvent event){
        clearSizeCheckBox();
        this.currentSize = 4;
        size4.setSelected(true);
    }

    @FXML private void clearSizeCheckBox(){
        size1.setSelected(false);
        size2.setSelected(false);
        size3.setSelected(false);
        size4.setSelected(false);
    }

    @FXML public void handleViewLetterButton(ActionEvent event, Supplies supplie) throws IOException {
        handleDeliveryViewPage(event, "/view/suppliesview.fxml", condo.getSession(), supplie);
    }

    @FXML public void handleReceiveLetterButton(ActionEvent event, Supplies supplies) throws IOException{
        boolean result = Supplies.receiveSupplies(supplies, condo.getSession().getName());
        handleNextPage(event, "/view/supplies.fxml", condo.getSession());
    }

    @FXML public void handleAddNewLetterButton(ActionEvent event) throws IOException {
        if (roomNumField.getText().equals("")) errorLabel.setText("กรุณากรอกหมายเลขห้อง");
        else if (senderInfoField.getText().equals("")) errorLabel.setText("กรุณากรอกข้อมูลผู้ส่ง");
        else if (serviceField.getText().equals("")) errorLabel.setText("กรุณากรอกชื่อบริการขนส่ง");
        else if (trackingField.getText().equals("")) errorLabel.setText("กรุณากรอกเลข Tracking Number");
        else if (filePath == null) errorLabel.setText("กรุณาเลือกรูปภาพ");
        else if ( Room.getRoomType(roomNumField.getText()) == -1 )
            errorLabel.setText("ไม่พบหมายเลขห้อง");
        else {
            String roomNum = roomNumField.getText();
            String senderInfomation = senderInfoField.getText();
            String image = filePath.getPath();
            String service = serviceField.getText();
            String tracking = trackingField.getText();
            String datetime = LocalDateTime.now().toString();

            Supplies.addNewSupplies(new Supplies(roomNum, senderInfomation, image, currentSize, service, tracking, datetime, condo.getSession().getName()));
            handleNextPage(event, "/view/supplies.fxml", condo.getSession());
        }
    }
}

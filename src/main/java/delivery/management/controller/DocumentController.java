package delivery.management.controller;

import delivery.management.model.Document;
import delivery.management.model.Letter;
import delivery.management.model.Room;
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

public class DocumentController extends DeliveryController{
    @FXML Label imageLabel, errorLabel;
    @FXML CheckBox size1, size2, size3, priority1, priority2, priority3;
    @FXML TextField roomNumField;
    @FXML TextArea senderInfoField;
    @FXML TableView tableView;
    @FXML TableColumn<Letter, String> colRoomNum, colSender, colSize, colDatetime;
    @FXML TableColumn<Letter, Button> colView, colReceive;
    
    private FileChooser fileChooser;
    private File filePath;
    private int currentSize = 1;
    private int currentPriority = 1;

    @Override
    public void initialize() {
        super.initialize("ignore Delivery initialize but use Controller initialize instead");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Document[] documents = Document.getDocumentData();
                    if (documents != null) {
                        // Show data on TableView
                        ObservableList<Document> documentList = FXCollections.observableArrayList();
                        colRoomNum.setCellValueFactory(new PropertyValueFactory<>("recipientRoomNumber"));
                        colSender.setCellValueFactory(new PropertyValueFactory<>("senderInformation"));
                        colSize.setCellValueFactory(new PropertyValueFactory<>("stringSize"));
                        colDatetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
                        colView.setCellValueFactory(new PropertyValueFactory<>("view"));
                        colReceive.setCellValueFactory(new PropertyValueFactory<>("receive"));
                        for (Document document : documents) {
                            Button viewLetter = new Button("ดูข้อมูล");
                            Button receiveLetter = new Button("รับของ");
                            viewLetter.setOnAction(event -> {
                                try {
                                    handleViewLetterButton(event, document);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            receiveLetter.setOnAction(event -> {
                                try {
                                    handleReceiveLetterButton(event, document);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            document.setReceive(receiveLetter);
                            document.setView(viewLetter);
                            documentList.add(document);
                        }
                        tableView.setItems(documentList);
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

    @FXML private void clearSizeCheckBox(){
        size1.setSelected(false);
        size2.setSelected(false);
        size3.setSelected(false);
    }

    @FXML private void handlePriority1CheckBox(ActionEvent event){
        clearPriority1CheckBox();
        this.currentPriority = 1;
        priority1.setSelected(true);
    }

    @FXML private void handlePriority2CheckBox(ActionEvent event){
        clearPriority1CheckBox();
        this.currentPriority = 2;
        priority2.setSelected(true);
    }

    @FXML private void handlePriority3CheckBox(ActionEvent event){
        clearPriority1CheckBox();
        this.currentPriority = 3;
        priority3.setSelected(true);
    }

    @FXML private void clearPriority1CheckBox(){
        priority1.setSelected(false);
        priority2.setSelected(false);
        priority3.setSelected(false);
    }

    @FXML public void handleViewLetterButton(ActionEvent event, Document document) throws IOException {
        handleDeliveryViewPage(event, "/view/documentview.fxml", condo.getSession(), document);
    }

    @FXML public void handleReceiveLetterButton(ActionEvent event, Document document) throws IOException{
        boolean result = Document.receiveDocument(document, condo.getSession().getName());
        handleNextPage(event, "/view/document.fxml", condo.getSession());
    }

    @FXML public void handleAddNewLetterButton(ActionEvent event) throws IOException {
        if (roomNumField.getText().equals("")) errorLabel.setText("กรุณากรอกหมายเลขห้อง");
        else if (senderInfoField.getText().equals("")) errorLabel.setText("กรุณากรอกข้อมูลผู้ส่ง");
        else if (filePath == null) errorLabel.setText("กรุณาเลือกรูปภาพ");
        else if ( Room.getRoomType(roomNumField.getText()) == -1 )
            errorLabel.setText("ไม่พบหมายเลขห้อง");
        else {
            String roomNum = roomNumField.getText();
            String senderInfomation = senderInfoField.getText();
            String datetime = LocalDateTime.now().toString();
            String image = filePath.getPath();

            Document.addNewDocument(new Document(roomNum, senderInfomation, image, currentSize, currentPriority, datetime, condo.getSession().getName()));
            handleNextPage(event, "/view/document.fxml", condo.getSession());
        }
    }
}

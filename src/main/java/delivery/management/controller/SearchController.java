package delivery.management.controller;

import delivery.management.model.*;
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
import java.util.ArrayList;

public class SearchController extends DeliveryController{
    @FXML Button backButton, searchButton;
    @FXML Label resultLabel;
    @FXML TextField searchBar;
    @FXML TableView tableView;
    @FXML TableColumn<Letter, String> colRoomNum, colSender, colSize, colDatetime;
    @FXML TableColumn<Letter, Button> colView, colReceive;

    private String currentRoom = null;

    @Override
    public void initialize() {
        super.initialize("ignore Delivery initialize but use Controller initialize instead");
    }

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/delivery.fxml", condo.getSession());
    }

    @FXML public void handleSearchButton(ActionEvent event) throws IOException {
        if (!searchBar.equals("")) {
            currentRoom = searchBar.getText();
            resultLabel.setText(currentRoom);
            searchBar.setText(currentRoom);
            setTableView();
        }
    }

    @FXML public void handleViewButton(ActionEvent event, Delivery obj) throws IOException {
        handleViewButtonAction(event, obj, "/view/search.fxml");
    }

    @FXML public void handleViewButtonAction(ActionEvent event, Delivery obj, String path) throws IOException {
        String className = obj.getClass().getName();
        if (className.equals("delivery.management.model.Letter"))
            handleDeliveryViewPageCache(event, "/view/letterview.fxml", condo.getSession(), obj, path);
        else if (className.equals("delivery.management.model.Supplies"))
            handleDeliveryViewPageCache(event, "/view/suppliesview.fxml", condo.getSession(), obj, path);
        else if (className.equals("delivery.management.model.Document"))
            handleDeliveryViewPageCache(event, "/view/documentview.fxml", condo.getSession(), obj, path);
        else return;
    }

    @FXML public void handleReceiveButton(ActionEvent event, Delivery obj) throws IOException{
        handleReceiveButtonAction(event, obj);
        setTableView();
    }

    @FXML public void handleReceiveButtonAction(ActionEvent event, Delivery obj) throws IOException{
        String className = obj.getClass().getName();
        if (className.equals("delivery.management.model.Letter"))
            Letter.receiveLetter((Letter)obj, condo.getSession().getName());
        else if (className.equals("delivery.management.model.Document"))
            Document.receiveDocument((Document)obj, condo.getSession().getName());
        else if (className.equals("delivery.management.model.Supplies"))
            Supplies.receiveSupplies((Supplies)obj, condo.getSession().getName());
        else return;
    }

    public ObservableList<Delivery> searchData(String roomNumber) throws FileNotFoundException {
        ArrayList<Delivery> items = mergeData();
        if (items.size() == 0) return null;
        ArrayList<Delivery> data = new ArrayList<>();
        for (Delivery item : items)
            if (item.getRecipientRoomNumber().equals(roomNumber))
                data.add(item);

        if (data.size() == 0) return null;
        return addButton(data);
    }

    @FXML public void setTableView() {
        try {
            if (currentRoom != null) {
                ObservableList<Delivery> data = searchData(currentRoom);
                if (data != null) {
                    colRoomNum.setCellValueFactory(new PropertyValueFactory<>("recipientRoomNumber"));
                    colSender.setCellValueFactory(new PropertyValueFactory<>("senderInformation"));
                    colSize.setCellValueFactory(new PropertyValueFactory<>("stringSize"));
                    colDatetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
                    colView.setCellValueFactory(new PropertyValueFactory<>("view"));
                    colReceive.setCellValueFactory(new PropertyValueFactory<>("receive"));
                    tableView.setItems(data);
                } else {
                    //set blank
                    tableView.setItems(FXCollections.observableArrayList());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Delivery> addButton(ArrayList<Delivery> objList) throws FileNotFoundException {
        ObservableList<Delivery> datalist = FXCollections.observableArrayList();
        for (Delivery obj : objList) {
            Button view = new Button("ดูข้อมูล");
            Button receive = new Button("รับของ");
            view.setOnAction(event -> {
                try {
                    handleViewButton(event, obj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receive.setOnAction(event -> {
                try {
                    handleReceiveButton(event, obj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            obj.setReceive(receive);
            obj.setView(view);
            datalist.add(obj);
        }
        return datalist;
    }

    public ArrayList<Delivery> mergeData() throws FileNotFoundException {
        Letter[] letters = Letter.getLetterData();
        Document[] documents = Document.getDocumentData();
        Supplies[] supplies = Supplies.getSuppliesData();

        ArrayList<Delivery> data = new ArrayList<>();
        if (letters != null) for (Letter letter : letters) data.add(letter);
        if (documents != null) for (Document document : documents) data.add(document);
        if (supplies != null) for (Supplies suppliesl : supplies) data.add(suppliesl);
        if (data.size() == 0) return null;

        return data;
    }

}

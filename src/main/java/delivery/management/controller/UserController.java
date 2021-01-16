package delivery.management.controller;

import delivery.management.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserController extends SearchController{
    @FXML Button logoutButton;
    @FXML TableView tableView;
    @FXML TableColumn<Letter, String> colRoomNum, colSender, colSize, colDatetime;
    @FXML TableColumn<Letter, Button> colView, colReceive;

    @Override
    public void initialize() {
        super.initialize();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<Delivery> data = null;
                try {
                    data = searchData(getRentListRoom());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (data != null) {
                    colRoomNum.setCellValueFactory(new PropertyValueFactory<>("recipientRoomNumber"));
                    colSender.setCellValueFactory(new PropertyValueFactory<>("senderInformation"));
                    colSize.setCellValueFactory(new PropertyValueFactory<>("stringSize"));
                    colDatetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
                    colView.setCellValueFactory(new PropertyValueFactory<>("view"));
                    colReceive.setCellValueFactory(new PropertyValueFactory<>("receive"));
                    tableView.setItems(data);
                }
            }
        });
    }

    public ObservableList<Delivery> searchData(HashMap<String, Integer> listRoom) throws FileNotFoundException {
        ArrayList<Delivery> items = mergeData();
        if (items.size() == 0) return null;
        ArrayList<Delivery> data = new ArrayList<>();
        for (Delivery item : items)
            if (listRoom.containsKey(item.getRecipientRoomNumber()))
                data.add(item);

        if (data.size() == 0) return null;
        return addButton(data);
    }

    @Override
    @FXML public void handleViewButton(ActionEvent event, Delivery obj) throws IOException {
        handleViewButtonAction(event, obj, "/view/user.fxml");
    }

    @FXML public void handleReceiveButton(ActionEvent event, Delivery obj) throws IOException{
        handleReceiveButtonAction(event, obj);
        handleNextPage(event, "/view/user.fxml", condo.getSession());
    }

    public HashMap<String, Integer> getRentListRoom(){
        try {
            Renter[] renters = Renter.getRentData();
            HashMap<String, Integer> listRoom = new HashMap<>();
            if (renters == null) return null;

            for (Renter renter:renters){
                if (renter.getRenterName().equals(condo.getSession().getName())){
                    String roomNum = renter.getRentRoomNumber();
                    if(listRoom.containsKey(roomNum))
                        listRoom.put(roomNum, listRoom.get(roomNum)+1);
                    else
                        listRoom.put(roomNum, 1);
                }
            }
            return listRoom;

        } catch (FileNotFoundException e){
            System.err.println(e);
        }
        return null;
    }

    @FXML public void handleChangePassButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/user/changepass.fxml", condo.getSession());
    }
}

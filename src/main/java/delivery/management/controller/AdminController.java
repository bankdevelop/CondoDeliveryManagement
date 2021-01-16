package delivery.management.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import delivery.management.model.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class AdminController extends Controller {
    @FXML Label listName, errorLabel, sucessLabel;
    @FXML TextField passField;
    @FXML Button registerUserButton, changePassButton;
    @FXML TableView userTableView;
    @FXML TableColumn<User, String> colName, colUser, colDatetime;

    @Override
    public void initialize() {
        super.initialize();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    User[] userList = User.getUserData();
                    if (userList != null) {
                        ArrayList<User> temp = new ArrayList<>();
                        for (User user : userList) {
                            if (user.getPermission()==1) temp.add(user);
                        }

                        Arrays.sort(userList, new Comparator<User>() {
                            @Override
                            public int compare(User o1, User o2) {
                                return o1.getDateTime().compareTo(o2.getDateTime());
                            }
                        });
                        // Show data on TableView
                        ObservableList<User> letterList = FXCollections.observableArrayList();
                        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
                        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
                        colDatetime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
                        for (int i=0; i<Math.min(temp.size(), 5); i++) {
                            letterList.add(temp.get(i));
                        }
                        userTableView.setItems(letterList);
                    }

                }catch (FileNotFoundException e){
                    listName.setText("ไม่สามารถดึงข้อมูล user มาแสดงได้");
                }
            }
        });
    }

    @FXML public void handleRegisterUserButton(ActionEvent event) throws IOException{
        handleNextPage(event, "/view/admin/registerUser.fxml", condo.getSession());
    }

    @FXML public void handleChangePassButton(ActionEvent event) {
        sucessLabel.setText("");
        errorLabel.setText("");
        String pw = passField.getText();
        if (pw.equals("") || pw.length() < 8)
            errorLabel.setText("กรุณากรอกรหัสผ่านอย่างน้อย 8 ตัวอักษร");
        else {
            String ur = condo.getSession().getUsername();
            try {
                User newSession = User.changePass(ur, pw);
                if (newSession == null)
                    throw new FileNotFoundException();
                condo.setSession(newSession);
                sucessLabel.setText("เปลี่ยนรหัสผ่านเรียบร้อย");
            }catch (FileNotFoundException e){
                errorLabel.setText("ระบบมีความผิดพลาด");
            }
        }
    }
}

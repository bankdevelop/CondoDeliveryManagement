package delivery.management.controller.admin;

import delivery.management.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import delivery.management.controller.Controller;

import java.io.IOException;

public class AdminRegisterUserController extends Controller {
    @FXML Button backButton, registerButton;
    @FXML TextField nameField, userField, passField;
    @FXML Label errorLabel, correctLabel;

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/admin.fxml", condo.getSession());
    }

    @FXML public void handleReigsterButton(ActionEvent event) throws IOException {
        correctLabel.setText("");
        errorLabel.setText("");
        if (nameField.getText().equals("")) errorLabel.setText("กรุณากรอก Name");
        else if (userField.getText().equals("")) errorLabel.setText("กรุณากรอก Username");
        else if (passField.getText().equals("")) errorLabel.setText("กรุณากรอก Password");
        else {
            User result = User.registerUser(nameField.getText(),userField.getText(), passField.getText(), 1);
            if (result==null){
                errorLabel.setText("Username นี้มีคนใช้อยู่แล้ว");
            }else{
                correctLabel.setText("สร้างบัญชีเสร็จสิ้น");
                nameField.clear();
                userField.clear();
                passField.clear();
            }
        }
    }
}

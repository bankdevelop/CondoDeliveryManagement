package delivery.management.controller;

import delivery.management.model.Renter;
import delivery.management.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterController extends Controller {
    @FXML Button backButton, registerButton;
    @FXML TextField nameField, passField, userField, telField;
    @FXML Label errorLabel, sucessLabel;

    @FXML public void handleRegisterButton(ActionEvent event) throws FileNotFoundException {
        errorLabel.setText("");
        sucessLabel.setText("");

        if (nameField.getText().length() < 5) errorLabel.setText("ชื่อต้องยาวอย่างน้อย 5 ตัวอักษระ");
        else if (passField.getText().length() < 8) errorLabel.setText("พาสเวิร์ดต้องยาวอย่างน้อย 8 ตัวอักษร");
        else if (userField.getText().length() < 5) errorLabel.setText("Username ต้องยาวอย่างน้อย 5 ตัวอักษร");
        else if (telField.getText().length() < 8) errorLabel.setText("เบอร์โทรศัพท์ต้องใช้อย่างน้อย 8 เลข");
        else {
            Renter[] renters = Renter.returnRenter(nameField.getText(), telField.getText());
            if (renters != null) {
                User result = User.registerUser(nameField.getText(), userField.getText(), passField.getText(), 0);
                if (result == null) {
                    errorLabel.setText("Username นี้มีคนใช้อยู่แล้ว");
                } else {
                    sucessLabel.setText("สร้างบัญชีเสร็จสิ้น");
                    nameField.clear();
                    userField.clear();
                    passField.clear();
                    telField.clear();
                }
            } else errorLabel.setText("ชื่อหรือเบอร์โทรศัพท์ไม่ถูกต้อง");
        }
    }

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/sample.fxml");
    }
}

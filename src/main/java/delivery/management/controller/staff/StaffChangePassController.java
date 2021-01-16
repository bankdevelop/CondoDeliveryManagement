package delivery.management.controller.staff;

import delivery.management.controller.Controller;
import delivery.management.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.FileNotFoundException;
import java.io.IOException;


public class StaffChangePassController extends Controller {
    @FXML PasswordField newPassField, againNewPassField;
    @FXML Label errorLabel, sucessLabel;
    @FXML Button changePassButton;

    @FXML public void handleChangePassButton(ActionEvent event){
        sucessLabel.setText("");
        errorLabel.setText("");
        String newpass = newPassField.getText();
        String agiannewpass = againNewPassField.getText();
        if (newpass.length() < 8 || agiannewpass.length() < 8)
            errorLabel.setText("รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร");
        else if (!newPassField.getText().equals(againNewPassField.getText()))
            errorLabel.setText("รหัสผ่านทั้งสองข่องต้องเหมือนกัน");
        else {
            try {
                User newSession = User.changePass(condo.getSession().getUsername(), newpass);
                if( newSession == null )
                    throw new FileNotFoundException();
                condo.setSession(newSession);
                sucessLabel.setText("เปลี่ยนรหัสผ่านเรียบร้อย");
            } catch (FileNotFoundException e){
                errorLabel.setText("ระบบ ERROR : ไม่สามารถเปลี่ยนรหัสผ่านได้");
            }
        }
    }

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/staff.fxml", condo.getSession());
    }
}

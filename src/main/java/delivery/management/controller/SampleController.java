package delivery.management.controller;

import delivery.management.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SampleController extends Controller
{
    @FXML PasswordField passwordField;
    @FXML TextField usernameField;
    @FXML Button loginButton, registerButton, creditButton, instructionButton;
    @FXML Label errorLabel;

    @FXML public void handleLoginButtonAction(ActionEvent event) throws IOException{
        if ( checkLogin(usernameField.getText(),passwordField.getText()) ){
            System.out.println("Login complete");
            switch (condo.getSession().getPermission()){
                case 0:
                    handleNextPage(event, "/view/user.fxml", condo.getSession());
                    break;
                case 999:
                    handleNextPage(event, "/view/admin.fxml", condo.getSession());
                    break;
                default:
                    handleNextPage(event, "/view/staff.fxml", condo.getSession());
                    break;
            }
        }else{
            errorLabel.setText("Username หรือ Password ไม่ถูกต้อง");
            System.out.println("Bad Login");
        }
    }

    @FXML public void handleCreditButtonAction(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/credit.fxml");
    }

    @FXML public void handleRegisterButtonAction(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/register.fxml");
    }

    @FXML public void handleInstructionButtonAction(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/guide.fxml");
    }

    private boolean checkLogin(String username, String password) {
        condo.setSession(User.checkLogin(username, password));
        if (condo.getSession() == null) return false;
        return true;
    }
}
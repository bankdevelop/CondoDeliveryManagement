package delivery.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class CreditController extends Controller{
    @FXML Button backButton;

    @FXML public void handleBackButtonAction(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/sample.fxml");
    }
}
package delivery.management.controller;

import delivery.management.model.Letter;
import delivery.management.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import delivery.management.model.Condo;

import java.io.IOException;

public class Controller
{
    protected Condo condo;

    @FXML public void initialize(){
        condo = new Condo("myCondo", 8, 10);
    }

    @FXML public void handleNextPage(ActionEvent event, String fxmlPath) throws IOException {
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(loader.load(), 800, 600));

        stage.show();
    }

    @FXML public void handleNextPage(ActionEvent event, String fxmlPath, User session) throws IOException {
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(loader.load(), 800, 600));

        Controller controller = loader.getController();
        controller.setSession(session);

        stage.show();
    }

    @FXML public void LogoutAndDestroySession(ActionEvent event) throws IOException {
        condo.setSession(null);
        handleNextPage(event, "/view/sample.fxml");
    }

    public void setSession(User session) {
        condo.setSession(session);
    }
}

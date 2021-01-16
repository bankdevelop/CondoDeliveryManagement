package delivery.management.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;

public class GuideController extends Controller {
    @FXML Button backButton;
    @FXML WebView contentView;

    @Override
    public void initialize() {
        super.initialize();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                URL url = this.getClass().getResource("/asset/gettingstart.html");
                WebEngine engine = contentView.getEngine();
                engine.load(url.toString());
            }
        });
    }

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        handleNextPage(event, "/view/sample.fxml");
    }
}
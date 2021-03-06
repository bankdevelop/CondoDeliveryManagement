package delivery.management.controller;

import delivery.management.model.Document;
import delivery.management.model.Supplies;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class DocumentViewController extends DeliveryController{
    @FXML Button backButton;
    @FXML Label roomLabel, dateLabel, sizeLabel, desLabel, priorityLabel, receiverLabel;
    @FXML ImageView image;

    @Override
    public void initialize() {
        super.initialize("ignore Delivery initialize but use Controller initialize instead");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Document dataCast = (Document) data;

                roomLabel.setText(dataCast.getRecipientRoomNumber());
                dateLabel.setText(dataCast.getDatetime());
                sizeLabel.setText(dataCast.getStringSize());
                desLabel.setText(dataCast.getSenderInformation());
                priorityLabel.setText(dataCast.getStringPriority());
                receiverLabel.setText(dataCast.getReceviver());
                try {
                    image.setImage(new Image("file:/"+dataCast.getImage()));
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });
    }

    @Override
    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        if (this.cache != null){
            try {
                handleNextPage(event, this.cache, condo.getSession());
            } catch (Exception e) {
                System.out.println("ไม่สามารถเปลี่ยนเพจได้");
            }
        } else {
            handleNextPage(event, "/view/document.fxml", condo.getSession());
        }
    }
}

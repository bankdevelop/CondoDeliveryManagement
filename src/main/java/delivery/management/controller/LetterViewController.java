package delivery.management.controller;

import delivery.management.model.Letter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class LetterViewController extends DeliveryController{
    @FXML Button backButton;
    @FXML Label roomLabel, dateLabel, sizeLabel, desLabel, receiverLabel;
    @FXML ImageView image;

    @Override
    public void initialize() {
        super.initialize("ignore Delivery initialize but use Controller initialize instead");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Letter letter = (Letter) data;

                roomLabel.setText(letter.getRecipientRoomNumber());
                dateLabel.setText(letter.getDatetime());
                sizeLabel.setText(letter.getStringSize());
                desLabel.setText(letter.getSenderInformation());
                receiverLabel.setText(letter.getReceviver());
                try {
                    image.setImage(new Image("file:/"+letter.getImage()));
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
            handleNextPage(event, "/view/letter.fxml", condo.getSession());
        }
    }
}

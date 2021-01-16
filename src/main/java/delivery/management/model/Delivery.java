package delivery.management.model;

import delivery.management.model.Interface.Datafile;
import javafx.scene.control.Button;

import java.time.LocalDateTime;

public class Delivery implements Datafile {
    private String recipientRoomNumber;
    private String senderInformation;
    private String image;
    private String datetime;
    private int typeSize;
    private String receviver;

    private Button view, receive;

    Delivery(String recipientRoomNumber, String senderInformation, String image){
        this.recipientRoomNumber = recipientRoomNumber;
        this.senderInformation = senderInformation;
        this.image = image;
        this.datetime = LocalDateTime.now().toString();
    }

    Delivery(String recipientRoomNumber, String senderInformation, String image, String datetime){
        this.recipientRoomNumber = recipientRoomNumber;
        this.senderInformation = senderInformation;
        this.image = image;
        this.datetime = datetime;
    }

    Delivery(String recipientRoomNumber, String senderInformation, String image, String datetime, String receiver){
        this.recipientRoomNumber = recipientRoomNumber;
        this.senderInformation = senderInformation;
        this.image = image;
        this.datetime = datetime;
        this.receviver = receiver;
    }

    public String getRecipientRoomNumber() {
        return recipientRoomNumber;
    }
    public String getSenderInformation() {
        return senderInformation;
    }
    public String getImage() {
        return image;
    }
    public String getDatetime() {
        return datetime;
    }
    public Button getReceive() {
        return receive;
    }
    public Button getView() {
        return view;
    }
    public int getTypeSize() {
        return typeSize;
    }
    public String getReceviver() {
        return receviver;
    }
    public void setReceive(Button receive) {
        this.receive = receive;
    }
    public void setView(Button view) {
        this.view = view;
    }
    public void setTypeSize(int typeSize) {
        this.typeSize = typeSize;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "recipientRoomNumber='" + recipientRoomNumber + '\'' +
                ", senderInformation='" + senderInformation + '\'' +
                ", image='" + image + '\'' +
                ", datetime='" + datetime +
                '}';
    }

    @Override
    public String getData() {
        return this.toString();
    }

    public String getStringSize() {
        return ""+this.typeSize;
    }
}

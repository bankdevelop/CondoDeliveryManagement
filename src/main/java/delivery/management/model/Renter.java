package delivery.management.model;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Renter {
    private static String pathRenter = "data/renter.csv";

    private String rentRoomNumber;
    private String renterName;
    private String telNumber;
    private String datetime;

    @FXML private Button controlButton;

    public Renter(String rentRoomNumber, String renterName, String telNumber, String datetime){
        this.rentRoomNumber = rentRoomNumber;
        this.renterName = renterName;
        this.telNumber = telNumber;
        this.datetime = datetime;
    }

    public static Renter[] getRentData(String roomNumber) throws FileNotFoundException {
        Renter[] renters = getRentData();
        ArrayList<Renter> temp = new ArrayList<>();

        if (renters != null) {
            for (Renter renter : renters)
                if (renter.rentRoomNumber.equals(roomNumber))
                    temp.add(renter);
            if (temp.size() == 0) return null;

            Renter[] result = new Renter[temp.size()];
            for (int i = 0; i < temp.size(); i++)
                result[i] = temp.get(i);
            return result;
        }else return null;
    }

    public static Renter[] getRentData() throws FileNotFoundException{
        ArrayList<Renter> renters = new ArrayList<Renter>();
        Scanner in = new Scanner(new File(pathRenter));
        String[] temp;
        Renter renter=null;

        while(in.hasNext()){
            temp = in.nextLine().trim().split(",");
            if (temp.length==4)
                renter = new Renter(temp[0], temp[1], temp[2], temp[3]);
            if (renter!=null) renters.add(renter);
        }

        int rentersLength = renters.size();
        if (rentersLength>0) {
            Renter[] result = new Renter[rentersLength];
            for (int i=0; i<rentersLength; i++)
                result[i] = renters.get(i);
            return result;
        }

        return null;
    }

    public static int getRenterAmount(String roomNumber) throws FileNotFoundException {
        Renter[] renters = getRentData(roomNumber);
        if (renters == null) return 0;
        return renters.length;
    }

    public static void removeRenter(String roomNumber, String telNumber) throws FileNotFoundException {
        Renter[] renters = getRentData();
        String temp = "";
        if (renters.length == 0) return;

        for (Renter renter:renters){
            if ( !(renter.telNumber.equals(telNumber) && renter.rentRoomNumber.equals(roomNumber)) ){
                temp+=renter.toString()+"\n";
            }
        }

        PrintStream out = new PrintStream(pathRenter);
        out.print(temp);
        out.flush();
        out.close();

        if (getRenterAmount(roomNumber) == 0) Room.setIsRented(roomNumber, false);
    }

    public static void removeRent(String roomNumber) throws FileNotFoundException{
        String temp = "";
        Renter[] renters = getRentData();
        int h = renters.length;

        for (int i=0; i<h; i++){
            Renter renter = renters[i];
            if (!renter.rentRoomNumber.equals(roomNumber)){
                temp+=renter.toString()+"\n";
            }
        }

        PrintStream out = new PrintStream(pathRenter);
        out.print(temp);
        out.flush();
        out.close();

        Room.setIsRented(roomNumber, false);
    }

    public static boolean addRenter(String roomNumber, Renter newRenter) throws FileNotFoundException{
        if (newRenter==null) return false;
        String temp = "";
        Renter[] renter = getRentData();

        if (renter != null) {
            for (int i = 0; i < renter.length; i++) {
                temp += renter[i].toString() + "\n";
                if (renter[i].rentRoomNumber.equals(roomNumber) &&
                        renter[i].telNumber.equals(newRenter.telNumber)) {
                    return false;
                }
            }
        }

        temp+=newRenter.toString();

        PrintStream out = new PrintStream(pathRenter);
        out.print(temp);
        out.flush();
        out.close();

        Room.setIsRented(roomNumber, true);
        return true;
    }

    public static boolean isRoomFull(String roomNumber) throws FileNotFoundException {
        int amountMax = Room.getRoomType(roomNumber)==1?4:2;
        int renterAmount = getRenterAmount(roomNumber);
        if (renterAmount < amountMax) return false;
        return true;
    }

    public static Renter[] returnRenter(String name, String telNumber) throws FileNotFoundException {
        Renter[] renters = getRentData();
        ArrayList<Renter> renterlist = new ArrayList<>();
        if (renters.length == 0) return null;

        for (Renter renter:renters){
            if ( renter.renterName.equals(name) && renter.telNumber.equals(telNumber) ){
                renterlist.add(renter);
            }
        }

        Renter[] temp;
        int nRenter = renterlist.size();
        if (nRenter >= 1) {
            temp = new Renter[nRenter];
            for (int i=0; i<nRenter; i++)
               temp[i] = renterlist.get(i);
            return temp;
        }
        return null;
    }

    public String getRenterName() {
        return renterName;
    }
    public String getTelNumber() {
        return telNumber;
    }
    public String getRentRoomNumber() {
        return rentRoomNumber;
    }

    public Button getColControl(){
        return controlButton;
    }
    public void setControlButton(Button controlButton) {
        this.controlButton = controlButton;
    }

    @Override
    public String toString() {
        return this.rentRoomNumber+","+this.renterName+","+this.telNumber+","+this.datetime;
    }
}
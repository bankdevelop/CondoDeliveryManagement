package delivery.management.model;

import delivery.management.model.Interface.Datafile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Received implements Datafile {
    private static String path = "data/received.csv";

    private Delivery dataValue;
    private String typeData;
    private String receiverData;
    private String receiveDatetime;

    public Received(Delivery dataValue, String receiverData, String receiveDatetime){
        this.dataValue = dataValue;
        this.typeData = dataValue.getClass().getName();
        this.receiverData = receiverData;
        this.receiveDatetime = receiveDatetime;
    }

    public Received(Delivery dataValue, String typeData ,String receiverData, String receiveDatetime){
        this.dataValue = dataValue;
        this.typeData = typeData;
        this.receiverData = receiverData;
        this.receiveDatetime = receiveDatetime;
    }

    public static Received[] getReceivedData() throws FileNotFoundException {
        Scanner in = new Scanner(new File(path));
        ArrayList<Received> temp = new ArrayList<>();
        while(in.hasNext()){
            String[] data = in.nextLine().split(",");
            int lengthData = data.length;
            if (lengthData >= 4) {
                String typeClass = data[0];
                String receiverData = data[1];
                String receiveDatatime = data[2];
                Delivery tempData = null;

                if (typeClass.equals("Document") && lengthData == 10) {
                    tempData = new Document(data[3], data[4], data[5], Integer.parseInt(data[6]),
                            Integer.parseInt(data[7]), data[8], data[9]);
                }else if (typeClass.equals("Letter") && lengthData == 9){
                    tempData = new Letter(data[3], data[4], data[5], Integer.parseInt(data[6]), data[7], data[8]);
                }else if (typeClass.equals("Supplies") && lengthData == 11){
                    tempData = new Supplies(data[3], data[4], data[5], Integer.parseInt(data[6]),
                            data[7], data[8], data[9], data[10]);
                }

                if (tempData != null){
                    temp.add(new Received(tempData, typeClass, receiverData, receiveDatatime));
                }
            }
        }
        in.close();

        Received[] result = new Received[temp.size()];
        for (int i=0; i<temp.size(); i++)
            result[i] = temp.get(i);

        return result;
    }

    public static boolean addNewReceived(Received obj) throws FileNotFoundException {
        if (obj == null) return false;
        Received[] receiveds = Received.getReceivedData();
        if (receiveds == null) return false;

        String temp = "";

        temp+=Data.getStringDataList(receiveds);
        temp+=obj.toString();

        PrintStream out = new PrintStream(path);
        out.print(temp);
        out.flush();
        out.close();
        return true;
    }

    public Delivery getDataValue() {
        return dataValue;
    }
    public String getTypeData() {
        return typeData;
    }
    public String getReceiveDatetime() {
        return receiveDatetime;
    }
    public String getReceiverData() {
        return receiverData;
    }

    @Override
    public String toString() {
        return this.getTypeData()+","+this.getReceiverData()+","+this.getReceiveDatetime()+","+this.getDataValue().toString();
    }

    @Override
    public String getData() {
        return toString();
    }

    //get For TableView
    public String getRoomNumber() {
        return this.dataValue.getRecipientRoomNumber();
    }
    public String getSenderInformation() {
        return this.dataValue.getSenderInformation();
    }
    public String getStringSize() {
        return this.dataValue.getStringSize();
    }
    public String getDatetimeData() {
        return this.dataValue.getDatetime();
    }
}

package delivery.management.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Supplies extends Delivery {
    private static String path = "data/supplies.csv";

    private String serviceName;
    private String trackingNumber;

    public Supplies(String recipientRoomNumber, String senderInformation, String image,
            int typeSize, String serviceName, String trackingNumber, String datetime, String receiver){
        super(recipientRoomNumber, senderInformation, image, datetime, receiver);
        this.serviceName = serviceName;
        this.trackingNumber = trackingNumber;
        this.setTypeSize(typeSize);
    }

    public static Supplies[] getSuppliesData() throws FileNotFoundException {
        Scanner in = new Scanner(new File(path));
        ArrayList<Supplies> temp = new ArrayList<>();
        while(in.hasNext()){
            String[] data = in.nextLine().split(",");
            if (data.length==8) {
                String room = data[0];
                String description = data[1];
                String image = data[2];
                int size = Integer.parseInt(data[3]);
                String service = data[4];
                String trackingNum = data[5];
                String date = data[6];
                String receiver = data[7];

                temp.add(new Supplies(room, description, image, size, service, trackingNum, date, receiver));
            }
        }
        in.close();

        Supplies[] result = new Supplies[temp.size()];
        for (int i=0; i<temp.size(); i++)
            result[i] = temp.get(i);

        return result;
    }

    public static boolean addNewSupplies(Supplies newSupplies) throws FileNotFoundException {
        if (newSupplies == null) return false;
        int checkHaveRoom = Room.getRoomType(newSupplies.getRecipientRoomNumber());
        if (checkHaveRoom == -1) return false;

        String temp = "";
        Supplies[] supplies = getSuppliesData();

        if (supplies != null){
            temp+=Data.getStringDataList(supplies);
            temp+=newSupplies.toString();

            PrintStream out = new PrintStream(path);
            out.print(temp);
            out.flush();
            out.close();
            return true;
        }
        return false;
    }

    public static boolean receiveSupplies(Supplies getSupplies) throws FileNotFoundException {
        String temp = "";
        Supplies[] supplies = getSuppliesData();
        if (supplies != null){
            for (Supplies supplie:supplies){
                if (!supplie.toString().equals(getSupplies.toString()))
                    temp+=supplie.toString()+"\n";
            }

            PrintStream out = new PrintStream(path);
            out.print(temp);
            out.flush();
            out.close();
            return true;
        }
        return false;
    }

    public static boolean receiveSupplies(Supplies obj, String name) throws FileNotFoundException {
        boolean result = receiveSupplies(obj);
        if (result)
            Received.addNewReceived(new Received(obj, "Supplies", name, obj.getDatetime()));
        return result;
    }

    public String getStringSize(){
        switch (this.getTypeSize()){
            case 1: return "14X20X6";
            case 2: return "17X25X9";
            case 3: return "20X30X11";
            case 4: return "22X35X14";
            default: return "not found";
        }
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String toString() {
        return this.getRecipientRoomNumber()+","+this.getSenderInformation()+","+this.getImage()+","+this.getTypeSize()+
                ","+this.getServiceName()+","+this.getTrackingNumber()+","+this.getDatetime()+","+this.getReceviver();
    }
}

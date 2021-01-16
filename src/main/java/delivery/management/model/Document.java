package delivery.management.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Document extends Delivery {
    private static String path = "data/document.csv";

    private int priority;

    public Document(String recipientRoomNumber, String senderInformation, String image,
             int typeSize, int priority, String datetime, String receiver){
        super(recipientRoomNumber, senderInformation, image, datetime, receiver);
        this.setTypeSize(typeSize);
        setPriority(priority);
    }

    public static Document[] getDocumentData() throws FileNotFoundException {
        Scanner in = new Scanner(new File(path));
        ArrayList<Document> temp = new ArrayList<>();
        while(in.hasNext()){
            String[] data = in.nextLine().split(",");
            if (data.length==7) {
                String room = data[0];
                String description = data[1];
                String image = data[2];
                int size = Integer.parseInt(data[3]);
                int priority = Integer.parseInt(data[4]);
                String date = data[5];
                String receiver = data[6];

                temp.add(new Document(room, description, image, size, priority, date, receiver));
            }
        }
        in.close();

        Document[] result = new Document[temp.size()];
        for (int i=0; i<temp.size(); i++)
            result[i] = temp.get(i);
        return result;
    }

    public static boolean addNewDocument(Document newDocument) throws FileNotFoundException {
        if (newDocument == null) return false;
        int checkHaveRoom = Room.getRoomType(newDocument.getRecipientRoomNumber());
        if (checkHaveRoom == -1) return false;

        String temp = "";
        Document[] documents = getDocumentData();

        if (documents != null){
            temp+=Data.getStringDataList(documents);
            temp+=newDocument.toString();

            PrintStream out = new PrintStream(path);
            out.print(temp);
            out.flush();
            out.close();
            return true;
        }
        return false;
    }

    public static boolean receiveDocument(Document getDocument) throws FileNotFoundException {
        String temp = "";
        Document[] documents = getDocumentData();
        if (documents != null){
            for (Document document:documents){
                if (!document.toString().equals(getDocument.toString()))
                    temp+=document.toString()+"\n";
            }

            PrintStream out = new PrintStream(path);
            out.print(temp);
            out.flush();
            out.close();
            return true;
        }
        return false;
    }

    public static boolean receiveDocument(Document obj, String name) throws FileNotFoundException {
        boolean result = receiveDocument(obj);
        if (result)
            Received.addNewReceived(new Received(obj, "Document", name, obj.getDatetime()));
        return result;
    }

    public void setPriority(int priority) {
        if(priority<0) this.priority = 0;
        if(priority>=3) this.priority = 2;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getStringPriority(){
        switch (this.getPriority()){
            case 0: return "ปกติ";
            case 1: return "ด่วน";
            case 2: return "ด่วนที่สุด";
            default: return "not found";
        }
    }

    public String getStringSize(){
        switch (this.getTypeSize()){
            case 1: return "A4";
            case 2: return "A3";
            case 3: return "A2";
            default: return "not found";
        }
    }

    @Override
    public String toString() {
        return this.getRecipientRoomNumber()+","+this.getSenderInformation()+","+this.getImage()+","+this.getTypeSize()+
                ","+this.getPriority()+","+this.getDatetime()+","+this.getReceviver();
    }
}

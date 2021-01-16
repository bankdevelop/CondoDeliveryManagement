package delivery.management.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Letter extends Delivery {
    private static String path = "data/letter.csv";

    public Letter(String recipientRoomNumber, String senderInformation, String image,
                  int typeSize){
        super(recipientRoomNumber, senderInformation, image);
        this.setTypeSize(typeSize);
    }

    public Letter(String recipientRoomNumber, String senderInformation, String image,
                  int typeSize, String datetime, String receiver){
        super(recipientRoomNumber, senderInformation, image, datetime, receiver);
        this.setTypeSize(typeSize);
    }

    public static Letter[] getLetterData() throws FileNotFoundException {
        Scanner in = new Scanner(new File(path));
        ArrayList<Letter> temp = new ArrayList<>();
        while(in.hasNext()){
            String[] data = in.nextLine().split(",");
            if (data.length==6)
                temp.add(new Letter(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5]));
        }
        in.close();

        Letter[] result = new Letter[temp.size()];
        for (int i=0; i<temp.size(); i++)
            result[i] = temp.get(i);

        return result;
    }

    public static boolean addNewLetter(Letter newLetter) throws FileNotFoundException {
        if (newLetter == null) return false;
        int checkHaveRoom = Room.getRoomType(newLetter.getRecipientRoomNumber());
        if (checkHaveRoom == -1) return false;

        String temp = "";
        Letter[] letters = getLetterData();

        if (letters != null){
            temp+=Data.getStringDataList(letters);
            temp+=newLetter.toString();

            PrintStream out = new PrintStream(path);
            out.print(temp);
            out.flush();
            out.close();
            return true;
        }
        return false;
    }

    public static boolean receiveLetter(Letter gettletter) throws FileNotFoundException {
        String temp = "";
        Letter[] letters = getLetterData();
        if (letters != null){
            for (Letter letter:letters){
                if (!letter.equal(gettletter))
                    temp+=letter.toString()+"\n";
            }

            PrintStream out = new PrintStream(path);
            out.print(temp);
            out.flush();
            out.close();

            return true;
        }
        return false;
    }

    public static boolean receiveLetter(Letter obj, String name) throws FileNotFoundException {
        boolean result = receiveLetter(obj);
        if (result)
            Received.addNewReceived(new Received(obj, "Letter", name, obj.getDatetime()));
        return result;
    }

    public boolean equal(Letter letter2){
        if (this.toString().equals(letter2.toString()))
            if (this.getTypeSize() == letter2.getTypeSize())
                return true;
        return false;
    }

    public String getStringSize(){
        switch (this.getTypeSize()){
            case 1: return "229x324";
            case 2: return "162x229";
            case 3: return "144x162";
            case 4: return "110x220";
            default: return "not found";
        }
    }

    @Override
    public String toString() {
        return this.getRecipientRoomNumber()+","+this.getSenderInformation()+","+
                this.getImage()+","+this.getTypeSize()+","+this.getDatetime()+","+this.getReceviver();
    }
}

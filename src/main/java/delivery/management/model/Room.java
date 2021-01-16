package delivery.management.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {
    private static String path = "data/room.csv";

    private String roomNumber;
    private int typeRoom;
    private boolean isRented;

    public Room(String roomNumber){
        this(roomNumber, 0);
    }

    public Room(String roomNumber, int typeRoom){
        this(roomNumber, typeRoom, false);
    }

    public Room(String roomNumber, int typeRoom, boolean available){
        this.roomNumber = roomNumber;
        this.typeRoom = typeRoom;
        this.isRented = available;
    }

    public boolean isRented(){
        return isRented;
    }

    public static Room[] getRoomData() throws FileNotFoundException {
        ArrayList<Room> rooms = new ArrayList<>();
        Scanner in = new Scanner(new File(path));
        String[] temp;

        while(in.hasNext()){
            temp = in.next().split(",");
            rooms.add(new Room(temp[0], Integer.parseInt(temp[1]),
                                                temp[2].equals("1")?true:false));
        }

        Room[] result = new Room[rooms.size()];
        for (int i=0; i<rooms.size(); i++)
            result[i] = rooms.get(i);

        return result;
    }

    public static int getRoomType(String roomNumber) throws FileNotFoundException{
        Room[] rooms = getRoomData();
        for (int i=0; i<rooms.length; i++)
            if (rooms[i].getRoomNumber().equals(roomNumber))
                return rooms[i].getTypeRoom(1);
        return -1;
    }

    public static void setIsRented(String roomNumber, boolean value) throws FileNotFoundException{
        Room[] rooms = getRoomData();
        String temp = "";
        int h = rooms.length;

        for (int i=0; i<h; i++){
            Room room = rooms[i];
            if (room.getRoomNumber().equals(roomNumber))
                temp+=room.getRoomNumber()+","+room.getTypeRoom(1)+","+(value?1:0)+"\n";
            else
                temp+=room.toString()+"\n";
        }

        PrintStream out = new PrintStream(path);
        out.print(temp);
        out.flush();
        out.close();
    }

    public static boolean addNewRoom(Room newRoom) throws FileNotFoundException {
        Room[] rooms = getRoomData();
        String temp = "";

        for (Room room : rooms) {
            if (room.getRoomNumber().equals(newRoom.getRoomNumber()))
                return false;
            temp+=room.toString()+"\n";
        }
        temp+=newRoom.toString()+"\n";

        PrintStream out = new PrintStream(path);
        out.print(temp);
        out.flush();
        out.close();
        return true;
    }

    public String getTypeRoom() {
        return typeRoom==1?"suite":"standard";
    }
    public int getTypeRoom(int i) {
        return typeRoom;
    }
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return this.getRoomNumber()+","+this.getTypeRoom(1)+","+(this.isRented()?1:0);
    }
}

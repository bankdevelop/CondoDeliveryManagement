package delivery.management.model;

public class Condo {
    private String name;
    private int floor;
    private int roomPerFloor;
    private Room[] listRoom;
    private User session;

    public Condo(String name, int floor, int roomPerFloor){
        this.name = name;
        this.floor = floor;
        this.roomPerFloor = roomPerFloor;
    }

    public void setSession(User session) {
        this.session = session;
    }
    public void setListRoom(Room[] listRoom) {
        this.listRoom = listRoom;
    }

    public User getSession() {
        return session;
    }
    public Room[] getListRoom() {
        return listRoom;
    }
}

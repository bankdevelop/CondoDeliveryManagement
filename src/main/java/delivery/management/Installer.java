package delivery.management;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class Installer {
    private boolean isRoomInstallBefore = true;
    private String dataDirectory = "data";
    private String listFile[] = {"document.csv", "letter.csv", "renter.csv", "room.csv", "supplies.csv", "user.csv", "received.csv"};

    public boolean install() {
        boolean isSucess = this.initEnvironment();
        if (!isSucess) return false;

        try {
            this.initRoomData(8, 10);
            this.initSuperUser("admin","12345678");
            this.createInstallChecker("Delivery Management version:1.0");
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
        return true;
    }

    public boolean initEnvironment() {
        File dataDir = new File(dataDirectory);
        if (!dataDir.exists()) dataDir.mkdir();
        for (String dataFile : listFile){
            File file = new File(getFilePath(dataFile));
            if (!file.exists()) {
                if (dataFile.equals("room.csv")) isRoomInstallBefore = false;
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.err.println("ไม่สามารถสร้าง dataFile ได้อย่างถูกต้อง");
                    return false;
                }
            }
        }
        return true;
    }

    private void initRoomData(int floor, int roomPerFloor) throws IOException {
        String roomDirFile = getFilePath("room.csv");
        File roomFile = new File(roomDirFile);
        FileWriter fw = new FileWriter(roomFile);
        BufferedWriter writer = new BufferedWriter(fw);

        for (int i=1; i<=floor; i++)
            for (int j=0; j<roomPerFloor; j++) {
                String buildingNumber = Integer.toString(1);
                String floorStr = Integer.toString(i);
                String room = Integer.toString(j);

                String dataLine = buildingNumber+floorStr+room+",0,0";
                writer.write(dataLine);
                writer.newLine();
            }

        writer.close();
    }

    private void initSuperUser(String username, String password) throws IOException {
        String roomDirFile = getFilePath("user.csv");
        File roomFile = new File(roomDirFile);
        FileWriter fw = new FileWriter(roomFile);
        BufferedWriter writer = new BufferedWriter(fw);

        writer.write("Admin SuperUser"+","+username+","+password+",999,null");
        writer.newLine();

        writer.close();
    }

    private void createInstallChecker(String comment) throws IOException {
        File file = new File(getFilePath("install.txt"));
        file.createNewFile();

        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);

        writer.write(comment);
        writer.close();
    }

    public boolean isInstall(){
        File file = new File(getFilePath("install.txt"));
        return file.exists();
    }

    private String getFilePath(String filename){
        return Paths.get(dataDirectory, filename).toString();
    }
}

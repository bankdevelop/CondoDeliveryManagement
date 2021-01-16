package delivery.management.model;

import delivery.management.model.Interface.Datafile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class User implements Datafile {
    private static String path = "data/user.csv";

    private String name;
    private String username;
    private String password;
    private int permission;
    private String dateTime;

    private User(String name, String username, String password, int permission){
        this(name, username, password, permission, LocalDateTime.now().toString());
    }

    private User(String name,String username, String password, int permission, String dateTime){
        this.name = name;
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.dateTime = dateTime;
    }

    public boolean setPermission(int permission, User changer) {
        if (permission >= 999 || permission < 0) return false;
        if (changer.permission == 999)
            this.permission = permission%2;
        return  true;
    }

    public static User checkLogin(String username, String password) {
        try {
            LocalDateTime myTime = LocalDateTime.now();
            String tempOut = "";
            User result = null;

            Scanner in = new Scanner(new File(path));
            while(in.hasNext()){
                String[] temp = in.nextLine().split(",");

                if(result == null && temp[1].equals(username) && temp[2].equals(password)){
                    result = new User(temp[0], temp[1], temp[2], Integer.parseInt(temp[3]), temp[4]);
                    tempOut += temp[0] +","+ temp[1]+","+ temp[2] +","+ temp[3] +","+ myTime.toString()+"\n";
                }else{
                    tempOut += temp[0] +","+ temp[1] +","+ temp[2] +","+ temp[3] +","+ temp[4]+"\n";
                }
            }

            in.close();

            FileOutputStream fout = new FileOutputStream(path);
            PrintStream out = new PrintStream(fout);
            out.print(tempOut);
            out.flush();
            out.close();
            return result;

        }catch (IOException error){
            System.out.println(error);
        }
        return null;
    }

    public static User[] getUserData() throws FileNotFoundException {
        Scanner in = new Scanner(new File(path));
        ArrayList<User> temp = new ArrayList<>();
        while(in.hasNext()){
            String[] data = in.nextLine().split(",");
            if (data.length==5)
                temp.add(new User(data[0], data[1], null, Integer.parseInt(data[3]), data[4]));
        }
        in.close();

        User[] result = new User[temp.size()];
        for (int i=0; i<temp.size(); i++)
            result[i] = temp.get(i);

        return result;
    }

    public static User[] getUserData(int g) throws FileNotFoundException {
        Scanner in = new Scanner(new File(path));
        ArrayList<User> temp = new ArrayList<>();
        while(in.hasNext()){
            String[] data = in.nextLine().split(",");
            if (data.length==5)
                temp.add(new User(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4]));
        }
        in.close();

        User[] result = new User[temp.size()];
        for (int i=0; i<temp.size(); i++)
            result[i] = temp.get(i);

        return result;
    }

    private static String getUserDataString() throws FileNotFoundException{
        String temp="";
        Scanner in = new Scanner(new File(path));
        while (in.hasNext()) temp+=in.nextLine()+"\n";
        in.close();
        return temp;
    }

    public static User registerUser(String name, String user, String pass, int typePermission) throws FileNotFoundException{
        int permission = typePermission==1?1:0;
        User[] listUser = User.getUserData();
        for (int i=0; i<listUser.length; i++){
            if (user.equals(listUser[i].getUsername()))
                return null;
        }
        String oldData = getUserDataString();
        PrintStream out = new PrintStream(path);
        out.append(oldData+name+","+user+","+pass+","+permission+","+"null");
        out.close();
        return new User(name, user, pass, permission, "null");
    }

    public static User changePass(String user, String newPass) throws FileNotFoundException{
        User[] listUser = User.getUserData(1);
        User result = null;
        for (int i=0; i<listUser.length; i++){
            if (listUser[i].getUsername().equals(user)) {
                listUser[i].password = newPass;
                result = listUser[i];
            }
        }
        String oldData = Data.getStringDataList(listUser);
        PrintStream out = new PrintStream(path);
        out.append(oldData);
        out.close();
        return result;
    }

    public int getPermission() {
        return permission;
    }
    public String getUsername() {
        return username;
    }
    public String getDateTime() {
        return dateTime;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name+","+username+","+password+","+permission+","+dateTime;
    }

    @Override
    public String getData() {
        return toString();
    }
}

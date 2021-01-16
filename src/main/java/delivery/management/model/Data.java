package delivery.management.model;

import delivery.management.model.Interface.Datafile;

public class Data {
    public static String getStringDataList(Datafile data[]){
        String temp = "";
        if (data != null)
            for (Datafile d : data) {
                temp += d.getData() + "\n";
            }
        return temp;
    }
}

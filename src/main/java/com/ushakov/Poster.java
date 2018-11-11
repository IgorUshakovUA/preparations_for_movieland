package com.ushakov;

public class Poster {
    int id;
    String picturePath;

    @Override
    public String toString() {
        return "INSERT INTO poster( id, picturePath) VALUES( " + id + ", '" + picturePath + "');";
    }
}

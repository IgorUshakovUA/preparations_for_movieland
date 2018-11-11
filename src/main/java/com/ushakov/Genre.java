package com.ushakov;

public class Genre {
    int id;
    String name;

    @Override
    public String toString() {
        return "INSERT INTO genre(id, name) VALUES(" + id + ", '" + name + "');";
    }
}

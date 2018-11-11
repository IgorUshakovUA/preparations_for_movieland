package com.ushakov;

public class Country {
    int id;
    String name;

    @Override
    public String toString() {
        return "INSERT INTO country (id, name) VALUES(" + id + ", '" + name + "');";
    }
}

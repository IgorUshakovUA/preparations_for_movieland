package com.ushakov;

public class User {
    int id;
    String name;
    String email;
    String nickName;

    @Override
    public String toString() {
        return  "INSERT INTO app_user (id, name, email, nickName) VALUES ("+id+", '"+name+"', '"+email+"', '"+nickName+"');";
    }
}

package com.example.andrey.client1.entities;

public class User {
    private String id;
    private String name;
    private String password;
    private String userRole;

    public User(String name, String password){
        this.userRole = "guest";
        this.name = name;
        this.password = password;
    }
}

package com.example.andrey.client1.entities;

public class User {
    private String id;
    private String name;
    private String password;
    private String userRole;
    public static String USER_ROLE = "userRole";
    public static String ADMIN_ROLE = "adminRole";
    public static String GUEST_ROLE = "guestRole";

    public User(String name, String password){
        this.userRole = "guestRole";
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserRole() {
        return userRole;
    }
}

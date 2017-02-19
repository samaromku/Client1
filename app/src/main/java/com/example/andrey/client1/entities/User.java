package com.example.andrey.client1.entities;

public class User {
    private int id;
    private String login;
    private String password;
    private String userRole;
    private String fio;
    public static String USER_ROLE = "userRole";
    public static String ADMIN_ROLE = "adminRole";
    public static String GUEST_ROLE = "guestRole";

    public User(String name, String password){
        this.userRole = "guestRole";
        this.login = name;
        this.password = password;
    }

    public String getFio() {
        return fio;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getUserRole() {
        return userRole;
    }
}

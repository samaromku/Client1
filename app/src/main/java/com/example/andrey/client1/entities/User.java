package com.example.andrey.client1.entities;


public class User {
    private int id;
    private String login;
    private String password;
    private String FIO;
    private String role;
    private String telephone;
    private String email;
    private UserRole userRole;

    public static String USER_ROLE = "userRole";
    public static String ADMIN_ROLE = "adminRole";
    public static String GUEST_ROLE = "guestRole";

    public User(){}

    public User(String name, String password){
        this.login = name;
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFIO() {
        return FIO;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public User(String login, String password, String FIO, String role, String telephone, String email) {
        this.login = login;
        this.password = password;
        this.FIO = FIO;
        this.role = role;
        this.telephone = telephone;
        this.email = email;
    }
}

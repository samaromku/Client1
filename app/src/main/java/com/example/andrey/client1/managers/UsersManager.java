package com.example.andrey.client1.managers;

import com.example.andrey.client1.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UsersManager {
    private User user;
    private List<User> users;
    public static final UsersManager INSTANCE = new UsersManager();

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user){
        users.add(user);
    }

    public void addAll(List<User> userList) {
        users.addAll(userList);
    }

    public void removeAll(){
        if(users.size()>0){
            users.clear();
        }
    }

    public User getUserByUserName(String userName){
        for(User u:users){
            if(u.getLogin().equals(userName))
                return u;
        }
        return null;
    }


    public User getUserById(int id){
        for(User u:users){
            if(u.getId()==id){
                return u;
            }
        }
        return null;
    }

    private UsersManager(){
        users = new ArrayList<>();
    }

    public int maxId(){
        int max=0;
        for(User u:users){
            if(u.getId()>max)
                max = u.getId();
        }
        return max;
    }

}

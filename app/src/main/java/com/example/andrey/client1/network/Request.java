package com.example.andrey.client1.network;

import com.example.andrey.client1.entities.Comment;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.entities.UserCoords;
import com.example.andrey.client1.entities.UserRole;

public class Request {
    private String request;
    private User user;
    private Task task;
    private UserRole userRole;
    private Comment comment;
    private UserCoords userCoords;
    public static final String ADD_TASK_TO_SERVER = "add_task_to_server";
    public static final String WANT_SOME_COMMENTS = "give_me_comments_by_task_id";
    public static final String CHANGE_PERMISSION_PLEASE = "change_permission_please";
    public static final String GIVE_ME_ADDRESSES_PLEASE = "give_me_addresses_please";
    public static final String ADD_NEW_USER = "add_new_user";
    public static final String ADD_NEW_ROLE = "add_new_role";
    public static final String ADD_COORDS = "add_coords";

    public Request(Task task, String request){
        this.task = task;
        this.request = request;
    }

    public Request(UserCoords userCoords, String request){
        this.userCoords = userCoords;
        this.request = request;
    }

    public Request(Comment comment, String request){
        this.comment = comment;
        this.request = request;
    }

    public Request(String request){
        this.request = request;
    }

    public Request(User user, String request){
        this.user = user;
        this.request = request;
    }

    public Request(UserRole userRole, String request){
        this.userRole = userRole;
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}

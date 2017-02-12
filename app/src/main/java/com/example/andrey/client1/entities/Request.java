package com.example.andrey.client1.entities;

public class Request {
    private String request;
    private User user;
    private Task task;
    public static final String ADD_TASK_TO_SERVER = "add_task_to_server";

    public Request(Task task, String request){
        this.task = task;
        this.request = request;
    }

    public Request(User user, String request){
        this.user = user;
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}

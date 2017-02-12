package com.example.andrey.client1.entities;

import java.util.List;

public class Response {
    private String response;
    private User user;
    private Task task;
    private List<Task> taskList;
    private List<User> userList;
    public static final String ADD_TASKS_TO_USER = "addTasksToUser";

    public List<Task> getTaskList() {
        return taskList;
    }

    public Response(User user, List<Task>taskList, String response) {
        this.user = user;
        this.taskList = taskList;
        this.response = response;
    }

    public Response(List<User> users, List<Task>taskList, String response) {
        this.userList = users;
        this.taskList = taskList;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public User getUser() {
        return user;
    }

    public Task getTask() {
        return task;
    }

    public List<User> getUserList() {
        return userList;
    }
}

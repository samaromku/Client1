package com.example.andrey.client1.entities;

public class Comment{
    private int id;
    private String ts;
    private String body;
    private int userId;
    private int taskId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comment(){}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Comment(String ts, String body, int userId, int taskId) {
        this.taskId = taskId;
        this.userId = userId;
        this.ts = ts;
        this.body = body;
    }
}

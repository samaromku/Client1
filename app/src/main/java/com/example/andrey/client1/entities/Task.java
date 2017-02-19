package com.example.andrey.client1.entities;

import java.util.List;

public class Task {
    private int id;
    private String created;
    private String importance;
    private String body;
    private String status;
    private String type;
    private String doneTime;
    private int userId;
    private String address;
    private String orgName;
    private List<Comment> comments;
    private Comment comment;

    public static final String NEW_TASK = "new_task";
    public static final String DISTRIBUTED_TASK = "distributed";
    public static final String DOING_TASK = "doing";
    public static final String DONE_TASK = "done";
    public static final String DISAGREE_TASK = "disagree";
    public static final String CONTROL_TASK = "control";
    public static final String NEED_HELP = "need_help";

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String doneTime) {
        this.doneTime = doneTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Task(int id, String created, String importance, String body, String status, String type, String doneTime, int userId, String address, String orgName) {
        this.id = id;
        this.created = created;
        this.importance = importance;
        this.body = body;
        this.type = type;
        this.status = status;
        this.doneTime = doneTime;
        this.userId = userId;
        this.address = address;
        this.orgName = orgName;
    }
}

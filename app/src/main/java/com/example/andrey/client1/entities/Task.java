package com.example.andrey.client1.entities;

public class Task {
    private String id;
    private String title;
    private String body;
    private String created;
    private boolean done;
    private String doneTime = "-1";
    private String user;
    private String adress;
    private String telephone;
    private String commentFromUser;

    public String getCommentFromUser() {
        return commentFromUser;
    }

    public void setCommentFromUser(String commentFromUser) {
        this.commentFromUser = commentFromUser;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String doneTime) {
        this.doneTime = doneTime;
    }

    public Task(String title, String body, String created, boolean done, String doneTime, String user, String adress, String telephone, String comment) {
        this.id = String.valueOf((int)(Math.random()*100));
        this.title = title;
        this.body = body;
        this.created = created;
        this.done = done;
        this.doneTime = doneTime;
        this.user = user;
        this.adress = adress;
        this.telephone = telephone;
        this.commentFromUser = comment;
    }
}

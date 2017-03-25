package com.example.andrey.client1.entities;

public class Task {
    private int id;
    private String created;
    private String importance;
    private String body;
    private String status;
    private String type;
    private String doneTime;
    private int userId;
    private int addressId;
    private String address;
    private String orgName;

    public static final String NEW_TASK = "новое задание";
    public static final String DISTRIBUTED_TASK = "распределено";
    public static final String DOING_TASK = "выполняется";
    public static final String DONE_TASK = "выполнено";
    public static final String DISAGREE_TASK = "отказ";
    public static final String CONTROL_TASK = "контроль";
    public static final String NEED_HELP = "нужна помощь";


    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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

    public Task(int id, String created, String importance, String body, String status, String type, String doneTime, int userId, int addressId) {
        this.id = id;
        this.created = created;
        this.importance = importance;
        this.body = body;
        this.type = type;
        this.status = status;
        this.doneTime = doneTime;
        this.userId = userId;
        this.addressId = addressId;
    }

    public Task(){}
}

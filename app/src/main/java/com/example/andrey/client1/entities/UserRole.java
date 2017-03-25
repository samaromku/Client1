package com.example.andrey.client1.entities;


public class UserRole {
    private int id;
    private boolean makeNewUser;
    private boolean makeTasks;
    private boolean correctionTask;
    private boolean makeAddress;
    private boolean watchAddress;
    private boolean correctionStatus;
    private boolean makeExecutor;
    private boolean correctionExecutor;
    private boolean watchTasks;
    private boolean commentTasks;
    private boolean changePassword;
    private int userId;

    public static final String USER_ROLE = "userRole";
    public static final String MANAGER_ROLE = "managerRole";
    public static final String ADMIN_ROLE = "adminRole";

    public UserRole(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMakeNewUser() {
        return makeNewUser;
    }

    public void setMakeNewUser(boolean makeNewUser) {
        this.makeNewUser = makeNewUser;
    }

    public boolean isMakeTasks() {
        return makeTasks;
    }

    public void setMakeTasks(boolean makeTasks) {
        this.makeTasks = makeTasks;
    }

    public boolean isCorrectionTask() {
        return correctionTask;
    }

    public void setCorrectionTask(boolean correctionTask) {
        this.correctionTask = correctionTask;
    }

    public boolean isMakeAddress() {
        return makeAddress;
    }

    public void setMakeAddress(boolean makeAddress) {
        this.makeAddress = makeAddress;
    }

    public boolean isWatchAddress() {
        return watchAddress;
    }

    public void setWatchAddress(boolean watchAddress) {
        this.watchAddress = watchAddress;
    }

    public boolean isCorrectionStatus() {
        return correctionStatus;
    }

    public void setCorrectionStatus(boolean correctionStatus) {
        this.correctionStatus = correctionStatus;
    }

    public boolean isMakeExecutor() {
        return makeExecutor;
    }

    public void setMakeExecutor(boolean makeExecutor) {
        this.makeExecutor = makeExecutor;
    }

    public boolean isCorrectionExecutor() {
        return correctionExecutor;
    }

    public void setCorrectionExecutor(boolean correctionExecutor) {
        this.correctionExecutor = correctionExecutor;
    }

    public boolean isWatchTasks() {
        return watchTasks;
    }

    public void setWatchTasks(boolean watchTasks) {
        this.watchTasks = watchTasks;
    }

    public boolean isCommentTasks() {
        return commentTasks;
    }

    public void setCommentTasks(boolean commentTasks) {
        this.commentTasks = commentTasks;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserRole(int id, boolean makeNewUser, boolean makeTasks, boolean correctionTask, boolean makeAddress, boolean watchAddress, boolean correctionStatus, boolean makeExecutor, boolean correctionExecutor, boolean watchTasks, boolean commentTasks, boolean changePassword, int userId) {
        this.id = id;
        this.makeNewUser = makeNewUser;
        this.makeTasks = makeTasks;
        this.correctionTask = correctionTask;
        this.makeAddress = makeAddress;
        this.watchAddress = watchAddress;
        this.correctionStatus = correctionStatus;
        this.makeExecutor = makeExecutor;
        this.correctionExecutor = correctionExecutor;
        this.watchTasks = watchTasks;
        this.commentTasks = commentTasks;
        this.changePassword = changePassword;
        this.userId = userId;
    }
}

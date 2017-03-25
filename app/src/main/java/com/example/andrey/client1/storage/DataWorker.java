package com.example.andrey.client1.storage;

import android.util.Log;

import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.CommentsManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Response;

public class DataWorker extends Thread{
    private JsonParser parser = new JsonParser();
    private int userId;
    private String userName;
    private boolean isUserRoleChanged = false;
    private UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    private TasksManager tasksManager = TasksManager.INSTANCE;
    private CommentsManager commentsManager = CommentsManager.INSTANCE;
    private UsersManager usersManager = UsersManager.INSTANCE;
    private AddressManager addressManager = AddressManager.INSTANCE;
    private boolean isStopped = false;
    private String response;

    public DataWorker(String response){
        this.response = response;
    }

    public DataWorker(){}

    public boolean isUserRoleChanged() {
        return isUserRoleChanged;
    }

    public void setUserRoleChanged(boolean userRoleChanged) {
        isUserRoleChanged = userRoleChanged;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        Response responseFromServer = parser.parseFromServerUserTasks(response);
        if (response != null && responseFromServer.getResponse() != null) {
            switch (responseFromServer.getResponse()) {

                case Response.ADD_TASKS_TO_USER:
                    Client.INSTANCE.setAuth(true);
                    tasksManager.addUnique(responseFromServer.getTaskList());
                    usersManager.setUser(responseFromServer.getUser());
                    usersManager.addAll(responseFromServer.getUserList());
                    userRolesManager.addUserRole(responseFromServer.getUser().getUserRole());
                    userRolesManager.setUserRole(userRolesManager.getRoleByUserId(responseFromServer.getUser().getId()));
                    setUserId(responseFromServer.getUser().getId());
                    return;

                case Response.ADD_ACTION_ADMIN:
                    Client.INSTANCE.setAuth(true);
                    Log.i("data", "добавляем админа");
                    tasksManager.addUnique(responseFromServer.getTaskList());
                    usersManager.addAll(responseFromServer.getUserList());
                    for (User u : usersManager.getUsers()) {
                        userRolesManager.addUserRole(u.getUserRole());
                        if (u.getLogin().equals(getUserName())) {
                            setUserId(u.getId());
                            usersManager.setUser(u);
                        }
                    }
                    userRolesManager.setUserRole(userRolesManager.getRoleByUserId(userId));
                    isStopped = true;
                    return;

                case Response.ADD_COMMENTS:
                    commentsManager.addAll(responseFromServer.getComments());
                    return;

                case Response.UPDATE_USER_ROLE_SUCCESS:
                    userRolesManager.updateUserRole(userRolesManager.getUpdateUserRole());
                    setUserRoleChanged(true);
                    return;

                case Response.INSERT_USER_ROLE_SUCCESS:
                    userRolesManager.addUserRole(userRolesManager.getCreateNewUserRole());
                    return;

                case Response.ADD_TASK_SUCCESS:
                    tasksManager.addTask(tasksManager.getTask());
                    return;

                case Response.ADD_ADDRESSES_TO_USER:
                    addressManager.addAll(responseFromServer.getAddresses());
                    System.out.println(addressManager.getAddresses().size()+ " размер");
                    return;
                case Response.ADD_COMMENT_SUCCESS:
                    commentsManager.addComment(commentsManager.getComment());
                    tasksManager.updateTask(tasksManager.getStatus(), tasksManager.getTask().getId());
                    return;

                case Response.SUCCESS_ADD_USER:
                    usersManager.addUser(usersManager.getUser());
                    return;

                case Response.GET_AWAY_GUEST:
                    Client.INSTANCE.setAuth(false);
                    return;

                default:
                    return;
            }
        }
    }
}

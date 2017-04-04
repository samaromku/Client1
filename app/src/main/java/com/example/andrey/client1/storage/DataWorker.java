package com.example.andrey.client1.storage;

import android.util.Log;

import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.CommentsManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UserCoordsManager;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Response;

public class DataWorker extends Thread{
    private JsonParser parser = new JsonParser();
    private boolean isUserRoleChanged = false;
    private UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    private TasksManager tasksManager = TasksManager.INSTANCE;
    private CommentsManager commentsManager = CommentsManager.INSTANCE;
    private UsersManager usersManager = UsersManager.INSTANCE;
    private AddressManager addressManager = AddressManager.INSTANCE;
    private UserCoordsManager userCoordsManager = UserCoordsManager.INSTANCE;
    private boolean isStopped = false;
    private String response;
    private Client client = Client.INSTANCE;
    public static final String TAG = "Dataworker";

    public DataWorker(String response){
        this.response = response;
    }

    public void setUserRoleChanged(boolean userRoleChanged) {
        isUserRoleChanged = userRoleChanged;
    }

    @Override
    public synchronized void run() {
        Response responseFromServer = parser.parseFromServerUserTasks(response);
        if (response != null && responseFromServer.getResponse() != null) {
            switch (responseFromServer.getResponse()) {

                case Response.ADD_TASKS_TO_USER:
                    client.setAuth(true);
                    tasksManager.addUnique(responseFromServer.getTaskList());
                    tasksManager.removeDone();
                    usersManager.setUser(responseFromServer.getUser());
                    usersManager.addAll(responseFromServer.getUserList());
                    userRolesManager.addUserRole(responseFromServer.getUser().getUserRole());
                    userRolesManager.setUserRole(userRolesManager.getRoleByUserId(responseFromServer.getUser().getId()));
                    return;

                case Response.ADD_ACTION_ADMIN:
                    client.setAuth(true);
                    Log.i("data", "добавляем админа");
                    tasksManager.addUnique(responseFromServer.getTaskList());
                    usersManager.addAll(responseFromServer.getUserList());
                    for (User u : usersManager.getUsers()) {
                        userRolesManager.addUserRole(u.getUserRole());
                        if (u.getLogin().equals(usersManager.getUser().getLogin())) {
                            usersManager.setUser(u);
                        }
                    }

                    User user = usersManager.getUser();
                    userRolesManager.setUserRole(userRolesManager.getRoleByUserId(user.getId()));
                    return;

                case Response.ADD_COMMENTS:
                    commentsManager.addAll(responseFromServer.getComments());
                    return;

                case Response.UPDATE_USER_ROLE_SUCCESS:
                    userRolesManager.updateUserRole(userRolesManager.getUpdateUserRole());
                    setUserRoleChanged(true);
                    return;

                case Response.ADD_LATEST_USER_COORDS:
                    userCoordsManager.addAll(responseFromServer.getUserCoordsList());
                    return;

                case Response.INSERT_USER_ROLE_SUCCESS:
                    userRolesManager.addUserRole(userRolesManager.getCreateNewUserRole());
                    return;

                case Response.SUCCESS_REMOVE_TASK:
                    tasksManager.removeTask(tasksManager.getRemoveTask());
                    return;

                case Response.SUCCESS_REMOVE_USER:
                    usersManager.removeUser(usersManager.getRemoveUser());

                case Response.ADD_TASK_SUCCESS:
                    tasksManager.addTask(tasksManager.getTask());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;

                case Response.ADD_ADDRESSES_TO_USER:
                    addressManager.addAll(responseFromServer.getAddresses());
                    System.out.println(addressManager.getAddresses().size()+ " размер");
                    return;
                case Response.ADD_COMMENT_SUCCESS:
                    commentsManager.addComment(commentsManager.getComment());
                    tasksManager.updateTask(tasksManager.getStatus(), tasksManager.getTask().getId());
                    return;

                case Response.SUCCESS_UPDATE_TASK:
                    tasksManager.updateTask(tasksManager.getTask());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;

                case Response.SUCCESS_ADD_USER:
                    User newUser = responseFromServer.getUser();
                    usersManager.addUser(newUser);
                    userRolesManager.addUserRole(newUser.getUserRole());
                    return;

                case Response.GET_AWAY_GUEST:
                    client.setAuth(false);
                    return;

                default:
                    return;
            }
        }
    }
}

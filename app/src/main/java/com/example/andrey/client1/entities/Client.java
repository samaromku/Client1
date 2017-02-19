package com.example.andrey.client1.entities;

import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;

import com.example.andrey.client1.activities.AccountActivity;
import com.example.andrey.client1.activities.AuthActivity;
import com.example.andrey.client1.storage.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static final Client INSTANCE = new Client();
    //private static final String hostName = "192.168.0.121";  // ноут алины
    private static final String hostName = "192.168.0.186";     //мой комп
    private static final int portNumber = 60123;
    private static final String debugString = "debug";
    private Socket socket = null;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isConnected = true;
    private String data;
    private String role;
    private JsonParser parser;
    private List<Task> taskList = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private String intFromAuth;
    AccountActivity accaunt = new AccountActivity();

    public List<Comment> getComments() {
        return comments;
    }

    public String getIntFromAuth() {
        return intFromAuth;
    }

    public void setIntFromAuth(String intFromAuth) {
        this.intFromAuth = intFromAuth;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getRole() {
        return role;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void start() {
        new Thread() {
            public void run() {
                try {
                        Log.i(debugString, "try connect server");
                        socket = new Socket(hostName, portNumber);
                        Log.i(debugString, "connect success");
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(isConnected) {
                        while ((data = reader.readLine()) != null) {
                            System.out.println(data);
                            workingWithData(data);
                        }
                    }
                } catch (IOException e) {
                    Log.e(debugString, e.getMessage());
                }
            }
        }.start();
    }

    public void stop() throws IOException {
        if(socket!=null && reader!=null && writer!=null) {
            writer.close();
            reader.close();
            socket.close();
        }
        isConnected = false;
    }

    public void sendMessage(final String message) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        if(socket!=null) {
                            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.write(message);
                            writer.newLine();
                            writer.flush();
                            System.out.println("message sent");
                        }
                    } catch (
                            IOException e
                            )
                    {
                        Log.e(debugString, e.getMessage());
                    }
                }
            }).start();
    }

    private void workingWithData(String response){
        parser = new JsonParser();
        if(response!=null && !socket.isClosed()) {
            if (parser.parseFromServerUserTasks(response).getResponse() != null) {
                if (parser.parseFromServerUserTasks(response).getResponse().equals("addTasksToUser")) {
                    role = "userRole";
                    removeOldTasks();
                    taskList.addAll(parser.parseFromServerUserTasks(response).getTaskList());
                    for (int i = 0; i < taskList.size(); i++) {
                        System.out.println(taskList.get(i).getBody());
                    }
                } else if (parser.parseFromServerUserTasks(response).getResponse().equals("addActionAdmin")) {
                    role = "adminRole";
                    removeOldTasks();
                    taskList.addAll(parser.parseFromServerUserTasks(response).getTaskList());
                    users.addAll(parser.parseFromServerUserTasks(response).getUserList());
                }
                else if(parser.parseFromServerUserTasks(response).getResponse().equals("add_comments")){
                    removeOldComments();
                    comments.addAll(parser.parseFromServerUserTasks(response).getComments());
                }
                else if(parser.parseFromServerUserTasks(response).getResponse().equals("add_task_success")){
                    taskList.add(parser.parseFromServerUserTasks(response).getTask());
                }
                else if(parser.parseFromServerUserTasks(response).getResponse().equals("add_comment_success")){
                    for (int i = 0; i < taskList.size(); i++) {
                        if(taskList.get(i).getId()==parser.parseFromServerUserTasks(response).getTask().getId()){
                            taskList.remove(i);
                        }
                    }
                    taskList.add(parser.parseFromServerUserTasks(response).getTask());
                }
                else {
                    guestEnter();
                }
            }
//            else {
//                guestEnter();
//            }
        }
        else
        {
            guestEnter();
        }
    }

    private void guestEnter(){
        role = "guestRole";
        removeOldTasks();
        taskList.add(new Task(0,"Guest", "Not registry system", null, null, null, null, 0, null, null));
    }

    private void removeOldTasks(){
        System.out.println("auth intent: " + intFromAuth);
        if(intFromAuth!=null) {
            if (taskList.size() > 0) {
                taskList.clear();
            }
            if(users.size()>0){
                users.clear();
            }
        }
    }

    private void removeOldComments(){
        System.out.println("delete old comments");
        System.out.println(comments.size());
        if(comments.size()>0){
            comments.clear();
        }
    }

}

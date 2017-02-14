package com.example.andrey.client1.entities;

import android.util.Log;

import com.example.andrey.client1.activities.AccountActivity;
import com.example.andrey.client1.activities.CreateTaskActivity;
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
    //private static final String hostName = "192.168.0.121";
    private static final String hostName = "192.168.0.186";
    private static final int portNumber = 60123;
    private static final String debugString = "дебаг";
    private Socket socket = null;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isConnected = true;
    private String data;
    private String role;
    private JsonParser parser;
    private List<Task> taskList = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private String intFromAuth;

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
                        Log.i(debugString, "пытаемся подключиться к серверу");
                        socket = new Socket(hostName, portNumber);
                        Log.i(debugString, "удалось подключиться к серверу");
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
                            System.out.println("сообщение отправлено 1");
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
        //System.out.println("Сообщение от сервера: " + response);
        parser = new JsonParser();
        if(response!=null) {
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
                    for (int i = 0; i < taskList.size(); i++) {
                        System.out.println(taskList.get(i).getBody());
                    }
                } else {
                    role = "guestRole";
                    removeOldTasks();
                    taskList.add(new Task(0,"Вы гость", "Вы не зарегестрированы в системе", null, true, null, null, null, null, null));
                        for (int i = 0; i < taskList.size(); i++) {
                        System.out.println(taskList.get(i).getBody());
                    }
                }
            } else {
                role = "guestRole";
                removeOldTasks();
                taskList.add(new Task(0,"Вы гость", "Вы не зарегестрированы в системе", null, true, null, null, null, null, null));
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.println(taskList.get(i).getBody());
                }
            }
        }else{
            role = null;
            removeOldTasks();
            taskList.add(new Task(0,"Нет соединения", "Отсутствует подключение к серверу", null, true, null, null, null, null, null));
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println(taskList.get(i).getBody());
            }
        }
    }

    private void removeOldTasks(){
        System.out.println("интент от авторизации: " + intFromAuth);
        if(intFromAuth!=null) {
            if (taskList.size() > 0) {
                taskList.clear();
            }
        }
    }
}

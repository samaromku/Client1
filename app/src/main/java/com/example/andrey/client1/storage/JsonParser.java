package com.example.andrey.client1.storage;

import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.Response;
import com.example.andrey.client1.entities.User;
import com.google.gson.Gson;

public class JsonParser {
    String auth = "auth";
    public String parseAuthUser(String name, String password){
        return new Gson().toJson(new User(name, password));
    }

    public Response parseFromServerUserTasks(String str){
        return new Gson().fromJson(str, Response.class);
    }

    public String requestToServer(Request request){
        return new Gson().toJson(request);
    }
}

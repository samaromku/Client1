package com.example.andrey.client1.storage;

import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.network.Response;
import com.example.andrey.client1.entities.User;
import com.google.gson.Gson;

public class JsonParser {
    public Response parseFromServerUserTasks(String str){
        return new Gson().fromJson(str, Response.class);
    }

    public String requestToServer(Request request){
        return new Gson().toJson(request);
    }
}

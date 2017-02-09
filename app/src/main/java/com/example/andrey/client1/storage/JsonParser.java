package com.example.andrey.client1.storage;

import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.User;
import com.google.gson.Gson;

public class JsonParser {
    String auth = "auth";
    public String parseAuthUser(String name, String password){
        return new Gson().toJson(new User(name, password));
    }

    public String requestAuth(Request request){
        Gson gson = new Gson();
        String str = gson.toJson(request);
        return str;
    }
}

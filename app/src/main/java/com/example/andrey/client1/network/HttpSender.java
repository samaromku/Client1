package com.example.andrey.client1.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpSender {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private static final String insideUrl = "http://192.168.137.1:60123/WebApp/auth";    //ноут алины
//    private static final String insideUrl = "http://192.168.0.98:60123/WebApp/auth";    //тепломер внутренний
    private static final String outsideUrl = "http://81.23.123.230:60123/WebApp/auth";   //тепломер внешний

    //метод, который при выборе флага принимает внутренний url, при снятти внешний
    public String trueUrl(boolean isChecked){
        //если флаг проставлен
        if(isChecked) {
            //урл равен внутреннему,
//        возвращаем внутренний
            return insideUrl;
        }else
//        иначе возвращаем внешний
        return outsideUrl;
    }

    public String post(String url, String json)  {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.example.andrey.client1.storage;

import android.util.Log;

public class ThreadWorker {
    public static final ThreadWorker instance = new ThreadWorker();
    private DataWorker dataWorker;
    private static final String TAG = "ThreadWorker";
    public DataWorker getDataWorker() {
        return dataWorker;
    }

    private ThreadWorker(){}

    public void workThread(String response){
        dataWorker = new DataWorker(response);
        System.out.println(response);
        Log.i(TAG, "run: thread start");
        dataWorker.start();
//        try {
//            dataWorker.join();
//            Log.i(TAG, "workThread: " + "дождались выполнения threadUpdate");
//            Thread.sleep(300);
//            dataWorker = null;
//            Log.i(TAG, "run: thread joined and nulled");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}

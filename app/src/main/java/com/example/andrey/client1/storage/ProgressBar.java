package com.example.andrey.client1.storage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.andrey.client1.activities.AccountActivity;

public class ProgressBar  extends AsyncTask<Void, Integer, Long> {
    // объявляем диалог
    public ProgressDialog dialog;
    // контекст родительского класса


    // запускаем ProgressBar в момент запуска потока
    protected void onPreExecute(Context ctx) {
        dialog = new ProgressDialog(ctx);
        dialog.setMessage("Поиск...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    // сама работа потока, SendHttpPost() - наш долгоработающий метод
    protected Long doInBackground(Void... params) {
//        try {
//            response = SendHttpPost();
//        } catch (Exception e) {
//            e.printStackTrace();
//            response = null;
//        }
        return null;
    }

    // как только получили ответ от сервера, выключаем ProgressBar
    protected void onPostExecute(Long unused) {
        dialog.dismiss();

        // тут можно сообщить родительскому классу, что мы закончили
        // я делаю это немножко криво:
//        ((Runnable) ctx).run();

        super.onPostExecute(unused);
    }
}
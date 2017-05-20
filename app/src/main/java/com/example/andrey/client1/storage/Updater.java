package com.example.andrey.client1.storage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.ConverterMessages;

public class Updater extends AsyncTask<Void, Void, Void>{
    private ProgressDialog dialog;
    private Context context;
    private Request request;
    private Intent intent;
    private ConverterMessages converter = new ConverterMessages();

    public Updater(Context context, Request request, Intent intent){
        this.context = context;
        this.dialog = new ProgressDialog(context);
        this.request = request;
        this.intent = intent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        converter.authMessage(request);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        context.startActivity(intent);
        super.onPostExecute(aVoid);
        dialog.dismiss();
    }
}

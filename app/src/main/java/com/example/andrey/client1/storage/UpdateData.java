package com.example.andrey.client1.storage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.andrey.client1.network.Client;

public class UpdateData extends AsyncTask<Void, Void, Void> {
    private Context context;
    private RecyclerView.Adapter adapter;
    private ProgressDialog dialog;
    private Client client = Client.INSTANCE;
    public static final String TAG = "updateDate";
    private ThreadWorker threadWorker = ThreadWorker.instance;

    public UpdateData(Context context, RecyclerView.Adapter adapter){
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Загружаются данные");
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        while(true){
            if (client.getThread() != null) {
                    try {
                        client.getThread().join();
//                        if(client.getThread().isInterrupted())
                        Log.i(TAG, "doInBackground: thread joined");
                        Thread.sleep(1000);
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter.notifyDataSetChanged();
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
}


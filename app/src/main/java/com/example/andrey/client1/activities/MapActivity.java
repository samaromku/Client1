package com.example.andrey.client1.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.fragments.MapFragment;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.storage.UpdateData;

public class MapActivity extends AppCompatActivity{
    private Fragment createFragment(){
        return new MapFragment();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        UpdateData data = new UpdateData(this);
        data.execute();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if(fragment==null){
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    class UpdateData extends AsyncTask<Void, Void, Void> {
        private Context context;
        private ProgressDialog dialog;
        private Client client = Client.INSTANCE;

        public UpdateData(Context context){
            this.context = context;
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
                        Thread.sleep(500);
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
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

}

package com.example.andrey.client1.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.andrey.client1.storage.ThreadWorker;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.CommentsManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.entities.UserRole;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.OnListItemClickListener;
import com.example.andrey.client1.R;
import com.example.andrey.client1.adapter.TasksAdapter;

import java.io.IOException;

public class AccountActivity extends AppCompatActivity {
    FloatingActionButton addTask;
    private TasksAdapter adapter;
    private RecyclerView tasksList;
    private JsonParser parser = new JsonParser();
    private TasksManager tasksManager = TasksManager.INSTANCE;
    private UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    private AddressManager addressManager = AddressManager.INSTANCE;
    private static final String TAG = "AccountActivity";
    private Client client = Client.INSTANCE;

    private OnListItemClickListener clickListener = (v, position) -> {
        client.sendMessage(parser.requestToServer(new Request(tasksManager.getTasks().get(position), Request.WANT_SOME_COMMENTS)));
        startActivity(new Intent(AccountActivity.this, TaskActivity.class).putExtra("taskNumber", position));
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        getSupportActionBar().setTitle("Аккаунт");
//        startService(GpsService.newIntent(this));
//        GpsService.setServiceAlarm(this, true);
        tasksList = (RecyclerView) findViewById(R.id.tasks_list);
        addTask = (FloatingActionButton) findViewById(R.id.add_task_btn);
        addTask.setVisibility(View.INVISIBLE);

        addTask.setOnClickListener(v -> startActivity(new Intent(AccountActivity.this, CreateTaskActivity.class)));
        tasksList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(tasksManager.getTasks(), clickListener);
        tasksList.setAdapter(adapter);
        outsideIntents();
    }

    private void outsideIntents(){
        Intent intent = getIntent();
        boolean auth = intent.getBooleanExtra("fromAuth", false);
        boolean createTask = intent.getBooleanExtra("createTask", false);
        boolean changeStatus = intent.getBooleanExtra("statusChanged", false);
        boolean removeTask = intent.getBooleanExtra("removeTask", false);
        if(auth) {
            checkAuth();
            UpdateDataAccountActivity test = new UpdateDataAccountActivity(this, adapter);
            test.execute();
        } else if(createTask || changeStatus || removeTask){
            UpdateDataAccountActivity test = new UpdateDataAccountActivity(this, adapter);
            test.execute();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void buttonAddTask(){
        //кнопка добавить задание
        UserRole userRole = userRolesManager.getUserRole();
        if(userRole!=null){
            if(userRole.isMakeTasks())
                addTask.setVisibility(View.VISIBLE);
            else addTask.setVisibility(View.INVISIBLE);
        }
    }


    private class UpdateDataAccountActivity extends AsyncTask<Void, Void, Void> {
        private Context context;
        private RecyclerView.Adapter adapter;
        private ProgressDialog dialog;
        private Client client = Client.INSTANCE;
        private static final String TAG = "updateDate";

         UpdateDataAccountActivity(Context context, RecyclerView.Adapter adapter){
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
            buttonAddTask();
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }


    public void checkAuth(){
        Handler handler = new Handler();
        new Thread(() -> {
            while(true){
                if (client.getThread() != null) {
                    try {
                        client.getThread().join();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!client.isAuth()) {
                        handler.post(() -> {
                            Toast.makeText(AccountActivity.this, "вы не зарегистрированы в системе", Toast.LENGTH_SHORT).show();
                            try {
//                                stopService(new Intent(GpsService.newIntent(this)));
                                client.stop();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(AccountActivity.this, AuthActivity.class));
                        });
                        break;
                    } else
                        handler.post(() -> Toast.makeText(AccountActivity.this, "вы успешно подключились", Toast.LENGTH_SHORT).show());
                    break;
                }
            }
            }).start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                try {
                    client.setAuth(false);
                    client.stop();
                    allClear();
//                    stopService(new Intent(GpsService.newIntent(this)));
                    System.out.println("стопарим сервис");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(AccountActivity.this, AuthActivity.class));
                return true;

            case R.id.users:
                startActivity(new Intent(AccountActivity.this, UsersActivity.class));
            return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }


    private void allClear(){
        tasksManager.removeAll();
        AddressManager.INSTANCE.removeAll();
        CommentsManager.INSTANCE.removeAll();
        userRolesManager.removeAll();
        UsersManager.INSTANCE.removeAll();
    }
}

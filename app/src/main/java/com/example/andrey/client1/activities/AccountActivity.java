package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.andrey.client1.storage.UpdateData;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.CommentsManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.service.GpsService;
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


    private OnListItemClickListener clickListener = (v, position) -> {
        Client.INSTANCE.sendMessage(parser.requestToServer(new Request(tasksManager.getTasks().get(position), Request.WANT_SOME_COMMENTS)));
        startActivity(new Intent(AccountActivity.this, TaskActivity.class).putExtra("taskNumber", position));
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        getSupportActionBar().setTitle("Аккаунт");
        tasksList = (RecyclerView) findViewById(R.id.tasks_list);
        buttonAddTask();
        addTask.setOnClickListener(v -> startActivity(new Intent(AccountActivity.this, CreateTaskActivity.class)));
        tasksList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(tasksManager.getTasks(), clickListener);
        tasksList.setAdapter(adapter);
        Intent intent = getIntent();
        boolean auth = intent.getBooleanExtra("fromAuth", false);
        if(auth) {
            UpdateData test = new UpdateData(this, adapter);
            test.execute();
            checkAuth();
        }
//                startServiceGps();
    }

    private void startServiceGps(){
                Intent i = GpsService.newIntent(AccountActivity.this);
                startService(i);
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
        addTask = (FloatingActionButton) findViewById(R.id.add_task_btn);
        UserRole userRole = userRolesManager.getUserRole();
        if(userRole!=null){
            if(userRole.isMakeTasks())
                addTask.setVisibility(View.VISIBLE);
            else addTask.setVisibility(View.INVISIBLE);
        }
    }


    private void checkAuth(){
        Handler handler = new Handler();
        new Thread(() -> {
            while(true){
                if (Client.INSTANCE.getThread() != null) {
                    try {
                        Client.INSTANCE.getThread().join();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!Client.INSTANCE.isAuth()) {
                        handler.post(() -> {
                            Toast.makeText(AccountActivity.this, "вы не зарегистрированы в системе", Toast.LENGTH_SHORT).show();
                            try {
                                Client.INSTANCE.stop();
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
                    Client.INSTANCE.setAuth(false);
                    Client.INSTANCE.stop();
                    allClear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(AccountActivity.this, AuthActivity.class));
                return true;

            case R.id.users:
                startActivity(new Intent(AccountActivity.this, UsersActivity.class));
            return true;

            case R.id.address:
                getAddressesFromServer();

                startActivity(new Intent(AccountActivity.this, AddressActivity.class));
                return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void getAddressesFromServer(){
        if(addressManager.getAddresses().size()>0){
            addressManager.removeAll();
        }
        Client.INSTANCE.sendMessage(parser.requestToServer(new Request(Request.GIVE_ME_ADDRESSES_PLEASE)));
    }

    private void allClear(){
        tasksManager.removeAll();
        AddressManager.INSTANCE.removeAll();
        CommentsManager.INSTANCE.removeAll();
        userRolesManager.removeAll();
        UsersManager.INSTANCE.removeAll();
    }
}

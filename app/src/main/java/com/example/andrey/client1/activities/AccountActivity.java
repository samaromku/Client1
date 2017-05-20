package com.example.andrey.client1.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.andrey.client1.storage.Updater;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.managers.TokenManager;
import com.example.andrey.client1.service.GpsService;
import com.example.andrey.client1.storage.ConverterMessages;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.CommentsManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.entities.UserRole;
import com.example.andrey.client1.storage.OnListItemClickListener;
import com.example.andrey.client1.R;
import com.example.andrey.client1.adapter.TasksAdapter;

public class AccountActivity extends AppCompatActivity {
    private FloatingActionButton addTask;
    private TasksAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private TasksManager tasksManager = TasksManager.INSTANCE;
    private UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    private static final String TAG = "AccountActivity";
    private Client client = Client.INSTANCE;
    private TokenManager tokenManager = TokenManager.instance;
    private ConverterMessages converter = new ConverterMessages();
    private UsersManager usersManager = UsersManager.INSTANCE;
    private AddressManager addressManager = AddressManager.INSTANCE;

    private OnListItemClickListener clickListener = (v, position) -> {
        Task task = tasksManager.getTasks().get(position);
        Intent intent = new Intent(AccountActivity.this, TaskActivity.class).putExtra("taskNumber", position);
        new Updater(this, new Request(task, Request.WANT_SOME_COMMENTS), intent).execute();
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        getSupportActionBar().setTitle("Привет, " + usersManager.getUser().getLogin());
        RecyclerView tasksList = (RecyclerView) findViewById(R.id.tasks_list);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        addTask = (FloatingActionButton) findViewById(R.id.add_task_btn);
        buttonAddTask();
        addTask.setOnClickListener(v -> firstTimeAddAddresses());
        tasksList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(tasksManager.getTasks(), clickListener);
        tasksList.setAdapter(adapter);
        outsideIntents();
        swipeLayout.setOnRefreshListener(() -> new UpdateDataSwip().execute());
    }

    private void outsideIntents(){
        Intent intent = getIntent();
        boolean auth = intent.getBooleanExtra("fromAuth", false);
        boolean createTask = intent.getBooleanExtra("createTask", false);
        boolean changeStatus = intent.getBooleanExtra("statusChanged", false);
        boolean removeTask = intent.getBooleanExtra("removeTask", false);
//        if(auth) {
//            checkAuth();
//            UpdateDataAccountActivity test = new UpdateDataAccountActivity(this, adapter);
//            test.execute();
//        } else if(createTask || changeStatus || removeTask){
//            UpdateDataAccountActivity test = new UpdateDataAccountActivity(this, adapter);
//            test.execute();
//        }
    }

    private void firstTimeAddAddresses(){
        Intent intent = new Intent(this, CreateTaskActivity.class);
        if(addressManager.getAddresses().size()==0) {
            new Updater(this, new Request(Request.GIVE_ME_ADDRESSES_PLEASE), intent).execute();
        }else startActivity(intent);
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
            if(userRole.isMakeTasks()) {
                addTask.setVisibility(View.VISIBLE);
            }
            else addTask.setVisibility(View.INVISIBLE);
        }
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
                Intent intent = new Intent(AccountActivity.this, AuthActivity.class);
                new LogoutUpdater(this, new Request(Request.LOGOUT), intent).execute();
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

    private class UpdateDataSwip extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            User user = usersManager.getUser();
            Request request = new Request(user, Request.UPDATE_TASKS);
            converter.authMessage(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        }
    }

    private class LogoutUpdater extends AsyncTask<Void, Void, Void>{
        private ProgressDialog dialog;
        private Context context;
        private Request request;
        private Intent intent;
        private ConverterMessages converter = new ConverterMessages();

        LogoutUpdater(Context context, Request request, Intent intent){
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
            client.setAuth(false);
            allClear();
            stopService(new Intent(GpsService.newIntent(context)));
            System.out.println("стопарим сервис");
            context.startActivity(intent);
            super.onPostExecute(aVoid);
            dialog.dismiss();
            tokenManager.setToken(null);
        }
    }
}

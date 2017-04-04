package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.andrey.client1.R;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.entities.UserRole;
import com.example.andrey.client1.storage.JsonParser;

public class UserRoleActivity extends AppCompatActivity{
    private int userId;
    private CheckBox makeUser;
    private CheckBox makeTask;
    private CheckBox correctionTask;
    private CheckBox makeAddress;
    private CheckBox watchAddress;
    private CheckBox correctionStatus;
    private CheckBox makeExecutor;
    private CheckBox correctionExecutor;
    private CheckBox watchTasks;
    private CheckBox commentTasks;
    private CheckBox changePassword;
    private Button send;
    private UserRole userRole;
    private JsonParser parser = new JsonParser();
    UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    private boolean hasRight = false;
    private Client client = Client.INSTANCE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_role_activity);
        getSupportActionBar().setTitle("Права");
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        init();
        send.setOnClickListener(v -> {
            addOrChangeUserRoleOnServer();
            startActivity(new Intent(this, UsersActivity.class));
        });
    }

    private void addOrChangeUserRoleOnServer(){
        UserRole userRole1 = new UserRole(
                userId,
                makeUser.isChecked(),
                makeTask.isChecked(),
                correctionTask.isChecked(),
                makeAddress.isChecked(),
                watchAddress.isChecked(),
                correctionStatus.isChecked(),
                makeExecutor.isChecked(),
                correctionExecutor.isChecked(),
                watchTasks.isChecked(),
                commentTasks.isChecked(),
                changePassword.isChecked(),
                userId);
            client.sendMessage(parser.requestToServer(new Request(userRole1, Request.CHANGE_PERMISSION_PLEASE)));
            userRolesManager.setUpdateUserRole(userRole1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserRoleActivity.this, UsersActivity.class));
    }

    private void init(){
        send = (Button) findViewById(R.id.send);
        makeUser = (CheckBox) findViewById(R.id.make_user);
        makeTask = (CheckBox) findViewById(R.id.make_task);
        correctionTask = (CheckBox) findViewById(R.id.correction_task);
        makeAddress = (CheckBox) findViewById(R.id.make_address);
        watchAddress = (CheckBox) findViewById(R.id.watch_address);
        correctionStatus = (CheckBox) findViewById(R.id.correction_status);
        makeExecutor = (CheckBox) findViewById(R.id.make_executor);
        correctionExecutor = (CheckBox) findViewById(R.id.correction_executor);
        watchTasks = (CheckBox) findViewById(R.id.watch_tasks);
        commentTasks = (CheckBox) findViewById(R.id.comment_tasks);
        changePassword = (CheckBox) findViewById(R.id.change_password);

        userRole = userRolesManager.getRoleByUserId(userId);
        noRight();
    if(hasRight) {
        makeUser.setChecked(userRole.isMakeNewUser());
        makeTask.setChecked(userRole.isMakeTasks());
        correctionTask.setChecked(userRole.isCorrectionTask());
        makeAddress.setChecked(userRole.isMakeAddress());
        watchAddress.setChecked(userRole.isWatchAddress());
        correctionStatus.setChecked(userRole.isCorrectionStatus());
        makeExecutor.setChecked(userRole.isMakeExecutor());
        correctionExecutor.setChecked(userRole.isCorrectionExecutor());
        watchTasks.setChecked(userRole.isWatchTasks());
        commentTasks.setChecked(userRole.isCommentTasks());
        changePassword.setChecked(userRole.isChangePassword());
    }
    }

    private void noRight(){
        if(userRole==null){
            Toast.makeText(this, "вы не имеете права", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserRoleActivity.this, UsersActivity.class));
        }else hasRight = true;
    }

    private int createNewUserRole(){
        Intent intent = getIntent();
        int newUserId = intent.getIntExtra("newUserId", 0);
        if(newUserId!=0){
            return newUserId;
        }
        return 0;
    }
}
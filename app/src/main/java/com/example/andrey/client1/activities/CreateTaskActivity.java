package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Address;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.entities.UserRole;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.DateUtil;
import com.example.andrey.client1.storage.JsonParser;

public class CreateTaskActivity extends AppCompatActivity{
    AppCompatSpinner importanceSpinner;
    AppCompatSpinner statusSpinner;
    EditText body;
    EditText chooseDate;
    AutoCompleteTextView orgName;
    AppCompatSpinner userSpinner;
    AppCompatSpinner typeSpinner;
    Button createTask;
    String userName;
    DateUtil dateUtil = new DateUtil();
    JsonParser parser = new JsonParser();
    AddressManager addressManager = AddressManager.INSTANCE;
    TasksManager tasksManager = TasksManager.INSTANCE;
    UsersManager usersManager = UsersManager.INSTANCE;
    String[] org_names;
    String[] importance = tasksManager.getStatusStrings();
    String[] userNames;
    String[] statuses = tasksManager.getStatuses();
    String[] types = tasksManager.getType();
    String importanceSelected;
    String typeSelected;
    String statusSelected;
    Address address;
    boolean createTaskIsChecked = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_activity);
        getSupportActionBar().setTitle(R.string.create_task);
        init();
        createDropMenuOrgNames();
        clickOnUserSpinner();
        clickOnImportance();
        clickOnType();
        clickOnStatus();
        checkRole();


        createTask.setOnClickListener(v -> {
            if(createTaskIsChecked) {
                if (orgName.getText().toString().equals("")) {
                    orgName.setHint("Вы должны заполнить это поле");
                } else if (body.getText().toString().equals("")) {
                    body.setHint("Вы должны заполнить это поле");
                } else if (chooseDate.getText().toString().equals("")) {
                    chooseDate.setHint("Вы должны заполнить это поле");
                } else {
                    System.out.println(chooseDate);
                    address = addressManager.getAddressByName(orgName.getText().toString());
                    Task task = new Task(
                            tasksManager.getMaxId() + 1,
                            dateUtil.currentDate(),
                            importanceSelected,
                            body.getText().toString(),
                            statusSelected,
                            typeSelected,
                            chooseDate.getText().toString(),
                            usersManager.getUserByUserName(userName).getId(),
                            address.getId()
                    );
                    task.setAddress(address.getAddress());
                    task.setOrgName(address.getName());
                    tasksManager.setTask(task);
                    Client.INSTANCE.sendMessage(parser.requestToServer(new Request(task, Request.ADD_TASK_TO_SERVER)));
                    startActivity(new Intent(CreateTaskActivity.this, AccountActivity.class));
                }
            }
            else {
                Toast.makeText(this, "у вас нет прав", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateTaskActivity.this, AccountActivity.class));
            }
        });

    }

    private void clickOnImportance(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, importance);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);
        importanceSpinner.setSelection(0);

        importanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                importanceSelected = importance[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                importanceSelected = importance[0];
            }
        });
    }

    private void checkRole(){
        UserRole userRole = UserRolesManager.INSTANCE.getUserRole();
        if(userRole!=null && userRole.isMakeTasks()){
            createTaskIsChecked=true;
        }
    }

    private void clickOnStatus(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setSelection(0);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = statuses[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statusSelected = statuses[0];
            }
        });
    }

    private void clickOnType(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(0);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelected = types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeSelected = types[0];
            }
        });
    }

    private void clickOnUserSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);
        userSpinner.setSelection(0);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userName = userNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userName = userNames[0];
            }
        });
    }

    private void createDropMenuOrgNames(){
        orgName.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, org_names));
        if(org_names.length==0){
            orgName.setHint("Получите адреса в нужной вкладке");
        }
    }

    private void init(){
        chooseDate = (EditText) findViewById(R.id.choose_date);
        orgName = (AutoCompleteTextView) findViewById(R.id.task_title);
        importanceSpinner = (AppCompatSpinner) findViewById(R.id.spinner_importance);
        statusSpinner = (AppCompatSpinner) findViewById(R.id.spinner_status);
        typeSpinner = (AppCompatSpinner) findViewById(R.id.spinner_type);
        body = (EditText) findViewById(R.id.task_body);
        userSpinner = (AppCompatSpinner) findViewById(R.id.spinner_users);
        createTask = (Button) findViewById(R.id.create_task_btn);
        getImportance();
        createOrgNames();
        createUserNames();
    }


    private void createUserNames(){
        userNames = new String[usersManager.getUsers().size()];
        for (int i = 0; i < usersManager.getUsers().size(); i++) {
            userNames[i] = usersManager.getUsers().get(i).getLogin();
        }
    }

    private void getImportance(){
        importanceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tasksManager.getStatusStrings()));
    }

    private void createOrgNames(){
        org_names = new String[addressManager.getAddresses().size()];
        for (int i = 0; i < addressManager.getAddresses().size(); i++) {
            org_names[i] = addressManager.getAddresses().get(i).getName();
        }
    }
}

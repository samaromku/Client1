package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import com.example.andrey.client1.storage.Updater;
import com.example.andrey.client1.entities.Address;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.fragments.DatePickerFragment;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.ConverterMessages;
import com.example.andrey.client1.storage.DateUtil;
import com.example.andrey.client1.storage.JsonParser;

public class UpdateTaskActivity extends AppCompatActivity{
    private AppCompatSpinner importanceSpinner;
    private AppCompatSpinner statusSpinner;
    private EditText body;
    private Button chooseDate;
    private AutoCompleteTextView addressesForUpdate;
    private AppCompatSpinner userSpinner;
    private AppCompatSpinner typeSpinner;
    private Button createTask;
    private String userName;
    private DateUtil dateUtil = new DateUtil();
    private JsonParser parser = new JsonParser();
    private AddressManager addressManager = AddressManager.INSTANCE;
    private TasksManager tasksManager = TasksManager.INSTANCE;
    private UsersManager usersManager = UsersManager.INSTANCE;
    private String[] addresses;
    private String[] importance = tasksManager.getImportanceString();
    private String[] userNames;
    private String[] statuses = tasksManager.getAllStatuses();
    private String[] types = tasksManager.getType();
    private String importanceSelected;
    private String typeSelected;
    private String statusSelected;
    private Task task;
    private int taskId;
    private Address address = new Address();
    private static final String DIALOG_DATE = "date_dialog";
    private Client client = Client.INSTANCE;
    private int newUserPosition = 0;
    private ConverterMessages converter = new ConverterMessages();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_activity);
        getSupportActionBar().setTitle("Изменить задание");
        init();
        getFromIntent();
        clickOnUserSpinner();
        clickOnImportance();
        clickOnType();
        clickOnStatus();
        setNeedData();

        createTask.setOnClickListener(v -> clickOnBtnCreateTask());
        chooseDate.setOnClickListener(v -> chooseDate());

    }

    private void clickOnBtnCreateTask(){
        if (addressesForUpdate.getText().toString().equals("")) {
            addressesForUpdate.setHint("Вы должны заполнить это поле");
        } else if (body.getText().toString().equals("")) {
            body.setHint("Вы должны заполнить это поле");
        } else if (chooseDate.getText().toString().equals("Выбрать дату")) {
            chooseDate.setText("Вы должны заполнить это поле");
        } else {
//            System.out.println(chooseDate);
//            if(taskId!=0){
//                address.setId(tasksManager.getById(taskId).getAddressId());
//                address.setAddress(tasksManager.getById(taskId).getAddress());
//                address.setName(tasksManager.getById(taskId).getOrgName());
//            }else
                address = addressManager.getAddressByAddress(addressesForUpdate.getText().toString());
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
            task.setId(taskId);
//            converter.sendMessage(new Request(task, Request.UPDATE_TASK));

            if(address.getId()==0){
                Toast.makeText(this, "Список адресов пуст, получите его", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, AccountActivity.class).putExtra("createTask", true);
                new Updater(this, new Request(task, Request.UPDATE_TASK), intent).execute();
//                startActivity(new Intent(this, AccountActivity.class).putExtra("createTask", true));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AccountActivity.class));
    }

    private void chooseDate(){
        FragmentManager manager = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(dateUtil.getDate());
        dialog.show(manager, DIALOG_DATE);
    }

    private void setNeedData(){

        statuses = tasksManager.getAllStatuses();
        Task task = tasksManager.getById(taskId);
        body.setText(task.getBody());
        chooseDate.setText(task.getDoneTime());
        createTask.setText("Изменить задание");
        addressesForUpdate.setText(task.getAddress());
        int userPosition = 0;
        //заглушка на удаленного пользователя
        if(usersManager.getUserById(task.getUserId())!=null) {
            for (int i = 0; i < userNames.length; i++) {
                if (userNames[i].equals(usersManager.getUserById(task.getUserId()).getLogin()))
                    userPosition = i;
            }
        }
        userSpinner.setSelection(userPosition);
        int importancePostition = 0;
        for (int i = 0; i < importance.length; i++) {
            if(importance[i].equals(task.getImportance()))
                importancePostition = i;
        }
        importanceSpinner.setSelection(importancePostition);
        int statusPosition = 0;
        for (int i = 0; i < statuses.length; i++) {
            if(statuses[i].equals(task.getStatus()))
                statusPosition = i;
        }
        System.out.println(statusPosition+"status");
        statusSpinner.setSelection(statusPosition);
    }

    private void getFromIntent(){
        Intent intent = getIntent();
        taskId = intent.getIntExtra("taskId", 0);
        task = tasksManager.getById(taskId);
    }

    private void init(){
        chooseDate = (Button) findViewById(R.id.choose_date);
        addressesForUpdate = (AutoCompleteTextView) findViewById(R.id.task_title);
        importanceSpinner = (AppCompatSpinner) findViewById(R.id.spinner_importance);
        statusSpinner = (AppCompatSpinner) findViewById(R.id.spinner_status);
        typeSpinner = (AppCompatSpinner) findViewById(R.id.spinner_type);
        body = (EditText) findViewById(R.id.task_body);
        userSpinner = (AppCompatSpinner) findViewById(R.id.spinner_users);
        createTask = (Button) findViewById(R.id.create_task_btn);
        createOrgNames();
        createDropMenuOrgNames();
        createUserNames();
        getImportance();
    }


    private void createUserNames(){
        userNames = new String[usersManager.getUsers().size()];
        for (int i = 0; i < usersManager.getUsers().size(); i++) {
            userNames[i] = usersManager.getUsers().get(i).getLogin();
        }
    }

    private void getImportance(){
        importanceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tasksManager.getImportanceString()));
    }

    private void createOrgNames(){
        addresses = new String[addressManager.getAddresses().size()];
        for (int i = 0; i < addressManager.getAddresses().size(); i++) {
            addresses[i] = addressManager.getAddresses().get(i).getAddress();
        }
    }


    private void createDropMenuOrgNames(){
        addressesForUpdate.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, addresses));
        if(addresses.length==0){
            addressesForUpdate.setHint("Получите адреса в нужной вкладке");
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
                if(statuses[position].equals(Task.NEW_TASK)){
                    for (int i = 0; i < userNames.length; i++) {
                        if(userNames[i].equals("Не назначена")){
                            newUserPosition = i;
                        }
                    }
                    userSpinner.setSelection(newUserPosition);
                }
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
                if(position==newUserPosition){
                    statusSpinner.setSelection(0);
                }else{
                    statusSpinner.setSelection(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userName = userNames[0];
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
}

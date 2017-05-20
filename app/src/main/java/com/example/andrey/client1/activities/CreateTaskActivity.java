package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrey.client1.network.CheckNetwork;
import com.example.andrey.client1.R;
import com.example.andrey.client1.storage.Updater;
import com.example.andrey.client1.entities.Address;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.fragments.DatePickerFragment;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.ConverterMessages;
import com.example.andrey.client1.storage.DateUtil;

public class CreateTaskActivity extends AppCompatActivity {
    private AppCompatSpinner importanceSpinner;
    private AppCompatSpinner statusSpinner;
    private EditText body;
    private Button chooseDate;
    private AutoCompleteTextView addressName;
    private AppCompatSpinner userSpinner;
    private AppCompatSpinner typeSpinner;
    private Button createTask;
    private String userName;
    private DateUtil dateUtil = new DateUtil();
    private AddressManager addressManager = AddressManager.INSTANCE;
    private TasksManager tasksManager = TasksManager.INSTANCE;
    private UsersManager usersManager = UsersManager.INSTANCE;
    private String[] addressNameArray;
    private String[] importanceString = tasksManager.getImportanceString();
    private String[] userNames;
    private String[] statusesString = tasksManager.getStatusesForCreate();
    private String[] types = tasksManager.getType();
    private String statusSelected;
    private String typeSelected;
    private String impornanceSelected;
    private Address address = new Address();
    private int taskId = 0;
    private static final String DIALOG_DATE = "date_dialog";
    private int newUserPosition = 0;
    private ConverterMessages converter = new ConverterMessages();
    private CheckNetwork checkNetwork = CheckNetwork.instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_activity);
        getSupportActionBar().setTitle(R.string.create_task);
        init();
        clickOnUserSpinner();
        clickOnImportance();
        clickOnType();
        clickOnStatus();

        createTask.setOnClickListener(v ->
                clickOnBtnCreateTask()
        );
        chooseDate.setOnClickListener(v -> chooseDate());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void chooseDate(){
        FragmentManager manager = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(dateUtil.getDate());
        dialog.show(manager, DIALOG_DATE);
    }

    private void clickOnBtnCreateTask() {
        if (addressName.getText().toString().equals("")) {
            addressName.setHint("Вы должны заполнить это поле");
        } else if (body.getText().toString().equals("")) {
            body.setHint("Вы должны заполнить это поле");
        } else if (chooseDate.getText().toString().equals("Выбрать дату")) {
            chooseDate.setText("Вы должны заполнить это поле");
        } else if (chooseDate.getText().toString().equals("Вы должны заполнить это поле")) {
            chooseDate.setText("Вы должны заполнить это поле");
        } else {
            address = addressManager.getAddressByAddress(addressName.getText().toString());
            Task task = new Task(
                    tasksManager.getMaxId() + 1,
                    dateUtil.currentDate(),
                    impornanceSelected,
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
            if (address.getId() == 0) {
                Toast.makeText(this, "Список адресов пуст, получите его", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(this, AccountActivity.class);
                new Updater(this, new Request(task, Request.ADD_TASK_TO_SERVER), intent).execute();
            }
        }
    }

    private void clickOnImportance(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, importanceString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);
        importanceSpinner.setSelection(0);

        importanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                impornanceSelected = importanceString[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                impornanceSelected = importanceString[0];
            }
        });
    }

    private void clickOnStatus(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, statusesString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setSelection(0);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = statusesString[position];
                if(statusesString[position].equals(Task.NEW_TASK)) {
                    for (int i = 0; i < userNames.length; i++) {
                        if (userNames[i].equals("Не назначена")) {
                            newUserPosition = i;
                        }
                    }
                    userSpinner.setSelection(newUserPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statusSelected = statusesString[0];
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AccountActivity.class));
    }


    private void init(){
        chooseDate = (Button) findViewById(R.id.choose_date);
        addressName = (AutoCompleteTextView) findViewById(R.id.task_title);
        importanceSpinner = (AppCompatSpinner) findViewById(R.id.spinner_importance);
        statusSpinner = (AppCompatSpinner) findViewById(R.id.spinner_status);
        typeSpinner = (AppCompatSpinner) findViewById(R.id.spinner_type);
        body = (EditText) findViewById(R.id.task_body);
        userSpinner = (AppCompatSpinner) findViewById(R.id.spinner_users);
        createTask = (Button) findViewById(R.id.create_task_btn);
        createImportance();
        createUserNames();
        createOrgNames();
        createDropMenuOrgNames();
    }


    private void createUserNames(){
        userNames = new String[]{usersManager.getUsers().get(0).getLogin()};
//        for (int i = 0; i < usersManager.getUsers().size(); i++) {
//            userNames[i] = usersManager.getUsers().get(i).getLogin();
//        }
    }

    private void createImportance(){
        importanceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tasksManager.getImportanceString()));
    }

    private void createOrgNames(){
        addressNameArray = new String[addressManager.getAddresses().size()];
        for (int i = 0; i < addressManager.getAddresses().size(); i++) {
            addressNameArray[i] = addressManager.getAddresses().get(i).getAddress();
        }
    }


    private void createDropMenuOrgNames(){
        System.out.println(addressNameArray.length);
        addressName.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, addressNameArray));
        if(addressNameArray.length==0){
            addressName.setHint("Получите адреса в нужной вкладке");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.address:
                getAddressesFromServer();
                return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void getAddressesFromServer(){
//        addressManager.removeAll();
        Intent intent = new Intent(this, AddressActivity.class);
        startActivity(intent);
//        new Updater(this, new Request(Request.GIVE_ME_ADDRESSES_PLEASE), intent).execute();
    }
}

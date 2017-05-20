package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.storage.Updater;
import com.example.andrey.client1.adapter.CommentsAdapter;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.managers.CommentsManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.entities.Comment;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.storage.ConverterMessages;
import com.example.andrey.client1.storage.DateUtil;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.OnListItemClickListener;

public class TaskActivity extends AppCompatActivity{
    private TextView typeTask;
    private TextView importance;
    private TextView orgName;
    private TextView address;
    private Button distributed;
    private TextView taskBody;
    private TextView deadLine;
    private TextView userLogin;
    private Button doneBtn;
    private int taskNumber;
    private EditText comment;
    private Button needHelp;
    private Button disAgree;
    private RecyclerView commentsList;
    private CommentsAdapter adapter;
    TasksManager tasksManager = TasksManager.INSTANCE;
    CommentsManager commentsManager = CommentsManager.INSTANCE;
    UsersManager usersManager = UsersManager.INSTANCE;
    UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    Task task;
    Comment newComment;
    private DateUtil dateUtil = new DateUtil();
    private AddressManager addressManager = AddressManager.INSTANCE;

    private OnListItemClickListener clickListener = (v, position) -> {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        Intent intent = getIntent();
        taskNumber = intent.getIntExtra("taskNumber", 0);
        getSupportActionBar().setTitle(tasksManager.getTasks().get(taskNumber).getStatus());
        initiate();
        commentsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsAdapter(commentsManager.getCommentsByTaskId(task.getId()), clickListener);
        commentsList.setAdapter(adapter);
        btnClicks();
        setEnableBtns();
//        UpdateData data = new UpdateData();
//        data.execute();
    }

    private void setEnableBtns(){
//        - новая, помощь, отказ - взять себе доступна, остальные блок
        if(task.getStatus().equals(Task.NEW_TASK) ||
            task.getStatus().equals(Task.DISAGREE_TASK) ||
            task.getStatus().equals(Task.NEED_HELP)){
            needHelp.setEnabled(false);
            disAgree.setEnabled(false);
            doneBtn.setEnabled(false);
        }
//        - распределена - взять себе блок, перевести в выполненные блок остальные доступны
        else if(task.getStatus().equals(Task.DISTRIBUTED_TASK)){
            distributed.setEnabled(false);
        }
// - контроль - взять себе, перевести в выполненные - доступ, остальные блок
        else if(task.getStatus().equals(Task.CONTROL_TASK)){
            needHelp.setEnabled(false);
            disAgree.setEnabled(false);
            doneBtn.setEnabled(false);
        }
//        - выполненные - неважно
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(userRolesManager.getUserRole().isCorrectionTask()) {
            getMenuInflater().inflate(R.menu.change_task_menu, menu);
        }
        return true;
    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if(task.getStatus().equals(Task.DISTRIBUTED_TASK)||
//                task.getStatus().equals(Task.NEW_TASK) ||
//                task.getStatus().equals(Task.DISAGREE_TASK) ||
//                task.getStatus().equals(Task.NEED_HELP)){
//            menu.getItem(R.id.check_as_done).setEnabled(false);
//        }
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_task:
                firstTimeAddAddresses();
                commentsManager.removeAll();
                return true;
            case R.id.remove_task:
                commentsManager.removeAll();
                tasksManager.setRemoveTask(task);
                Intent intent = new Intent(this, AccountActivity.class).putExtra("removeTask", true);
                new Updater(this, new Request(task, Request.REMOVE_TASK), intent).execute();
//                converter.sendMessage(new Request(task, Request.REMOVE_TASK));
//                startActivity(new Intent(this, AccountActivity.class).putExtra("removeTask", true));
                return true;
            case R.id.check_as_done:
                clickToChangeStatus(Task.DONE_TASK);
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }


    private void firstTimeAddAddresses(){
        Intent intent = new Intent(this, UpdateTaskActivity.class).putExtra("taskId", task.getId());
        if(addressManager.getAddresses().size()==0) {
            new Updater(this, new Request(Request.GIVE_ME_ADDRESSES_PLEASE), intent).execute();
        }else startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TaskActivity.this, AccountActivity.class));
        commentsManager.removeAll();
    }

    private void btnClicks(){
        doneBtn.setOnClickListener(v -> clickOnButton(Task.CONTROL_TASK));
        needHelp.setOnClickListener(v -> clickOnButton(Task.NEED_HELP));
        disAgree.setOnClickListener(v -> clickOnButton(Task.DISAGREE_TASK));
        distributed.setOnClickListener(v -> clickToChangeStatus(Task.DISTRIBUTED_TASK));
    }


    private void clickOnButton(String actionTask){
        if(comment.getText().toString().equals("")){
            comment.setHint("Вы должны заполнить это поле");
        }
        else {
            //создаем новый коммент
            newComment = new Comment(
                    new DateUtil().currentDate(),
                    comment.getText().toString(),
                    usersManager.getUser().getId(),
                    tasksManager.getTasks().get(taskNumber).getId());
            //добавить в бдб отправить на сервер
            tasksManager.setStatus(actionTask);
            commentsManager.setComment(newComment);
            commentsManager.removeAll();
            Intent intent = new Intent(this, AccountActivity.class).putExtra("statusChanged", true);
            new Updater(this, new Request(newComment, actionTask), intent).execute();
//            converter.sendMessage(new Request(newComment, actionTask));
//            startActivity(new Intent(this, AccountActivity.class).putExtra("statusChanged", true));
        }
    }

    //юзер решил взять заявку себе
    private void clickToChangeStatus(String changedStatusTask){
        task.setUserId(usersManager.getUser().getId());
        task.setStatus(changedStatusTask);
        tasksManager.setTask(task);
        commentsManager.removeAll();
        Intent intent = new Intent(this, AccountActivity.class).putExtra("statusChanged", true);
        new Updater(this, new Request(task, changedStatusTask), intent).execute();
    }

    private void initiate(){
        commentsList = (RecyclerView) findViewById(R.id.comments);
        typeTask = (TextView) findViewById(R.id.type_task);
        importance = (TextView) findViewById(R.id.importance);
        orgName = (TextView) findViewById(R.id.org_name);
        address = (TextView) findViewById(R.id.address);
        distributed = (Button) findViewById(R.id.distibuted);
        taskBody = (TextView) findViewById(R.id.task_body);
        deadLine = (TextView) findViewById(R.id.deadline);
        doneBtn = (Button) findViewById(R.id.done);
        needHelp = (Button) findViewById(R.id.need_help);
        disAgree = (Button) findViewById(R.id.disagree);
        comment = (EditText) findViewById(R.id.commentFromUser);
        userLogin = (TextView) findViewById(R.id.user_login);

        task = tasksManager.getTasks().get(taskNumber);
        tasksManager.setTask(task);

        typeTask.setText(task.getType());
        importance.setText(task.getImportance());
        orgName.setText(task.getOrgName());
        address.setText(task.getAddress());
        taskBody.setText(task.getBody());
        deadLine.setText(task.getDoneTime());
        //заглушка на удаленного пользователя
        User userWithNotName = usersManager.getUserById(task.getUserId());
        if(userWithNotName!=null) {
            userLogin.setText(usersManager.getUserById(task.getUserId()).getLogin());
        }else userLogin.setText("Удален");
    }
}

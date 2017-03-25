package com.example.andrey.client1.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.adapter.CommentsAdapter;
import com.example.andrey.client1.managers.CommentsManager;
import com.example.andrey.client1.managers.TasksManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.entities.Comment;
import com.example.andrey.client1.storage.DataWorker;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.storage.DateUtil;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.OnListItemClickListener;
import com.example.andrey.client1.storage.UpdateData;

public class TaskActivity extends AppCompatActivity{
    private TextView typeTask;
    private TextView importance;
    private TextView orgName;
    private TextView address;
    private TextView telephone;
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
    Task task;
    Comment newComment;
    DataWorker dataWorker = new DataWorker();

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
        UpdateData data = new UpdateData();
        data.execute();
//        update();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TaskActivity.this, AccountActivity.class));
        commentsManager.removeAll();
    }

    private class UpdateData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(TaskActivity.this);
            dialog.setTitle("Загружаются данные");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (Client.INSTANCE.getThread() != null) {
                    if (!Client.INSTANCE.getThread().isInterrupted()) {
                        try {
                            Client.INSTANCE.getThread().join();
                            Thread.sleep(500);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new CommentsAdapter(commentsManager.getCommentsByTaskId(task.getId()), clickListener);
            commentsList.setAdapter(adapter);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    private void btnClicks(){
        doneBtn.setOnClickListener(v -> clickOnButton(Task.DONE_TASK));
        needHelp.setOnClickListener(v -> clickOnButton(Task.NEED_HELP));
        disAgree.setOnClickListener(v -> clickOnButton(Task.DISAGREE_TASK));
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
                    dataWorker.getUserId(),
                    tasksManager.getTasks().get(taskNumber).getId());
            //добавить в бдб отправить на сервер
            tasksManager.setStatus(actionTask);
            commentsManager.setComment(newComment);
            Client.INSTANCE.sendMessage(new JsonParser().requestToServer(new Request(newComment, actionTask)));
            startActivity(new Intent(TaskActivity.this, AccountActivity.class));
        }
    }

    private void initiate(){
        commentsList = (RecyclerView) findViewById(R.id.comments);
        typeTask = (TextView) findViewById(R.id.type_task);
        importance = (TextView) findViewById(R.id.importance);
        orgName = (TextView) findViewById(R.id.org_name);
        address = (TextView) findViewById(R.id.address);
        telephone = (TextView) findViewById(R.id.telefon);
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
        //telephone.setText(Client.INSTANCE.getTaskList().get(taskNumber).getTelephone());
        taskBody.setText(task.getBody());
        deadLine.setText(task.getDoneTime());
        userLogin.setText(usersManager.getUserById(task.getUserId()).getLogin());
    }
}

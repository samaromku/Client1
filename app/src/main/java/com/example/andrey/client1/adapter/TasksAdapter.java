package com.example.andrey.client1.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.storage.OnListItemClickListener;
import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Task;

import java.util.List;


    public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
        private List<Task> tasks;
        private OnListItemClickListener clickListener;

        public TasksAdapter(List<Task> tasks, OnListItemClickListener clickListener) {
            this.tasks = tasks;
            this.clickListener = clickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(tasks.get(position));
            if(tasks.get(position).getImportance()!=null){
                if(tasks.get(position).getStatus().equals(Task.DONE_TASK)){
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                    holder.title.setTextColor(Color.BLACK);
                    holder.body.setTextColor(Color.BLACK);
                    holder.address.setTextColor(Color.BLACK);
                    holder.created.setTextColor(Color.BLACK);
                    holder.firsLetter.setTextColor(Color.BLACK);
                    holder.userLogin.setTextColor(Color.BLACK);
                    return;
                }

                switch (tasks.get(position).getImportance()){
                    case Task.STANDART:
                        holder.itemView.setBackgroundColor(Color.WHITE);
                        holder.title.setTextColor(Color.BLACK);
                        holder.body.setTextColor(Color.BLACK);
                        holder.address.setTextColor(Color.BLACK);
                        holder.created.setTextColor(Color.BLACK);
                        holder.firsLetter.setTextColor(Color.BLACK);
                        holder.userLogin.setTextColor(Color.BLACK);
                        break;

                    case Task.AVARY:
                        holder.itemView.setBackgroundColor(Color.RED);
                        holder.title.setTextColor(Color.WHITE);
                        holder.body.setTextColor(Color.WHITE);
                        holder.address.setTextColor(Color.WHITE);
                        holder.created.setTextColor(Color.WHITE);
                        holder.firsLetter.setTextColor(Color.WHITE);
                        holder.userLogin.setTextColor(Color.WHITE);
                        break;

                    case Task.INFO:
                        holder.itemView.setBackgroundColor(Color. BLUE);
                        holder.title.setTextColor(Color.WHITE);
                        holder.body.setTextColor(Color.WHITE);
                        holder.address.setTextColor(Color.WHITE);
                        holder.created.setTextColor(Color.WHITE);
                        holder.firsLetter.setTextColor(Color.WHITE);
                        holder.userLogin.setTextColor(Color.WHITE);
                        break;

                    default:
                        holder.itemView.setBackgroundColor(Color.WHITE);
                        holder.title.setTextColor(Color.BLACK);
                        holder.body.setTextColor(Color.BLACK);
                        holder.address.setTextColor(Color.BLACK);
                        holder.created.setTextColor(Color.BLACK);
                        holder.firsLetter.setTextColor(Color.BLACK);
                        holder.userLogin.setTextColor(Color.BLACK);
                        break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title;
            TextView body;
            TextView address;
            TextView created;
            TextView firsLetter;
            TextView userLogin;

            public ViewHolder(View itemView) {
                super(itemView);
                userLogin = (TextView) itemView.findViewById(R.id.user_login);
                title = (TextView) itemView.findViewById(R.id.title);
                address = (TextView) itemView.findViewById(R.id.address);
                created = (TextView) itemView.findViewById(R.id.date);
                firsLetter = (TextView) itemView.findViewById(R.id.firstLetter);
                body = (TextView) itemView.findViewById(R.id.body);
                itemView.setOnClickListener(this);
            }

            public void bind(Task task) {
                title.setText(task.getOrgName());
                address.setText(task.getAddress());
                created.setText(task.getCreated());
                body.setText(task.getBody());
                userLogin.setText(UsersManager.INSTANCE.getUserById(task.getUserId()).getLogin());
                char[] str = task.getStatus().toCharArray();
                /*Ставим плюсики и крестики*/
                switch (task.getStatus()) {
                    case Task.NEED_HELP:
                        firsLetter.setText("+");
                        break;
                    case Task.DONE_TASK:
                        firsLetter.setText("X");
                        break;
                    default:
                        firsLetter.setText(String.valueOf(str[0]).toUpperCase());
                        break;
                }
            }

            @Override
            public void onClick(View v) {
                clickListener.onClick(v, getAdapterPosition());
            }
        }
    }


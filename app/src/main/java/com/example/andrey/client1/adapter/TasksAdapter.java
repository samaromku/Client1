package com.example.andrey.client1.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            if(tasks.get(position).getStatus()!=null){
            switch (tasks.get(position).getStatus()) {
                case Task.DISAGREE_TASK:
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2"));
                    break;
                case Task.DONE_TASK:
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                    break;
                case Task.NEED_HELP:
                    holder.itemView.setBackgroundColor(Color.parseColor("#F0F4C3"));
                    break;
                case Task.DOING_TASK:
                    holder.itemView.setBackgroundColor(Color.YELLOW);
                    break;
                case Task.DISTRIBUTED_TASK:
                    holder.itemView.setBackgroundColor(Color.parseColor("#B2EBF2"));
                    break;
                case Task.CONTROL_TASK:
                    holder.itemView.setBackgroundColor(Color.parseColor("#FCE4EC"));
                    break;
                case Task.NEW_TASK:
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    break;
                default:
                    holder.itemView.setBackgroundColor(Color.WHITE);
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


            public ViewHolder(View itemView) {
                super(itemView);
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
                //char[] str = task.getTitle().toCharArray();
                firsLetter.setText(String.valueOf(getAdapterPosition()+1));
            }

            @Override
            public void onClick(View v) {
                clickListener.onClick(v, getAdapterPosition());
            }
        }
    }


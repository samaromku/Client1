package com.example.andrey.client1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Comment;
import com.example.andrey.client1.storage.OnListItemClickListener;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private List<Comment> comments;
    private OnListItemClickListener clickListener;

    public CommentsAdapter(List<Comment> comments, OnListItemClickListener clickListener) {
        this.comments = comments;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView body;
        TextView userName;


        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            body = (TextView) itemView.findViewById(R.id.comment_body);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            itemView.setOnClickListener(this);
        }

        public void bind(Comment comment) {
            date.setText(comment.getTs());
            body.setText(comment.getBody());
            String userLogin = "";
            for (int i = 0; i < Client.INSTANCE.getUsers().size(); i++) {
                if(Client.INSTANCE.getUsers().get(i).getId()==(comment.getUserId())){
                    userLogin = Client.INSTANCE.getUsers().get(i).getLogin();
                }
            }
            userName.setText(userLogin);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }
}

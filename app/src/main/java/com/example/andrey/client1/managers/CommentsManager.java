package com.example.andrey.client1.managers;

import com.example.andrey.client1.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsManager {
    Comment comment;
    List<Comment>comments;
    public static final CommentsManager INSTANCE = new CommentsManager();

    private CommentsManager(){
        comments = new ArrayList<>();
    }

    public Comment getComment() {
        return comment;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void addAll(List<Comment> commentList){
        comments.addAll(commentList);
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void removeAll(){
        if(comments.size()>0){
            comments.clear();
        }
    }

    public List<Comment> getCommentsByTaskId(int taskId){
        List<Comment> commentList = new ArrayList<>();
        for(Comment c:comments){
            if(c.getTaskId()==taskId){
                commentList.add(c);
            }
        }
        return commentList;
    }
}

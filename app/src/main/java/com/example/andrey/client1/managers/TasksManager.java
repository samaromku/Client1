package com.example.andrey.client1.managers;

import com.example.andrey.client1.entities.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TasksManager {
    private Task task;
    private List<Task> tasks;
    private Task removeTask;
    private String status;
    private String[] importanceString = new String[]{Task.STANDART, Task.INFO, Task.AVARY, Task.TIME};
    private String[] type = new String[]{"к сведению", "приемка", "УУИТЭ", "ИТП", "АРТФ"};
    private String[] statusesForCreate = new String[]{Task.NEW_TASK, Task.DISTRIBUTED_TASK};
    private String[] AllStatuses = new String[]{Task.NEW_TASK, Task.DISTRIBUTED_TASK, Task.DOING_TASK, Task.CONTROL_TASK, Task.DONE_TASK, Task.NEED_HELP};
    public static final TasksManager INSTANCE = new TasksManager();

    public Task getRemoveTask() {
        return removeTask;
    }

    public void setRemoveTask(Task removeTask) {
        this.removeTask = removeTask;
    }

    public String[] getAllStatuses() {
        return AllStatuses;
    }

    public String[] getStatusesForCreate() {
        return statusesForCreate;
    }

    public String[] getType() {
        return type;
    }

    public String[] getImportanceString() {
        return importanceString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task getTask() {
        return task;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void addAll(List<Task> taskList){
        tasks.addAll(taskList);
    }

    public Task getById(int taskId){
        for(Task t:tasks){
            if(t.getId()==taskId){
                return t;
            }
        }
        return null;
    }
    public void removeDone(){
        Iterator<Task> iterator = tasks.iterator();
        while(iterator.hasNext()){
            if(iterator.next().getStatus().equals(Task.DONE_TASK)){
                iterator.remove();
            }
        }
    }

    public void removeTask(Task task){
        Iterator<Task> iterator = tasks.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(task)){
                iterator.remove();
            }
        }
    }

    public List<Task> getByUserId(int userId){
        List<Task>taskList = new ArrayList<>();
        for(Task t:tasks){
            if(t.getUserId()==userId){
                taskList.add(t);
            }
        }
        return taskList;
    }

    public void updateTask(Task task){
        Iterator<Task> iterator = tasks.iterator();
        while(iterator.hasNext()){
            if(iterator.next().getId()==task.getId()){
                iterator.remove();
            }
        }
        tasks.add(task);
    }

    public void addUnique(List<Task>taskList){
        for(Task t:taskList){
            if(!tasks.contains(t)){
                tasks.add(t);
            }
        }
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void updateTask(String status, int id){
        for(Task t:tasks){
            if(t.getId()==id){
                t.setStatus(status);
            }
        }
    }

    public void removeAll(){
        if(tasks.size()>0){
            tasks.clear();
        }
    }

    public int getMaxId(){
        int max = 0;
        for(Task t:tasks){
            if(t.getId()>max){
                max=t.getId();
            }
        }
        return max;
    }

    private TasksManager(){
        tasks = new ArrayList<>();
    }
}


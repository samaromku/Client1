package com.example.andrey.client1.activities;

import com.example.andrey.client1.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        createFakeTasks();
        return tasks;
    }

    public void createFakeTasks(){
        tasks.add(new Task("Снять отчет", "Снять вычисления с узла учета в теплоцентре, забрать отчеты за предыдущие месяцы, сказать начальству что зарплата маленькая", "23.02 20-15", false, "24.02 20-15", "Антон", "Илюшина 15", "895232655985", ""));
        tasks.add(new Task("Курьерская доставка", "Отвезти договора по адресу, забрать старые", "23.02 20-15", false, "24.02 20-15", "Антон", "Кушелевская 25", "654211548", ""));
        tasks.add(new Task("Закончить монтаж", "Промаркировать оборудование, опломбировать приборы", "23.02 20-15", false, "24.02 20-15", "Антон", "Английский пр. 15", "81265855685", ""));
    }
}

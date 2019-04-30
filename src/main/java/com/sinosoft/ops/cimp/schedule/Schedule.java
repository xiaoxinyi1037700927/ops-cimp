package com.sinosoft.ops.cimp.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class Schedule {

    private final List<Task> tasks;

    @Autowired
    public Schedule(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Scheduled(cron = "${schedule.cron}")
    public void execTasks() {
        tasks.sort(Comparator.comparingInt(Task::getOrder));
        tasks.forEach(Task::exec);
    }
}

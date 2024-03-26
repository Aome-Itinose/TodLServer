package org.aome.todlserver.util.exceptions.task;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException() {
        super("Task not found");
    }
}

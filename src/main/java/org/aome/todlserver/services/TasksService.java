package org.aome.todlserver.services;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.repositories.TasksRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TasksService {
    private final TasksRepository tasksRepository;
}

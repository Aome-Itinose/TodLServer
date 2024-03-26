package org.aome.todlserver.services;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.Task;
import org.aome.todlserver.repositories.TasksRepository;
import org.aome.todlserver.util.exceptions.task.TaskNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TasksService {
    private final TasksRepository tasksRepository;

    private final ProjectsService projectsService;


    public Task getByIdAndProjectId(int project_id, int task_id){
        Project project = projectsService.findProjectById(project_id);
        return tasksRepository.getTaskByIdAndProject(task_id, project).orElseThrow(TaskNotFoundException::new);
    }
}

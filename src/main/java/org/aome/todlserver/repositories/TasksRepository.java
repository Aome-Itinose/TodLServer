package org.aome.todlserver.repositories;

import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TasksRepository extends JpaRepository<Task, Integer> {
    Optional<Task> getTaskByIdAndProject(int task_id, Project project);
}

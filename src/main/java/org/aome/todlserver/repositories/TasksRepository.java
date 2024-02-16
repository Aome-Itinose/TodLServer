package org.aome.todlserver.repositories;

import org.aome.todlserver.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Task, Integer> {
}

package org.aome.todlserver.repositories;

import org.aome.todlserver.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Integer> {
    List<Project> findAllByName(String name);
    Optional<Project> findByNameAndDescription(String name, String description);
}

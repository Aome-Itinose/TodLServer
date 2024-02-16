package org.aome.todlserver.util;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.services.ProjectsService;
import org.aome.todlserver.util.exceptions.ProjectNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProjectValidator implements Validator {
    private final ProjectsService projectsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Project.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Project project = (Project) target;
        List<Project> projects;
        try {
            projects = projectsService.findAllByName(project.getName());
        }catch (ProjectNotFoundException e){
            return;
        }
        for(Project pr: projects){
            if(Objects.equals(pr.getDescription(), project.getDescription())){
                errors.rejectValue("name", "", "Project with this name and this description already exist");
            }
        }
    }
}

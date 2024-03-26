package org.aome.todlserver.util.validators;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.services.ProjectsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


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
        if(projectsService.isProjectExist(project.getName())){
            errors.rejectValue("name", "", String.format("Project '%s' already exist", project.getName()));
        }
    }
}

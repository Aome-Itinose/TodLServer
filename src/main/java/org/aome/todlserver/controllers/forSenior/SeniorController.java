package org.aome.todlserver.controllers.forSenior;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.ProjectDTO;
import org.aome.todlserver.dto.UserDTO;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.services.ProjectsService;
import org.aome.todlserver.services.UsersService;
import org.aome.todlserver.util.Converter;
import org.aome.todlserver.util.exceptions.project.ProjectNotCreatedException;
import org.aome.todlserver.util.validators.ProjectValidator;
import org.aome.todlserver.util.exceptions.user.UserNotFoundException;
import org.aome.todlserver.util.responses.EditResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class SeniorController {
    private final UsersService usersService;
    private final ProjectsService projectsService;

    private final Converter converter;
    private final ProjectValidator projectValidator;


    @PostMapping("/set_role_senior")
    public ResponseEntity<EditResponse> setAdminRole(@RequestBody @Valid UserDTO userDTO) throws UserNotFoundException{
        usersService.setRoleSenior(converter.convertToUser(userDTO));

        String message = String.format("'%s' now is senior.", userDTO.getUsername());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }
    @PostMapping("/create_project")
    public ResponseEntity<EditResponse> createProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult){
        Project project = converter.convertToProject(projectDTO);

        projectValidator.validate(project, bindingResult);
        if(bindingResult.hasErrors()){
            StringBuilder message = new StringBuilder();
            for(FieldError fe: bindingResult.getFieldErrors()){
                message.append(fe.getField()).append(" - ").append(fe.getDefaultMessage()).append("; ");
            }
            throw new ProjectNotCreatedException(message.toString());
        }

        projectsService.createNewProject(project);

        String message = String.format("Project '%s' is created.", project.getName());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }

}

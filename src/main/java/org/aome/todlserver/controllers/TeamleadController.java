package org.aome.todlserver.controllers;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.ProjectDTO;
import org.aome.todlserver.dto.UserDTO;
import org.aome.todlserver.dto.compositeDTO.Project_User;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.User;
import org.aome.todlserver.security.UsersDetails;
import org.aome.todlserver.services.ProjectsService;
import org.aome.todlserver.services.UsersService;
import org.aome.todlserver.util.exceptions.ProjectEditException;
import org.aome.todlserver.util.exceptions.ProjectNotCreatedException;
import org.aome.todlserver.util.exceptions.UserNotFoundException;
import org.aome.todlserver.util.exceptions.responses.EditResponse;
import org.aome.todlserver.util.exceptions.responses.ExceptionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class TeamleadController {
    private final ProjectsService projectsService;
    private final UsersService usersService;

    private final ModelMapper modelMapper;

    @PostMapping("/edit_team_lead")
    public ResponseEntity<EditResponse> editTeamLead(@RequestBody Project_User projectUser) {

        Project project = convertToProject(projectUser.getProjectDTO());
        User user = convertToUser(projectUser.getUserDTO());

        projectsService.setTeamlead(project, user);

        return new ResponseEntity<>(new EditResponse(String.format("%s now is teamlead.", user.getUsername()), new Date()), HttpStatus.OK);
    }
    @PostMapping("/add_person")
    private ResponseEntity<EditResponse> addPerson(@RequestBody UserDTO userDTO){
        User user = convertToUser(userDTO);
        projectsService.addUser(user);

        String message = String.format("Now %s is in your project.", user.getUsername());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }
    @PostMapping("/delete_developer")
    public ResponseEntity<EditResponse> deleteDeveloper(@RequestBody UserDTO userDTO){
        User user = convertToUser(userDTO);
        User teamlead = getAuthenticatedUser();
        projectsService.deleteUserFromProject(user, teamlead.getMyProject());

        String message = String.format("%s deleter from your project.", user.getUsername());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }

    @PostMapping("/edit_name")
    public ResponseEntity<EditResponse> editProjectName(@RequestBody ProjectDTO projectDTO){
        User teamlead = getAuthenticatedUser();

        projectsService.editProjectName(teamlead.getMyProject(), convertToProject(projectDTO));

        String message = String.format("Project renamed to %s.", projectDTO.getName());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }



    @PostMapping("")
    public ResponseEntity<EditResponse> some(){
        
        String message = String.format("");
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }

//Exception handlers
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(UserNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(ProjectNotCreatedException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(ProjectEditException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


//Additional tools
    private Project convertToProject(ProjectDTO projectDTO){
        return modelMapper.map(projectDTO, Project.class);
    }
    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
    private User getAuthenticatedUser() {
        UsersDetails usersDetails = (UsersDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usersService.findByUsername(usersDetails.getUsername());
    }
}

package org.aome.todlserver.controllers.forTeamlead;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.ProjectDTO;
import org.aome.todlserver.dto.UserDTO;
import org.aome.todlserver.dto.compositeDTO.Project_User;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.User;
import org.aome.todlserver.services.ProjectsService;
import org.aome.todlserver.util.Converter;
import org.aome.todlserver.util.responses.EditResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class TeamleadController {
    private final ProjectsService projectsService;

    private final Converter converter;


    @PostMapping("/edit_team_lead")
    public ResponseEntity<EditResponse> editTeamLead(@RequestBody Project_User projectUser) {

        Project project = converter.convertToProject(projectUser.getProjectDTO());
        User user = converter.convertToUser(projectUser.getUserDTO());

        projectsService.setTeamlead(project, user);

        return new ResponseEntity<>(new EditResponse(String.format("%s now is teamlead.", user.getUsername()), new Date()), HttpStatus.OK);
    }
    @PostMapping("/add_person")
    private ResponseEntity<EditResponse> addPerson(@RequestBody UserDTO userDTO){
        User user = converter.convertToUser(userDTO);
        projectsService.addUser(user);

        String message = String.format("Now %s is in your project.", user.getUsername());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }
    @PostMapping("/delete_developer")
    public ResponseEntity<EditResponse> deleteDeveloper(@RequestBody UserDTO userDTO){
        User user = converter.convertToUser(userDTO);

        projectsService.deleteUserFromProject(user);

        String message = String.format("%s deleter from your project.", user.getUsername());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }
    @PostMapping("/edit_name")
    public ResponseEntity<EditResponse> editProjectName(@RequestBody ProjectDTO projectDTO){
        projectsService.editProjectName(converter.convertToProject(projectDTO));

        String message = String.format("Project renamed to %s.", projectDTO.getName());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }

}

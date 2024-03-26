package org.aome.todlserver.controllers.forUser;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.ProjectDTO;
import org.aome.todlserver.dto.TaskDTO;
import org.aome.todlserver.dto.UserDTO;
import org.aome.todlserver.services.ProjectsService;
import org.aome.todlserver.services.TasksService;
import org.aome.todlserver.services.UsersService;
import org.aome.todlserver.util.Converter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UsersService usersService;
    private final ProjectsService projectsService;
    private final TasksService tasksService;

    private final Converter converter;


    @GetMapping("/getMe")
    public UserDTO getUser() {
        return converter.convertToUserDTO(usersService.findAuthenticatedUser());
    }
    @GetMapping("/getProject")
    public ProjectDTO getProject(@RequestParam("project_id") int project_id) {
        return converter.convertToProjectDTO(projectsService.findProjectById(project_id));
    }
    @GetMapping("/getTask")
    public TaskDTO getTask(@RequestParam("project_id") int project_id,
                           @RequestParam("task_id") int task_id){
        return converter.convertToTaskDTO(tasksService.getByIdAndProjectId(project_id, task_id));
    }
}

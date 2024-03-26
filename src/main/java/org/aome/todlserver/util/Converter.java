package org.aome.todlserver.util;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.ProjectDTO;
import org.aome.todlserver.dto.RegistrationDTO;
import org.aome.todlserver.dto.TaskDTO;
import org.aome.todlserver.dto.UserDTO;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.Task;
import org.aome.todlserver.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Converter {
    private final ModelMapper modelMapper;

    public User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
    public User convertToUser(RegistrationDTO registrationDTO){
        return modelMapper.map(registrationDTO, User.class);
    }
    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    public RegistrationDTO convertToRegistrationDTO(User user){
        return modelMapper.map(user, RegistrationDTO.class);
    }
    public Project convertToProject(ProjectDTO projectDTO){
        return modelMapper.map(projectDTO, Project.class);
    }
    public ProjectDTO convertToProjectDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }
    public TaskDTO convertToTaskDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }
    public Task convertToTask(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }
}

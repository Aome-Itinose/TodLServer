package org.aome.todlserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.aome.todlserver.models.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ProjectDTO {
    @NotBlank(message = "Name should be not empty")
    private String name;

    private String description;

    //TODO: status may be only "planned", "in progress", "completed"
    private String status;

    private List<Task> tasks;
}

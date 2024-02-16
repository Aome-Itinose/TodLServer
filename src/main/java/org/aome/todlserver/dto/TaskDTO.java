package org.aome.todlserver.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class TaskDTO {
    @Size(min = 1, max = 300, message = "Task length should be between 1 and 300")
    @NotBlank(message = "Task should be not empty")
    private String name;

    private String description;

    //TODO: priority may be only 1,2,3,4
    private int priority;

    private LocalDateTime expirationAt;

    @NotNull(message = "'createdAt' IS EMPTY, FUCKING YOU BASTARD")
    private LocalDateTime createdAt;

    private String status;

    private Project project;

    private User worksWho;
}

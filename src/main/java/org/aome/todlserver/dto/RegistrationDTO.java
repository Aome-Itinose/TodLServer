package org.aome.todlserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aome.todlserver.models.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    @NotBlank(message = "Username should be not empty")
    @Size(min = 2, max = 100, message = "Username length should be between 2 and 100")
    private String username;

    @NotBlank(message = "Password should be not empty")
    private String password;

    private String gmail;

    private String role;

    private List<Task> tasks;
}

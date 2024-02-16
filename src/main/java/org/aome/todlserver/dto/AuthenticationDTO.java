package org.aome.todlserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationDTO {
    @NotBlank(message = "Username should be not empty")
    @Size(min = 2, max = 100, message = "Username length should be between 2 and 100")
    private String username;

    @NotBlank(message = "Password should be not empty")
    private String password;
}

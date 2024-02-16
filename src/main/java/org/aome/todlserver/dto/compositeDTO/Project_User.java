package org.aome.todlserver.dto.compositeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aome.todlserver.dto.ProjectDTO;
import org.aome.todlserver.dto.UserDTO;

@Data
@AllArgsConstructor
public class Project_User {
    private final ProjectDTO projectDTO;
    private final UserDTO userDTO;
}

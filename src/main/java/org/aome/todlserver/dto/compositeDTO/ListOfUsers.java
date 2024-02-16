package org.aome.todlserver.dto.compositeDTO;

import lombok.Data;
import org.aome.todlserver.dto.RegistrationDTO;

import java.util.List;

@Data
public class ListOfUsers {
    private List<RegistrationDTO> users;
}

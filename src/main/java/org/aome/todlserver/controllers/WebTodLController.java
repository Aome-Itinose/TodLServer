package org.aome.todlserver.controllers;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.RegistrationDTO;
import org.aome.todlserver.models.User;
import org.aome.todlserver.security.UsersDetails;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebTodLController {
    private final ModelMapper modelMapper;

    @GetMapping("/user_info")
    public ResponseEntity<RegistrationDTO> userInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails user = (UsersDetails) auth.getPrincipal();

        return new ResponseEntity<>(convertToUserDTO(user.getUser()), HttpStatus.FOUND);
    }


//Additional tools
    private RegistrationDTO convertToUserDTO(User user) {
        return modelMapper.map(user, RegistrationDTO.class);
    }
}



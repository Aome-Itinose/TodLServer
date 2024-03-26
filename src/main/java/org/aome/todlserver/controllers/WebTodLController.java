package org.aome.todlserver.controllers;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.RegistrationDTO;
import org.aome.todlserver.security.UsersDetails;
import org.aome.todlserver.util.Converter;
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
    private final Converter converter;


    @GetMapping("/user_info")
    public ResponseEntity<RegistrationDTO> userInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails user = (UsersDetails) auth.getPrincipal();

        return new ResponseEntity<>(converter.convertToRegistrationDTO(user.getUser()), HttpStatus.FOUND);
    }
}



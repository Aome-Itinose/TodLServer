package org.aome.todlserver.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aome.todlserver.dto.AuthenticationDTO;
import org.aome.todlserver.dto.RegistrationDTO;
import org.aome.todlserver.models.User;
import org.aome.todlserver.security.JWTUtil;
import org.aome.todlserver.services.UsersService;
import org.aome.todlserver.util.Converter;
import org.aome.todlserver.util.validators.UserValidator;
import org.aome.todlserver.util.responses.AuthResponse;
import org.aome.todlserver.util.exceptions.user.UserNotCreateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final UsersService usersService;

    private final JWTUtil jwtUtil;
    private final UserValidator userValidator;
    private final AuthenticationManager authProvider;
    private final Converter converter;

    @GetMapping("/some")
    public ResponseEntity<AuthResponse> some(){
        AuthResponse response = new AuthResponse("Account created.", "ss", new Date());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> performRegistration(@RequestBody @Valid RegistrationDTO registrationDTO, BindingResult bindingResult){
        User newUser = converter.convertToUser(registrationDTO);
        userValidator.validate(newUser, bindingResult);
        if(bindingResult.hasErrors()){
            StringBuilder message = new StringBuilder();
            for(FieldError fe: bindingResult.getFieldErrors()){
                message.append(fe.getField()).append(" - ").append(fe.getDefaultMessage()).append("; ");
            }
            throw new UserNotCreateException(message.toString());
        }
        usersService.createNewUser(newUser);

        String token = jwtUtil.generateToken(newUser.getUsername());
        AuthResponse response = new AuthResponse("Account created.", token, new Date());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(), authenticationDTO.getPassword());
        if(authProvider.authenticate(authToken).isAuthenticated()) {

            String token = jwtUtil.generateToken(authenticationDTO.getUsername());
            AuthResponse response = new AuthResponse("You are logged.", token, new Date());
            log.info("Logged.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        AuthResponse response = new AuthResponse("Username or password is incorrect!", "", new Date());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
     }

}

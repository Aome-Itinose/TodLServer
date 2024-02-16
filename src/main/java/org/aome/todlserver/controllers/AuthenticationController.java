package org.aome.todlserver.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.AuthenticationDTO;
import org.aome.todlserver.dto.RegistrationDTO;
import org.aome.todlserver.models.User;
import org.aome.todlserver.security.JWTUtil;
import org.aome.todlserver.services.UsersService;
import org.aome.todlserver.util.validators.UserValidator;
import org.aome.todlserver.util.exceptions.responses.ExceptionResponse;
import org.aome.todlserver.util.exceptions.responses.AuthResponse;
import org.aome.todlserver.util.exceptions.UserNotCreateException;
import org.modelmapper.ModelMapper;
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
public class AuthenticationController {

    private final UsersService usersService;

    private final JWTUtil jwtUtil;
    private final UserValidator userValidator;
    private final AuthenticationManager authProvider;
    private final ModelMapper modelMapper;

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> performRegistration(@RequestBody @Valid RegistrationDTO registrationDTO, BindingResult bindingResult){
        User newUser = convertToUser(registrationDTO);
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
        authProvider.authenticate(authToken);

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        AuthResponse response = new AuthResponse("You are logged.", token, new Date());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


//Exception handlers
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(UserNotCreateException e){
        String prefix = "User not created: ";
        ExceptionResponse response = new ExceptionResponse(
                prefix + e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


//Additional tools
    private User convertToUser(RegistrationDTO registrationDTO){
        return modelMapper.map(registrationDTO, User.class);
    }
}

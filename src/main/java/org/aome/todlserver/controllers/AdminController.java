package org.aome.todlserver.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aome.todlserver.dto.UserDTO;
import org.aome.todlserver.models.User;
import org.aome.todlserver.services.UsersService;
import org.aome.todlserver.util.exceptions.ProjectEditException;
import org.aome.todlserver.util.exceptions.ProjectNotCreatedException;
import org.aome.todlserver.util.exceptions.UserNotFoundException;
import org.aome.todlserver.util.exceptions.responses.EditResponse;
import org.aome.todlserver.util.exceptions.responses.ExceptionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UsersService usersService;

    private final ModelMapper modelMapper;

    @PostMapping("/set_admin")
    public ResponseEntity<EditResponse> setAdmin(@RequestBody @Valid UserDTO userDTO) throws UserNotFoundException {
        usersService.setAdmin(convertToUser(userDTO));

        String message = String.format("'%s' now is admin.", userDTO.getUsername());
        return new ResponseEntity<>(new EditResponse(message, new Date()), HttpStatus.OK);
    }


//Exception handlers
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(UserNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(ProjectNotCreatedException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(ProjectEditException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


//Additional tools
    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

}

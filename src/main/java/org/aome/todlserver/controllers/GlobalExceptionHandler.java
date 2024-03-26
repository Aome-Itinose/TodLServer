package org.aome.todlserver.controllers;

import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.aome.todlserver.util.exceptions.project.ProjectEditException;
import org.aome.todlserver.util.exceptions.project.ProjectNotCreatedException;
import org.aome.todlserver.util.exceptions.project.ProjectNotFoundException;
import org.aome.todlserver.util.exceptions.user.UserNotCreateException;
import org.aome.todlserver.util.exceptions.user.UserNotFoundException;
import org.aome.todlserver.util.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ExceptionResponse> exceptionHandler(UserNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> exceptionHandler(UserNotCreateException e){
        String prefix = "User not created: ";
        ExceptionResponse response = new ExceptionResponse(
                prefix + e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProjectNotCreatedException.class)
    private ResponseEntity<ExceptionResponse> exceptionHandler(ProjectNotCreatedException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProjectEditException.class)
    private ResponseEntity<ExceptionResponse> exceptionHandler(ProjectEditException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProjectNotFoundException.class)
    private ResponseEntity<ExceptionResponse> exceptionHandler(ProjectNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({JWTDecodeException.class, IncorrectClaimException.class, InvalidClaimException.class})
    private ResponseEntity<ExceptionResponse> incorrectTokenExceptionHandler (JWTVerificationException ignoredE) {
        ExceptionResponse response = new ExceptionResponse(
                "Token is incorrect.",
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JWTVerificationException.class)
    private ResponseEntity<ExceptionResponse> exceptionHandler(JWTVerificationException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package org.aome.todlserver.util.exceptions;

public class JWTTokenException extends RuntimeException{
    public JWTTokenException(String message){
        super(message);
    }
}

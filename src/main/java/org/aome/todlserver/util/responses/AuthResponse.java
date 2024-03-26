package org.aome.todlserver.util.responses;

import lombok.Data;

import java.util.Date;

@Data
public class AuthResponse {
    private String message;
    private String jwt_token;
    private Date time;

    public AuthResponse(String message, Date time){
        this.message = message;
        this.time = time;
    }

    public AuthResponse(String message, String jwt_token, Date time) {
        this.message = message;
        this.jwt_token = jwt_token;
        this.time = time;
    }
}

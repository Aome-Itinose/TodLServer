package org.aome.todlserver.util.exceptions.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private Date time;
}

package com.app.tmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoProjectException extends RuntimeException{
    public NoProjectException(String message) {
        super(message);
    }
}

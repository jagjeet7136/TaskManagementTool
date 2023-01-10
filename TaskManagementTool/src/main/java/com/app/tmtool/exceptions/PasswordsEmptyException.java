package com.app.tmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordsEmptyException extends RuntimeException{

    public PasswordsEmptyException(String errorMessage) {
        super(errorMessage);
    }
}

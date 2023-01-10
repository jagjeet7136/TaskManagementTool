package com.app.tmtool.exceptions;

import lombok.Data;

@Data
public class PasswordEmptyExceptionResponse {
    String errorMessage;

    public PasswordEmptyExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

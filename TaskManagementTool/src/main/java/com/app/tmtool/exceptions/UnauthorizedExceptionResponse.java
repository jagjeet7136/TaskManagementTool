package com.app.tmtool.exceptions;

import lombok.Data;

@Data
public class UnauthorizedExceptionResponse {

    private String message;
    public UnauthorizedExceptionResponse(String message) {
        this.message = message;
    }
}

package com.app.tmtool.exceptions;

import lombok.Data;

@Data
public class NoProjectExceptionResponse {
    private String message;

    public NoProjectExceptionResponse(String message) {
        this.message = message;
    }
}

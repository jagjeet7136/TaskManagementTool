package com.app.tmtool.exceptions;

import lombok.Data;

@Data
public class UserNameAlreadyExistsResponse {

    private String username;

    public UserNameAlreadyExistsResponse(String username) {
        this.username = username;
    }
}

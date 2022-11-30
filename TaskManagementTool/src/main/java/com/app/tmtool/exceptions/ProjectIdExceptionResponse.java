package com.app.tmtool.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIdExceptionResponse {

    private String projectIdentifier;
}

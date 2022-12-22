package com.app.tmtool.controller;

import com.app.tmtool.entity.ProjectTask;
import com.app.tmtool.service.MapValidationErrorService;
import com.app.tmtool.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    ProjectTaskService projectTaskService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult
            bindingResult, @PathVariable String backlogId) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap!=null) {
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask);
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

}

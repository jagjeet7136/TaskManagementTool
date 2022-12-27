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
import java.security.Principal;
import java.util.List;

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
            bindingResult, @PathVariable String backlogId, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap!=null) {
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlogId, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findBacklogById(backlogId, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{backlogId}/{projectTaskId}")
    public ResponseEntity<ProjectTask> getProjectTask(@PathVariable String backlogId, @PathVariable String
            projectTaskId, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findProjectTaskByProjectSequence(backlogId, projectTaskId),
                HttpStatus.OK);
    }

}

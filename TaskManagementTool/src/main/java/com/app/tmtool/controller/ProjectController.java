package com.app.tmtool.controller;

import com.app.tmtool.entity.Project;
import com.app.tmtool.service.MapValidationErrorService;
import com.app.tmtool.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createdNewProject(@Valid @RequestBody Project project, BindingResult bindingResult) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap!=null) {
            return errorMap;
        }
        Project project1 = projectService.saveOrUpdate(project);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        Project project = projectService.findProjectByIdentifier(projectId.toUpperCase());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.findAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProjectById(@PathVariable String projectId) {
        projectService.deleteByProjectId(projectId);

        return new ResponseEntity<>("Project with Id:" + projectId + " is deleted", HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateProject(@Valid @RequestBody Project project, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap!=null) {
            return errorMap;
        }
        return new ResponseEntity<>(projectService.updateProject(project), HttpStatus.OK);
    }
}

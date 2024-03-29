package com.app.tmtool.service;

import com.app.tmtool.entity.Backlog;
import com.app.tmtool.entity.Project;
import com.app.tmtool.entity.ProjectTask;
import com.app.tmtool.exceptions.NoProjectException;
import com.app.tmtool.repository.BacklogRepository;
import com.app.tmtool.repository.ProjectTaskRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProjectTaskService.class);

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
        Project project = projectService.findProjectByIdentifier(projectIdentifier, username);
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

        projectTask.setBacklog(backlog);
        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;
        backlog.setPTSequence(backlogSequence);

        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlogId, String sequence, String username) {
        projectService.findProjectByIdentifier(backlogId, username);
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
        if (projectTask == null) {
            throw new NoProjectException("Project Task " + sequence + " not found");
        }
        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            logger.error("Wrong project sequence {} {} ", projectTask, sequence);
            throw new NoProjectException("Project Task " + sequence + " does not exists in project: " + backlogId);
        }
        return projectTask;
    }

    public List<ProjectTask> findBacklogById(String backlogId, String username) {
        Project project = projectService.findProjectByIdentifier(backlogId, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask updateByProjectSequence(ProjectTask projectTask, String backlogId, String projectTaskId,
                                               String username) {
        ProjectTask task = findProjectTaskByProjectSequence(backlogId, projectTaskId, username);
        task = new ModelMapper().map(projectTask, ProjectTask.class);
        return projectTaskRepository.save(task);
    }

    public void deleteProjectTaskByProjectSequence(String backlogId, String projectTaskId, String username) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlogId, projectTaskId, username);
        projectTaskRepository.delete(projectTask);
    }
}

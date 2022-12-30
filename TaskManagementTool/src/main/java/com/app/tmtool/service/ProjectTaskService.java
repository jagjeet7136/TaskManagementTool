package com.app.tmtool.service;

import com.app.tmtool.entity.Backlog;
import com.app.tmtool.entity.Project;
import com.app.tmtool.entity.ProjectTask;
import com.app.tmtool.exceptions.NoProjectException;
import com.app.tmtool.repository.BacklogRepository;
import com.app.tmtool.repository.ProjectTaskRepository;
import org.modelmapper.ModelMapper;
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

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
        Project project = projectService.findProjectByIdentifier(projectIdentifier, username);
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getPriority()==null/* || projectTask.getPriority()==0*/) {
                projectTask.setPriority(3);
            }

            if(projectTask.getStatus()==null || projectTask.getStatus().equals("")) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        }
        catch (Exception exception) {
            throw new NoProjectException("Project Not found");
        }
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlogId, String sequence) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
        if(backlog==null) {
            throw new NoProjectException("Project with ID: " + backlogId + " does not exist");
        }
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
        if(projectTask==null) {
            throw new NoProjectException("Project Task " + sequence + " not found");
        }
        if(!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new NoProjectException("Project Task " + sequence + " does not exists in project: " + backlogId);
        }
        return projectTask;
    }

    public List<ProjectTask> findBacklogById(String backlogId, String username) {
        Project project = projectService.findProjectByIdentifier(backlogId, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask updateByProjectSequence(ProjectTask projectTask, String backlogId, String projectTaskId) {
        ProjectTask task = findProjectTaskByProjectSequence(backlogId, projectTaskId);
        task = new ModelMapper().map(projectTask, ProjectTask.class);
        return projectTaskRepository.save(task);
    }

    public void deleteProjectTaskByProjectSequence(String backlogId, String projectTaskId) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlogId, projectTaskId);
        Backlog backlog = projectTask.getBacklog();
        List<ProjectTask> projectTaskList = backlog.getProjectTaskList();
        projectTaskList.remove(projectTask);
        backlogRepository.save(backlog);
        projectTaskRepository.delete(projectTask);
    }
}

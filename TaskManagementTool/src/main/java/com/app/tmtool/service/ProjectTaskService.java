package com.app.tmtool.service;

import com.app.tmtool.entity.Backlog;
import com.app.tmtool.entity.ProjectTask;
import com.app.tmtool.repository.BacklogRepository;
import com.app.tmtool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
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
}

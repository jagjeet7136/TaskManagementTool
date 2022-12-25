package com.app.tmtool.service;

import com.app.tmtool.entity.Backlog;
import com.app.tmtool.entity.Project;
import com.app.tmtool.entity.User;
import com.app.tmtool.exceptions.NoProjectException;
import com.app.tmtool.exceptions.ProjectIdException;
import com.app.tmtool.repository.BacklogRepository;
import com.app.tmtool.repository.ProjectRepository;
import com.app.tmtool.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BacklogRepository backlogRepository;

    public Project saveOrUpdate(Project project, String username) {
        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            Backlog backlog = new Backlog();
            project.setBacklog(backlog);
            backlog.setProject(project);
            backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }catch (Exception ex) {
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() +
                    " already exists");
        }
    }

    public Project findProjectByIdentifier(String projectIdentifier, String username){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project==null) {
            throw new ProjectIdException("Project Id " + projectIdentifier.toUpperCase() +
                    " do not exists");
        }
        if(!project.getProjectLeader().equals(username)) {
            throw new NoProjectException("Project Not found in your account");
        }
        return project;
    }

    public List<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteByProjectId(String projectId, String username){
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

    public Project updateProject(Project project, String username){
        Project project1 = projectRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase());

        if(project1==null) {
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() +
                    " do not exists");
        }
        else {
            if(!project1.getProjectLeader().equals(username)) {
                throw new NoProjectException("Project Not found in your account");
            }
        }
        project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
        project1 = new ModelMapper().map(project, Project.class);
        return projectRepository.save(project1);
    }
}
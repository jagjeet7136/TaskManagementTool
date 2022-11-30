package com.app.tmtool.service;

import com.app.tmtool.entity.Project;
import com.app.tmtool.exceptions.ProjectIdException;
import com.app.tmtool.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public Project saveOrUpdate(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }catch (Exception ex) {
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() +
                    " already exists");
        }
    }

    public Project findProjectByIdentifier(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project==null) {
            throw new ProjectIdException("Project Id " + projectIdentifier.toUpperCase() +
                    " do not exists");
        }
        return project;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteByProjectId(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project==null) {
            throw new ProjectIdException("Project Id " + projectId.toUpperCase() +
                    " do not exists");
        }
        projectRepository.delete(project);
    }

    public Project updateProject(Project project) {
        Project project1 = projectRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase());

        if(project1==null) {
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() +
                    " do not exists");
        }
        project1 = new ModelMapper().map(project, Project.class);
        return projectRepository.save(project1);
    }
}

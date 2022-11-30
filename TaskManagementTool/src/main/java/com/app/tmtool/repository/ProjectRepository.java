package com.app.tmtool.repository;

import com.app.tmtool.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByProjectIdentifier(String projectIdentifier);
}

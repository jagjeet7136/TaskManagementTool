package com.app.tmtool.repository;

import com.app.tmtool.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByProjectIdentifier(String projectIdentifier);

    List<Project> findAllByProjectLeader(String username);
}

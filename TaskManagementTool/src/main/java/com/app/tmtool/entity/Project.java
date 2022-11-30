package com.app.tmtool.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "project name is required")
    private String projectName;

    @NotBlank(message = "project identifier is required")
    @Size(min = 4, max = 5, message = "please use 4 to 5 characters")
    @Column(updatable = false, unique = true)
    private String projectIdentifier;

    @NotBlank(message = "project description is required")
    private String description;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date endDate;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date updatedAt;

}

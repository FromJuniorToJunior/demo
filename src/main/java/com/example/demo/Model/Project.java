package com.example.demo.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@Document
public class Project {
    @Id
    private String projectId;
    @DBRef
    private List<Task> assignedTasks;
    @DBRef
    private List<Project> connectedProjects;
    private String title;
    private String description;
    private LocalDateTime creationTime;
    private String startDate;
    private String endDate;

}

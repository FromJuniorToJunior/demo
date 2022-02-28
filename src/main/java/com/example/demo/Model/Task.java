package com.example.demo.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
@Document
public class Task {
    /*tytułu, treści, daty utworzenia, daty początku, daty końca, przypisanych użytkowników, priorytetu*/
    @Id
    private String taskId;
    private String description;
    private List<Task> subTasks;
    private String title;
    private LocalDateTime creationTime;
    private String startDate;
    private String endDate;
    @DBRef
    private List<User> asignedUsers;
    private Priority priority;
    private Project project;
}

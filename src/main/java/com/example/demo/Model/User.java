package com.example.demo.Model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
    @Id
    private String userId;
    private String name;
    private String surname;
    private String email;
    private String dateOfBirth;
    @DBRef
    private List<Task> assignedTasks;



}

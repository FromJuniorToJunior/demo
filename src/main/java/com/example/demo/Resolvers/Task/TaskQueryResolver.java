package com.example.demo.Resolvers.Task;

import com.example.demo.Model.Task;
import com.example.demo.Model.User;
import com.example.demo.Repository.TaskRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@AllArgsConstructor
@Component
public class TaskQueryResolver implements GraphQLQueryResolver {
    private final TaskRepository taskRepository;
    private final MongoTemplate mongoTemplate;

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }
    public List<Task> getTasksSortByDate(){
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC,"creationTime"));
        return mongoTemplate.find(query,Task.class);
    }
    public List<Task> getTasksSortByTitle(){
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC,"title"));
        return mongoTemplate.find(query,Task.class);
    }

}

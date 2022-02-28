package com.example.demo.Resolvers.Project;

import com.example.demo.Model.Project;
import com.example.demo.Repository.ProjectRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ProjectQueryResolver implements GraphQLQueryResolver {
    private final ProjectRepository projectRepository;
    private final MongoTemplate mongoTemplate;
    public Project getProject(String id){

        return projectRepository.findById(id).orElseThrow();
    }
    public List<Project> getProjects(){
        return projectRepository.findAll();
    }
    public Project getProjectByTitle(String title){
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));
        return mongoTemplate.findOne(query,Project.class);
    }
}

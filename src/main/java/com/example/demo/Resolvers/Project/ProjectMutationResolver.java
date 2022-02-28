package com.example.demo.Resolvers.Project;

import com.example.demo.Model.Project;
import com.example.demo.Model.Task;
import com.example.demo.Repository.ProjectRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor
@Component
public class ProjectMutationResolver implements GraphQLMutationResolver {

    private final ProjectRepository projectRepository;
    private final MongoTemplate mongoTemplate;
    private boolean checkIfTitleExist(String title){
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));

        if(mongoTemplate.find(query,Task.class).size()==0){
            return false;
        }else
            return true;
    }

    public Project createProject(Project project) throws Exception {
        if(checkIfTitleExist(project.getTitle())){
            throw new Exception("Task with similar title already exists");
        }
        project.setCreationTime(LocalDateTime.now());
        project.setAssignedTasks(new ArrayList<>());
        project.setConnectedProjects(new ArrayList<>());
        return projectRepository.save(project);
    }
    public Project updateProject(String id, Project project) throws Exception {
        Project update = projectRepository.findById(id).orElseThrow();

        if(project.getTitle()!=null){
            if(checkIfTitleExist(project.getTitle())){
                throw new Exception("Task with similar title already exists");
            }
            update.setTitle(project.getTitle());
        }
        if(project.getDescription() !=null){
            update.setDescription(project.getDescription());
        }
        if(project.getStartDate() !=null){
            update.setStartDate(update.getStartDate());
        }
        if(project.getEndDate() !=null){
            update.setEndDate(project.getEndDate());
        }

        return projectRepository.save(update);
    }
    public String deleteProject(String id){
        Project projectToDelete = projectRepository.findById(id).orElseThrow();
        //clean ref with other projects
        for(Project project: projectToDelete.getConnectedProjects()){
            for(Project projectFromRef: project.getConnectedProjects()){
                if(projectFromRef.equals(projectToDelete)){
                    project.getConnectedProjects().remove(projectFromRef);
                    projectRepository.save(project);
                    break;
                }
            }
        }

        for(Task task: projectToDelete.getAssignedTasks()){
            task.setProject(null);
        }

        projectRepository.delete(projectToDelete);


        return id;
    }
    public Project projAddProject(String id, String subProject){
        Project addRef = projectRepository.findById(id).orElseThrow();
        addRef.getConnectedProjects().add(projectRepository.findById(subProject).orElseThrow());
     return projectRepository.save(addRef);
    }
}

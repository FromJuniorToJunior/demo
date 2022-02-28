package com.example.demo.Resolvers.Task;

import com.example.demo.Model.Project;
import com.example.demo.Model.Task;
import com.example.demo.Model.User;
import com.example.demo.Repository.ProjectRepository;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AllArgsConstructor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class TaskMutationResolver implements GraphQLMutationResolver {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
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

    public Project createTask(Task task, String projectId) throws Exception {
        if(checkIfTitleExist(task.getTitle())){
            throw new Exception("Task with similar title already exists");
        }
        task.setProject(projectRepository.findById(projectId).orElseThrow());
        task.setCreationTime(LocalDateTime.now());
        task.setAsignedUsers(new ArrayList<>());
        task.setSubTasks(new ArrayList<>());
        taskRepository.save(task);
        Project project = projectRepository.findById(projectId).orElseThrow();
        project.getAssignedTasks().add(task);

        return projectRepository.save(project);

    }
    public User taskAddUser(String id, String taskId){
        User user = userRepository.findById(id).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();
        /*taskRepository.findById(taskId).orElseThrow().getAsignedUsers().add(userRepository.findById(id).orElseThrow());*/

        task.getAsignedUsers().add(user);
        user.getAssignedTasks().add(task);


        taskRepository.save(task);
        userRepository.save(user);



        return userRepository.findById(id).orElseThrow();
    }
    public Task taskAddSubTask(String id, String subId){
        Task task = taskRepository.findById(id).orElseThrow();
        task.getSubTasks().add(taskRepository.findById(subId).orElseThrow());
        return taskRepository.save(task);
    }
    public Task updateTask(String id, Task task) throws Exception {
        Task update = taskRepository.findById(id).orElseThrow();

        if(task.getDescription()!=null){
            update.setDescription(task.getDescription());
        }
        if(task.getEndDate() !=null){
            update.setEndDate(task.getEndDate());
        }
        if(task.getStartDate() !=null){
            update.setStartDate(task.getStartDate());
        }
        if(task.getPriority() !=null){
            update.setPriority(task.getPriority());
        }
        if(task.getTitle() !=null){
            if(checkIfTitleExist(task.getTitle())){
                throw new Exception("Task with similar title already exists");
            }
            update.setTitle(task.getTitle());
        }
        return taskRepository.save(update);
    }
    public String deleteTask(String id){
        //ToDo //problem przy posiadaniu subtaskow przez subtaski
        Task taskToDelete = taskRepository.findById(id).orElseThrow();
        Project upper = taskToDelete.getProject();
        //clean ref from subtasks
        for(Task task: taskToDelete.getSubTasks()){
            for(Task tasksInProject: taskToDelete.getProject().getAssignedTasks()){
                if(task.equals(tasksInProject)){
                    taskToDelete.getProject().getAssignedTasks().remove(tasksInProject);
                    break;
                }
            }
        }
        //clean ref to task
        upper.getAssignedTasks().remove(taskToDelete);
        projectRepository.save(upper);

        //clean ref from users
        for(User user: taskToDelete.getAsignedUsers()){
            user.getAssignedTasks().remove(taskToDelete);
            for(Task task: user.getAssignedTasks()){
                if(task.equals(taskToDelete)){
                    user.getAssignedTasks().remove(task);
                    break;
                }
            }
        }

        taskRepository.delete(taskToDelete);


        return id;
    }
}

package com.example.demo.Resolvers.User;

import com.example.demo.Model.Task;
import com.example.demo.Model.User;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class UserMutationResolver implements GraphQLMutationResolver {
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private MongoTemplate mongoTemplate;


    public User createUser(User user) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(user.getEmail()));

        if (mongoTemplate.find(query, User.class).size() != 0) {
            throw new Exception("User with this email already exists");
        }
        user.setAssignedTasks(new ArrayList<>());

        return userRepository.save(user);
    }

    public User updateUser(String id, User user) throws Exception {
        User update = userRepository.findById(id).orElseThrow();
        if (user.getName() != null) {
            update.setName(user.getName());
        }
        if (user.getSurname() != null) {
            update.setSurname(user.getSurname());
        }
        if (user.getEmail() != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(user.getEmail()));
            if (mongoTemplate.find(query, User.class).size() != 0) {
                throw new Exception("User with this email already exists");
            }
            update.setEmail(user.getEmail());
        }
        if (user.getDateOfBirth() != null) {
            update.setDateOfBirth(user.getDateOfBirth());
        }


        return userRepository.save(update);
    }

    public String deleteUser(String id) {
        User userToDelete = userRepository.findById(id).orElseThrow();
        for (Task task : userToDelete.getAssignedTasks()) {
            for (User user : task.getAsignedUsers()) {
                if (user.getUserId().equals(id)) {
                    task.getAsignedUsers().remove(user);
                    taskRepository.save(task);
                    break;
                }
            }
        }

        userRepository.delete(userToDelete);
        return id;
    }
}

package com.example.demo.Resolvers.User;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@AllArgsConstructor
@Component
public class UserQueryResolver implements GraphQLQueryResolver {
    private UserRepository userRepository;
    private MongoTemplate mongoTemplate;

    public List<User> getUsers(){
       return userRepository.findAll();
    }
    public List<User> getUsersBySurname(String surname){
        Query query = new Query();
        query.addCriteria(Criteria.where("surname").is(surname));
        return mongoTemplate.find(query,User.class);
    }
    public User getUserByEmail(String email){
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query,User.class);
    }


}

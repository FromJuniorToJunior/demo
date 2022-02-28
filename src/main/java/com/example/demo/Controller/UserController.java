/*
package com.example.demo.Controller;

import com.example.demo.Model.User;

import com.example.demo.Resolvers.User.UserMutationResolver;
import com.example.demo.Resolvers.User.UserQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@AllArgsConstructor
public class UserController {
    private UserQueryResolver userQueryResolver;
    private UserMutationResolver userMutationResolver;
    @GetMapping("users")
    public List<User> getAllUsers(){
        return userQueryResolver.getUsers();
    }
    @PostMapping("users")
    public User createUser(String name, String surname, String email, String dateOfBirth){
        return userMutationResolver.createUser(name,surname,email,dateOfBirth);
    }
}
*/

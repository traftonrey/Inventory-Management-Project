package com.skillstorm.traftonreynolds.project1traftonreynolds.services;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.traftonreynolds.project1traftonreynolds.dtos.UserDto;
import com.skillstorm.traftonreynolds.project1traftonreynolds.mappers.UserMapper;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.User;

@Service
public class UserService {
    /*
     * all business logic for controllers and repositories
     */

     @Autowired
     UserMapper mapper;

    //  @Autowired
    //  UserRepository repository;

     public List<UserDto> findAllUsers() {
        
        List<User> users = new LinkedList<>();

        users.add(new User(0, "Trafton", "Reynolds", "reynoldstrafton@gmail.com", "goatedPW123"));
        users.add(new User(1, "Austin", "Reeves", "areeves@gmail.com", "goatedPW123"));
        users.add(new User(2, "Jordan", "Espinosa", "jespinosa@gmail.com", "goatedPW123"));
        users.add(new User(3, "Chris", "Behrens", "cbehrens@gmail.com", "goatedPW123"));

        // converting list<user> to stream<user>, then converting steam<user> to stream<userdto>, then stream<userdto> to list<userdto>
        List<UserDto> userDtos = users.stream().map(mapper::toDto).collect(Collectors.toList());
        return userDtos;
     }
    
     public List<User> findUserByFirstName(String name) {
        List<User> users = new LinkedList<>();

        users.add(new User(0, name, "Reynolds", "reynoldstrafton@gmail.com", "goatedPW123"));
        users.add(new User(1, name, "Reeves", "areeves@gmail.com", "goatedPW123"));
        users.add(new User(2, name, "Espinosa", "jespinosa@gmail.com", "goatedPW123"));
        users.add(new User(3, name, "Behrens", "cbehrens@gmail.com", "goatedPW123"));

        return users;
     }

     public User findUserById(long id) {
        
        return new User(0, "Trafton", "Reynolds", "reynoldstrafton@gmail.com", "goatedPW123");
     }

     public User createUser(User user) {
        user.setId(1000);
        return user;
     }

     public User updateUser(long id, User user) {
        user.setId(id);
        return user;
     }

     public User deleteUser(long id, User user) {
        return user;
     }
}
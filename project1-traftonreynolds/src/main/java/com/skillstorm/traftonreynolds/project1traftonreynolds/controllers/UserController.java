package com.skillstorm.traftonreynolds.project1traftonreynolds.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.traftonreynolds.project1traftonreynolds.dtos.UserDto;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.User;
import com.skillstorm.traftonreynolds.project1traftonreynolds.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;
    
    @RequestMapping("/hellr")
    public String hellr() {
        return "Hellr wrrl";
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        return service.findAllUsers();
    }

    @GetMapping("/first_name")
    public List<User> findByFirstName(@RequestParam String firstName) {
        return service.findUserByFirstName(firstName);
    }

    @GetMapping("/user/{id}")
    public User findById(@PathVariable long id) {
        return service.findUserById(id);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = service.createUser(user);
        return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);       // sets the status code to 201 - CREATED
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User user) {
        User updatedUser = service.updateUser(id, user);
        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);            // sets the state code to 200 - OK
    }

    @PostMapping("/user/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id, @RequestBody User user) {
        User deletedUser = service.deleteUser(id, user);
        return new ResponseEntity<User>(deletedUser, HttpStatus.NO_CONTENT);            // sets the state code to 204 - NO CONTENT
    }
}

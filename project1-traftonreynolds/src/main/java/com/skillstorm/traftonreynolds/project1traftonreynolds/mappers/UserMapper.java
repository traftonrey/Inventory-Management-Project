package com.skillstorm.traftonreynolds.project1traftonreynolds.mappers;

import org.springframework.stereotype.Component;

import com.skillstorm.traftonreynolds.project1traftonreynolds.dtos.UserDto;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.User;

 @Component
public class UserMapper {
    
    public User toUser(UserDto dto) {
        return new User(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getEmail());
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

}

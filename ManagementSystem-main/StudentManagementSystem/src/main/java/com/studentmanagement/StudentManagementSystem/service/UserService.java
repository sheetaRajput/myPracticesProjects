package com.studentmanagement.StudentManagementSystem.service;

import com.studentmanagement.StudentManagementSystem.entity.User;

import com.studentmanagement.StudentManagementSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;


    public User saveUser(User user) {
        user.setPassword(passwordEncode.encode(user.getPassword()));
        user.setRole("Role_User");
        return userRepo.save(user);
    }

    public boolean checkEmail(String username) {
        return userRepo.existsByUsername(username);

    }
}

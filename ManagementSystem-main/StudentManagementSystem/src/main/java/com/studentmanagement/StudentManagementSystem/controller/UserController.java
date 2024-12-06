package com.studentmanagement.StudentManagementSystem.controller;

import com.studentmanagement.StudentManagementSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepo userRepo;

//    @ModelAttribute
//    private void userDetails(Model m, Principal p) {
//        String email = p.getName();
//        UserDtls user = userRepo.findByEmail(email);
//
//        m.addAttribute("user", user);
//
//    }

    @GetMapping("/")
    public String home() {
        return "users/home";
    }
}

